package com.faithForward.network.dto.login

data class ActivationCodeResponse(
    val status: String,
    val message: String,
    val data: ActivationCodeData,
)

data class ActivationCodeData(
    val activation_status: String? = null,
    val expires_in: Long,
    val activation_code: String,
    val qr_img: String,
    val verification_url: String,
    val verification_url_with_code: String,
)
