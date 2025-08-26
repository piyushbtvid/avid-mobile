package com.faithForward.network.dto.subscription

import com.faithForward.network.dto.login.User
import com.google.gson.annotations.SerializedName

data class SubscriptionResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: User,
)
