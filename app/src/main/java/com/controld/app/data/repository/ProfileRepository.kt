package com.controld.app.data.repository

import com.controld.app.data.api.ControlDApi
import com.controld.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ControlDApi
) {
    // Profiles CRUD
    suspend fun listProfiles(): Result<List<Profile>> {
        return try {
            val response = api.listProfiles()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.profiles ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list profiles"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProfile(name: String, cloneFrom: String? = null): Result<Profile> {
        return try {
            val response = api.createProfile(name, cloneFrom)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.profile?.let { Result.success(it) }
                    ?: Result.failure(Exception("No profile data returned"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun modifyProfile(profileId: String, name: String?): Result<Profile> {
        return try {
            val response = api.modifyProfile(profileId, name)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.profile?.let { Result.success(it) }
                    ?: Result.failure(Exception("No profile data returned"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to modify profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProfile(profileId: String): Result<Unit> {
        return try {
            val response = api.deleteProfile(profileId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to delete profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Profile Options
    suspend fun listProfileOptions(): Result<List<ProfileOption>> {
        return try {
            val response = api.listProfileOptions()
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.options ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list options"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun modifyProfileOption(profileId: String, optionName: String, status: Int): Result<Unit> {
        return try {
            val response = api.modifyProfileOption(profileId, optionName, status)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to modify option"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Services
    suspend fun listServices(profileId: String): Result<List<ServiceCategory>> {
        return try {
            val response = api.listServices(profileId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.services ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list services"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun modifyService(profileId: String, serviceId: String, status: Int, doValue: String? = null): Result<Unit> {
        return try {
            val response = api.modifyService(profileId, serviceId, status, doValue)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to modify service"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Custom Rules
    suspend fun listRules(profileId: String): Result<RulesBody> {
        return try {
            val response = api.listRules(profileId)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.body?.let { Result.success(it) }
                    ?: Result.failure(Exception("No rules data"))
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list rules"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRule(profileId: String, hostAction: String, status: Int, group: String? = null): Result<Unit> {
        return try {
            val response = api.createRule(profileId, hostAction, status, group)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create rule"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRule(profileId: String, rulePk: String): Result<Unit> {
        return try {
            val response = api.deleteRule(profileId, rulePk)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to delete rule"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRuleFolder(profileId: String, name: String): Result<Unit> {
        return try {
            val response = api.createRuleFolder(profileId, name)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to create folder"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Filters
    suspend fun listFilters(profileId: String): Result<List<Filter>> {
        return try {
            val response = api.listFilters(profileId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.filters ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list filters"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun listExternalFilters(profileId: String): Result<List<Filter>> {
        return try {
            val response = api.listExternalFilters(profileId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.body?.filters ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to list external filters"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun modifyFilter(profileId: String, filterPk: String, status: Int): Result<Unit> {
        return try {
            val response = api.modifyFilter(profileId, filterPk, status)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to modify filter"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
