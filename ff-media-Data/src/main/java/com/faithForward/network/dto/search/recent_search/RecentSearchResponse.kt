package com.faithForward.network.dto.search.recent_search

data class RecentSearchResponse(
    val status: String,
    val message: String,
    val data: List<RecentSearchItem>
)

data class RecentSearchItem(
    val term: String,
    val timestamp: Long
)