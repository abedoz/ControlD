package com.controld.app.ui.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.controld.app.data.model.Profile
import com.controld.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfilesUiState(
    val profiles: List<Profile> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showCreateDialog: Boolean = false,
    val actionMessage: String? = null
)

@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfilesUiState())
    val uiState: StateFlow<ProfilesUiState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    fun loadProfiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            profileRepository.listProfiles().fold(
                onSuccess = { profiles ->
                    _uiState.value = _uiState.value.copy(
                        profiles = profiles,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load profiles"
                    )
                }
            )
        }
    }

    fun showCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = true)
    }

    fun hideCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = false)
    }

    fun clearActionMessage() {
        _uiState.value = _uiState.value.copy(actionMessage = null)
    }

    fun createProfile(name: String, cloneFrom: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showCreateDialog = false)

            profileRepository.createProfile(name, cloneFrom).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Profile created")
                    loadProfiles()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }

    fun deleteProfile(profileId: String) {
        viewModelScope.launch {
            profileRepository.deleteProfile(profileId).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Profile deleted")
                    loadProfiles()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }
}
