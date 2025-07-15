package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.sections.search.SearchContentDto
import com.faithForward.media.ui.sections.search.item.SearchItemDto
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.search.SearchResponse
import com.faithForward.util.Resource

// UI State for Search Screen
data class SearchUiState(
    val query: String = "",
    val searchResults: Resource<SearchContentDto> = Resource.Unspecified()
)

// Search Events
sealed class SearchEvent {
    data class SubmitQuery(val query: String) : SearchEvent()
}


fun SearchResponse.toSearchContentDto(): SearchContentDto {
    val searchItemDtoList = data.map {
        it.toSearchItemDto()
    }

    return SearchContentDto(
        searchItemList = searchItemDtoList
    )
}


fun ContentItem.toSearchItemDto(): SearchItemDto {
    return SearchItemDto(
        itemId = id.toString(),
        title = name,
        contentType = content_type,
        contentSlug = slug,
        image = portrait,
    )
}