package com.controld.app.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.controld.app.data.model.Device
import com.controld.app.data.model.Profile
import com.controld.app.data.repository.DeviceRepository
import com.controld.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DevicesUiState(
    val devices: List<Device> = emptyList(),
    val profiles: List<Profile> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val showCreateDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val editingDevice: Device? = null,
    val actionMessage: String? = null
)

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DevicesUiState())
    val uiState: StateFlow<DevicesUiState> = _uiState.asStateFlow()

    init {
        loadDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            deviceRepository.listDevices().fold(
                onSuccess = { devices ->
                    _uiState.value = _uiState.value.copy(
                        devices = devices,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load devices"
                    )
                }
            )

            // Also load profiles for the assign dialog
            profileRepository.listProfiles().fold(
                onSuccess = { profiles ->
                    _uiState.value = _uiState.value.copy(profiles = profiles)
                },
                onFailure = { /* non-critical */ }
            )
        }
    }

    fun showCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = true)
    }

    fun hideCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = false)
    }

    fun showEditDialog(device: Device) {
        _uiState.value = _uiState.value.copy(showEditDialog = true, editingDevice = device)
    }

    fun hideEditDialog() {
        _uiState.value = _uiState.value.copy(showEditDialog = false, editingDevice = null)
    }

    fun clearActionMessage() {
        _uiState.value = _uiState.value.copy(actionMessage = null)
    }

    fun createDevice(name: String, profileId: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showCreateDialog = false)

            deviceRepository.createDevice(name, profileId).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Device created")
                    loadDevices()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }

    fun modifyDevice(deviceId: String, name: String?, profileId: String?, status: Int?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showEditDialog = false, editingDevice = null)

            deviceRepository.modifyDevice(deviceId, name, profileId, status = status).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Device updated")
                    loadDevices()
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        actionMessage = "Failed: ${e.message}"
                    )
                }
            )
        }
    }

    fun deleteDevice(deviceId: String) {
        viewModelScope.launch {
            deviceRepository.deleteDevice(deviceId).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(actionMessage = "Device deleted")
                    loadDevices()
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
