package com.faithForward.network.dto.myList

import com.faithForward.network.dto.ContentItem

data class MyListContentCategory(
    val title: String,
    val id: String,
    val content: List<ContentItem>
)
