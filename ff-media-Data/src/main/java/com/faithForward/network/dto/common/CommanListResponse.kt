package com.faithForward.network.dto.common

import com.faithForward.network.dto.ContentItem

data class CommanListResponse(
    val status: String,
    val message: String,
    val data: List<ContentItem>,
)