package com.faithForward.network.dto.request

data class CreateProfileRequest(
    val name: String,
    val avatar: Int,
    val language: String,
    val preferences: String,
)
