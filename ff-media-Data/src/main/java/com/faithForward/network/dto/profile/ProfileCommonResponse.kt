package com.faithForward.network.dto.profile

import com.google.gson.annotations.SerializedName

data class ProfileCommonResponse(
    val status: String?,
    val message: String?,
    @SerializedName("data") private val data: Profile? = null,
    @SerializedName("response_data") private val responseData: Profile? = null,
) {
    val profile: Profile?
        get() = data ?: responseData
}
