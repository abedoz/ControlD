package com.controld.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.controld.app.data.model.Device
import com.controld.app.data.model.Profile
import com.controld.app.data.model.User
import com.controld.app.data.repository.DeviceRepository
import com.controld.app.data.repository.ProfileRepository
import com.controld.app.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val user: User? = null,
    val currentIp: String? = null,
    val datacenter: String? = null,
    val deviceCount: Int = 0,
    val profileCount: Int = 0,
    val devices: List<Device> = emptyList(),
    val profiles: List<Profile> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val deviceRepository: DeviceRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            var hasError = false

            // Load user info
            userRepository.getUser().fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(user = user)
                },
                onFailure = { hasError = true }
            )

            // Load IP info
            userRepository.getIp().fold(
                onSuccess = { ipBody ->
                    _uiState.value = _uiState.value.copy(
                        currentIp = ipBody.ip,
                        datacenter = ipBody.datacenter
                    )
                },
                onFailure = { /* non-critical */ }
            )

            // Load device count
            deviceRepository.listDevices().fold(
                onSuccess = { devices ->
                    _uiState.value = _uiState.value.copy(
                        deviceCount = devices.size,
                        devices = devices
                    )
                },
                onFailure = { hasError = true }
            )

            // Load profile count
            profileRepository.listProfiles().fold(
                onSuccess = { profiles ->
                    _uiState.value = _uiState.value.copy(
                        profileCount = profiles.size,
                        profiles = profiles
                    )
                },
                onFailure = { hasError = true }
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = if (hasError) "Some data could not be loaded" else null
            )
        }
    }
}
