package com.faithForward.network.dto.subscription

import com.faithForward.network.dto.login.User
import com.google.gson.annotations.SerializedName

data class SubscriptionResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: SubscriptionData,
)

data class SubscriptionData(
    @SerializedName("user") val user: User?,
    @SerializedName("subscription") val subscription: Any?,
    @SerializedName("all_subscriptions") val allSubscriptions: List<Any>?,
    @SerializedName("subscription_count") val subscriptionCount: Int,
    @SerializedName("has_active_subscription") val hasActiveSubscription: Boolean,
)
