package com.faithForward.network.dto.login

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("errors") val errors: Map<String, List<String>>?
)
