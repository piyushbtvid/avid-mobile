package com.faithForward.network.dto

data class GenreResponse(
    var title: String? = null,
    var id: String? = null,
    val data: List<ContentItem>
)