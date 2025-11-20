package com.faithForward.network.dto.myList

data class MyListResponse(
    val status: String,
    val message: String,
    val data: List<MyListContentCategory>,
)