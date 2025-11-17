package com.faithForward.network.dto.genre

import com.faithForward.network.dto.ContentItem

data class GenreResponse(
    val status: String?,
    val message: String?,
    var title: String? = null,
    var id: Int? = null,
    val data: List<ContentItem>?
)