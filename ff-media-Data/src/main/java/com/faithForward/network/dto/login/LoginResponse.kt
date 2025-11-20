package com.faithForward.network.dto.login

import com.faithForward.network.dto.profile.Profile
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginData,
)

data class LoginData(
    @SerializedName("user") val user: User?,
    @SerializedName("token") val token: String?,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("token_type") val tokenType: String?,
    val expire_date: Long,
    val activation_status: String?,
)

data class User(
    val name: String?,
    val email: String?,
    @SerializedName("user_type") val user_type: String?,
    val role: String?,
    val plans: List<Plan>? = null,
    val profile: Profile? = null
)

data class Plan(
    val platform_name: String,
    val product_id: String,
    val starts_at: String,
    val expires_at: String
)


