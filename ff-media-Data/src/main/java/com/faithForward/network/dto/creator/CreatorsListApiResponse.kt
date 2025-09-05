package com.faithForward.network.dto.creator

import com.google.gson.annotations.SerializedName

data class CreatorsListApiResponse(
    @SerializedName("data")
    val data: List<UserData>,
)

data class UserData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("profile_img")
    val profileImg: String?, // Can be empty string or null

    @SerializedName("bio")
    val bio: String?, // Can be empty or null

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("channel_name")
    val channelName: String?,

    @SerializedName("channel_category")
    val channelCategory: String?,

    @SerializedName("channel_banner")
    val channelBanner: String?, // Explicitly null in example

    @SerializedName("channel_description")
    val channelDescription: String?,

    @SerializedName("channel_subscribers")
    val channelSubscribers: String, // "100" is a String in JSON, could be Int if API guarantees it
)