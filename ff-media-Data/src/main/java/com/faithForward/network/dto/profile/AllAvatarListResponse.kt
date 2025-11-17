package com.faithForward.network.dto.profile

import com.google.gson.annotations.SerializedName

data class AllAvatarListResponse(
    val status: String?,
    val message: String?,
    @SerializedName("data") private val data: List<AvatarItem>? = null,
    @SerializedName("response_data") private val responseData: List<AvatarItem>? = null,
) {
    val avatars: List<AvatarItem>
        get() = data ?: responseData ?: emptyList()
}

data class AvatarItem(
    val id: Int,
    val image_url: String,
)
