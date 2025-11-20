package com.faithForward.network.dto.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refresh_token") val refreshToken: String,
)

