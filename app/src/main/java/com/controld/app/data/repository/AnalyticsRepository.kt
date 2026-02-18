package com.controld.app.data.repository

import com.controld.app.data.api.ControlDApi
import com.controld.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsRepository @Inject constructor(
    private val api: ControlDApi
) {
    suspend fun listEndpoints(): Result<List<AnalyticsEndpoint>> {
        return try {
            val response = api.listAnalyticsEndpoints()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.endpoints ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list analytics endpoints"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
