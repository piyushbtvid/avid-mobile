package com.faithForward.network.dto.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginData
)

data class LoginData(
    @SerializedName("user") val user: User,
    @SerializedName("token") val token: String,
    @SerializedName("token-type") val tokenType: String
)

data class User(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?, // nullable
    @SerializedName("profile_img") val profileImg: String?, // nullable
    @SerializedName("status") val status: String,
    @SerializedName("role") val role: String
)
