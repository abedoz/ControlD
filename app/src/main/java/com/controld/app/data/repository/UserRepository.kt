package com.controld.app.data.repository

import com.controld.app.data.api.ControlDApi
import com.controld.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: ControlDApi
) {
    suspend fun getUser(): Result<User> {
        return try {
            val response = api.getUser()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.user?.let { Result.success(it) }
                    ?: Result.failure(Exception("No user data"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getIp(): Result<IpBody> {
        return try {
            val response = api.getIp()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.let { Result.success(it) }
                    ?: Result.failure(Exception("No IP data"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to get IP"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
