package com.faithForward.network.dto.login

import com.faithForward.network.dto.profile.Profile
import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SignupData,
)

data class SignupData(
    @SerializedName("user") val user: User?,
    @SerializedName("token") val token: String?,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("expire_date") val expireDate: Long?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("subscription") val subscription: Any?,
)

