package com.faithForward.network.dto.search

import com.faithForward.network.dto.ContentItem

data class SearchResponse(
    val status: String,
    val message: String,
    val data: List<ContentItem>
)