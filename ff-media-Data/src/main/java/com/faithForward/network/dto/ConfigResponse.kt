package com.faithForward.network.dto

data class ConfigResponse(
    val status: String,
    val message: String,
    val data: ConfigData
)

data class ConfigData(
    val id: Int,
    val roku_pay_api_key: String?,
    val amazon_iap_secret: String?,
    val google_play_billing_key: String?,
    val enable_subscription: Boolean,
    val enable_ads: Boolean,
    val enable_qrlogin: Boolean,
    val enable_login: Boolean,
    val enable_home: Boolean,
    val enable_creator: Boolean,
    val enable_movie: Boolean,
    val enable_series: Boolean,
    val enable_live: Boolean,
    val created_at: String,
    val updated_at: String
)
