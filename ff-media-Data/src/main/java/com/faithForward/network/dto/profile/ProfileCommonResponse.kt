package com.faithForward.network.dto.profile

data class ProfileCommonResponse(
    val status: String,
    val message: String,
    val data: Profile,
)
