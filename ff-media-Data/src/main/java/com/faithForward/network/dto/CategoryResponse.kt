package com.faithForward.network.dto


data class CategoryResponse(
    val status: String,
    val data: List<Category>
)


data class Category(
    val id: Int,
    val name: String,
    val slug: String
)