package com.controld.app.data.api

import com.controld.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ControlDApi {

    // User / Account
    @GET("users")
    suspend fun getUser(): Response<ApiResponse<UserBody>>

    @GET("ip")
    suspend fun getIp(): Response<ApiResponse<IpBody>>

    // Devices
    @GET("devices")
    suspend fun listDevices(): Response<ApiResponse<DevicesBody>>

    @POST("devices")
    @FormUrlEncoded
    suspend fun createDevice(
        @Field("name") name: String,
        @Field("profile_id") profileId: String? = null,
        @Field("icon") icon: String? = null
    ): Response<ApiResponse<DeviceBody>>

    @PUT("devices/{deviceId}")
    @FormUrlEncoded
    suspend fun modifyDevice(
        @Path("deviceId") deviceId: String,
        @Field("name") name: String? = null,
        @Field("profile_id") profileId: String? = null,
        @Field("icon") icon: String? = null,
        @Field("status") status: Int? = null
    ): Response<ApiResponse<DeviceBody>>

    @DELETE("devices/{deviceId}")
    suspend fun deleteDevice(
        @Path("deviceId") deviceId: String
    ): Response<ApiResponse<Any>>

    // Profiles
    @GET("profiles")
    suspend fun listProfiles(): Response<ApiResponse<ProfilesBody>>

    @POST("profiles")
    @FormUrlEncoded
    suspend fun createProfile(
        @Field("name") name: String,
        @Field("clone_from") cloneFrom: String? = null
    ): Response<ApiResponse<ProfileBody>>

    @PUT("profiles/{profileId}")
    @FormUrlEncoded
    suspend fun modifyProfile(
        @Path("profileId") profileId: String,
        @Field("name") name: String? = null
    ): Response<ApiResponse<ProfileBody>>

    @DELETE("profiles/{profileId}")
    suspend fun deleteProfile(
        @Path("profileId") profileId: String
    ): Response<ApiResponse<Any>>

    // Profile Options
    @GET("profiles/options")
    suspend fun listProfileOptions(): Response<ApiResponse<ProfileOptionsBody>>

    @PUT("profiles/{profileId}/options/{optionName}")
    @FormUrlEncoded
    suspend fun modifyProfileOption(
        @Path("profileId") profileId: String,
        @Path("optionName") optionName: String,
        @Field("status") status: Int
    ): Response<ApiResponse<Any>>

    // Services
    @GET("profiles/{profileId}/services")
    suspend fun listServices(
        @Path("profileId") profileId: String
    ): Response<ApiResponse<ServicesBody>>

    @PUT("profiles/{profileId}/services/{serviceId}")
    @FormUrlEncoded
    suspend fun modifyService(
        @Path("profileId") profileId: String,
        @Path("serviceId") serviceId: String,
        @Field("status") status: Int,
        @Field("do") doValue: String? = null
    ): Response<ApiResponse<Any>>

    // Custom Rules
    @GET("profiles/{profileId}/rules")
    suspend fun listRules(
        @Path("profileId") profileId: String
    ): Response<ApiResponse<RulesBody>>

    @GET("profiles/{profileId}/rules/{folderId}")
    suspend fun listRulesInFolder(
        @Path("profileId") profileId: String,
        @Path("folderId") folderId: String
    ): Response<ApiResponse<RulesBody>>

    @POST("profiles/{profileId}/rules")
    @FormUrlEncoded
    suspend fun createRule(
        @Path("profileId") profileId: String,
        @Field("do") hostAction: String,
        @Field("status") status: Int,
        @Field("group") group: String? = null
    ): Response<ApiResponse<RuleBody>>

    @PUT("profiles/{profileId}/rules")
    @FormUrlEncoded
    suspend fun modifyRule(
        @Path("profileId") profileId: String,
        @Field("PK") rulePk: String,
        @Field("status") status: Int,
        @Field("do") doValue: String? = null
    ): Response<ApiResponse<Any>>

    @DELETE("profiles/{profileId}/rules/{rulePk}")
    suspend fun deleteRule(
        @Path("profileId") profileId: String,
        @Path("rulePk") rulePk: String
    ): Response<ApiResponse<Any>>

    // Rule Folders
    @POST("profiles/{profileId}/groups")
    @FormUrlEncoded
    suspend fun createRuleFolder(
        @Path("profileId") profileId: String,
        @Field("group") name: String
    ): Response<ApiResponse<Any>>

    // Filters
    @GET("profiles/{profileId}/filters")
    suspend fun listFilters(
        @Path("profileId") profileId: String
    ): Response<ApiResponse<FiltersBody>>

    @GET("profiles/{profileId}/filters/external")
    suspend fun listExternalFilters(
        @Path("profileId") profileId: String
    ): Response<ApiResponse<FiltersBody>>

    @PUT("profiles/{profileId}/filters/{filterPk}")
    @FormUrlEncoded
    suspend fun modifyFilter(
        @Path("profileId") profileId: String,
        @Path("filterPk") filterPk: String,
        @Field("status") status: Int
    ): Response<ApiResponse<Any>>

    // Analytics
    @GET("analytics/endpoints")
    suspend fun listAnalyticsEndpoints(): Response<ApiResponse<AnalyticsEndpointsBody>>
}
