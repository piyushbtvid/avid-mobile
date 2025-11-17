package com.faithForward.network.dto.profile

import com.google.gson.annotations.SerializedName

data class AllProfileResponse(
    val status: String?,
    val message: String?,
    @SerializedName("data") private val data: List<Profile>? = null,
    @SerializedName("response_data") private val responseData: List<Profile>? = null,
) {
    val profiles: List<Profile>
        get() = data ?: responseData ?: emptyList()
}

data class Profile(
    val id: Int,
    val name: String,
    val avatar_id: Int,
    val avatar_img: String,
    val language: String,
    val preferences: Any?,  // or you can use a specific data class if `preferences` is expected to be a structured object
    val is_default: Boolean,
)
