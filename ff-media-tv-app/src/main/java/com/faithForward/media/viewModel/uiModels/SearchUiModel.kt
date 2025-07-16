package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.sections.search.SearchContentDto
import com.faithForward.media.ui.sections.search.SearchUiScreenDto
import com.faithForward.media.ui.sections.search.item.SearchItemDto
import com.faithForward.media.util.formatDurationInReadableFormat
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.search.SearchResponse
import com.faithForward.util.Resource

// UI State for Search Screen
data class SearchUiState(
    val query: String = "",
    val searchResults: Resource<SearchContentDto> = Resource.Unspecified(),
)

data class SearchScreenUiState(
    val result: SearchUiScreenDto? = SearchUiScreenDto(searchItemDtoList = emptyList()),
    val recentSearch: List<String>? = emptyList(),
    val isLoading: Boolean = false,
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


fun SearchResponse.toSearchUiDto(): SearchUiScreenDto {
    val searchItemDtoList = data.map {
        it.toSearchItemDto()
    }

    return SearchUiScreenDto(searchItemDtoList = searchItemDtoList)
}

fun ContentItem.toSearchItemDto(): SearchItemDto {
    return SearchItemDto(
        itemId = id.toString(),
        title = channelName ?: name,
        contentType = content_type,
        contentSlug = slug,
        image = portrait,
        creatorName = if (!channelName.isNullOrEmpty()) name else "",
        duration = formatDurationInReadableFormat(duration),
        genre = genres?.mapNotNull { it.name }  // safely extract non-null names
            ?.joinToString(", "),
        imdb = rating
    )
}
