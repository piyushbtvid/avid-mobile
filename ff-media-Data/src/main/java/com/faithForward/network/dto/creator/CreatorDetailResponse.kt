package com.faithForward.network.dto.creator

data class CreatorResponse(
    val status: String,
    val message: String,
    val data: CreatorDetailData,
)

data class CreatorDetailData(
    val name: String,
    val email: String,
    val phone: String?,  // nullable
    val profile_img: String,
    val bio: String,
    val channel_name: String,
    val channel_category: String,
    val channel_banner: String,
    val channel_description: String?,  // nullable
    val channel_subscribers: String,
)
