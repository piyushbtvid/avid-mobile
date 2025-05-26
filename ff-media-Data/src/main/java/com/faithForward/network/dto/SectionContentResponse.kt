package com.faithForward.network.dto

data class SectionContentResponse(
    val status: String,
    val message: String,
    val data: List<ContentItem>
)
