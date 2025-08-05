package com.faithForward.network.dto.profile

data class AllProfileResponse(
    val status: String,
    val message: String,
    val data: List<Profile>,
)

data class Profile(
    val id: Int,
    val name: String,
    val avatar_id: Int,
    val avatar_img: String,
    val language: String,
    val preferences: Any?,  // or you can use a specific data class if `preferences` is expected to be a structured object
    val is_default: Boolean,
)
