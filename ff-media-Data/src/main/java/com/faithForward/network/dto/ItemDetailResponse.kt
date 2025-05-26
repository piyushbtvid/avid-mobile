package com.faithForward.network.dto

data class ItemDetailResponse(
    var title: String? = null,
    var id: String? = null,
    var status: String? = null,
    var message: String? = null,
    val data: List<ContentItem>
)