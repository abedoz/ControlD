package com.controld.app.ui.profiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.controld.app.data.model.*
import com.controld.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileDetailUiState(
    val profileId: String = "",
    val profileName: String = "",
    val services: List<ServiceCategory> = emptyList(),
    val rules: List<Rule> = emptyList(),
    val ruleGroups: List<RuleGroup> = emptyList(),
    val filters: List<Filter> = emptyList(),
    val externalFilters: List<Filter> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedTab: Int = 0,
    val actionMessage: String? = null
)

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    fun initialize(profileId: String, profileName: String) {
        _uiState.value = _uiState.value.copy(profileId = profileId, profileName = profileName)
        loadAll()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun clearActionMessage() {
        _uiState.value = _uiState.value.copy(actionMessage = null)
    }

    fun loadAll() {
        val profileId = _uiState.value.profileId
        if (profileId.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Load services
            profileRepository.listServices(profileId).fold(
                onSuccess = { services ->
                    _uiState.value = _uiState.value.copy(services = services)
                },
                onFailure = { /* continue loading others */ }
            )

            // Load rules
            profileRepository.listRules(profileId).fold(
                onSuccess = { rulesBody ->
                    _uiState.value = _uiState.value.copy(
                        rules = rulesBody.rules,
                        ruleGroups = rulesBody.groups ?: emptyList()
                    )
                },
                onFailure = { /* continue */ }
            )

            // Load filters
            profileRepository.listFilters(profileId).fold(
                onSuccess = { filters ->
                    _uiState.value = _uiState.value.copy(filters = filters)
                },
                onFailure = { /* continue */ }
            )

            // Load external filters
            profileRepository.listExternalFilters(profileId).fold(
                onSuccess = { filters ->
                    _uiState.value = _uiState.value.copy(externalFilters = filters)
                },
                onFailure = { /* continue */ }
            )

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Service actions
    fun modifyService(serviceId: String, status: Int, doValue: String?) {
        viewModelScope.launch {
            profileRepository.modifyService(
                _uiState.value.profileId, serviceId, status, doValue
            ).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Service updated")
                    loadAll()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }

    // Rule actions
    fun createRule(host: String, status: Int, group: String?) {
        viewModelScope.launch {
            profileRepository.createRule(
                _uiState.value.profileId, host, status, group
            ).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Rule created")
                    loadAll()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }

    fun deleteRule(rulePk: String) {
        viewModelScope.launch {
            profileRepository.deleteRule(_uiState.value.profileId, rulePk).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Rule deleted")
                    loadAll()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }

    // Filter actions
    fun modifyFilter(filterPk: String, status: Int) {
        viewModelScope.launch {
            profileRepository.modifyFilter(_uiState.value.profileId, filterPk, status).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Filter updated")
                    loadAll()
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
