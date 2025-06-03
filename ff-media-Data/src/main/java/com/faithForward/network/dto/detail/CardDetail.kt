package com.faithForward.network.dto.detail

import com.faithForward.network.dto.ContentItem

data class CardDetail(
    val status: String,

    val message: String,

    val data: ContentItem
)