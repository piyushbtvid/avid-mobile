package com.faithForward.network.dto.profile

data class CreateProfileResponse(
    val status: String,
    val message: String,
    val data: Profile,
)
