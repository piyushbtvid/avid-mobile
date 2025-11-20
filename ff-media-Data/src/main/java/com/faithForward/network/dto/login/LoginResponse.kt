package com.faithForward.network.dto.login

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
    val user_type: String?,
    val role: String?
)
