package com.faithForward.network.dto.request

data class ContinueWatchingRequest(
    val slug: String,
    val progress_seconds: String,
    val duration: String
)
