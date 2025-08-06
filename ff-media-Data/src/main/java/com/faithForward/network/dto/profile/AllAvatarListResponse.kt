package com.faithForward.network.dto.profile

data class AllAvatarListResponse(
    val status: String,
    val message: String,
    val data: List<AvatarItem>,
)

data class AvatarItem(
    val id: Int,
    val image_url: String,
)
