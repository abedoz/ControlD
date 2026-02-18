package com.controld.app.data.repository

import com.controld.app.data.api.ControlDApi
import com.controld.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val api: ControlDApi
) {
    suspend fun listDevices(): Result<List<Device>> {
        return try {
            val response = api.listDevices()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.devices ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list devices"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createDevice(name: String, profileId: String? = null, icon: String? = null): Result<Device> {
        return try {
            val response = api.createDevice(name, profileId, icon)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.device?.let { Result.success(it) }
                    ?: Result.failure(Exception("No device data returned"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create device"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun modifyDevice(
        deviceId: String,
        name: String? = null,
        profileId: String? = null,
        icon: String? = null,
        status: Int? = null
    ): Result<Device> {
        return try {
            val response = api.modifyDevice(deviceId, name, profileId, icon, status)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.device?.let { Result.success(it) }
                    ?: Result.failure(Exception("No device data returned"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to modify device"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteDevice(deviceId: String): Result<Unit> {
        return try {
            val response = api.deleteDevice(deviceId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to delete device"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
