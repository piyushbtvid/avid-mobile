package com.faithForward.media.viewModel

import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.DetailDto
import com.faithForward.network.dto.detail.CardDetail


sealed interface DetailPageItem {
    data class CardWithRelated(
        val detailDto: DetailDto,
        val relatedList: List<PosterCardDto> = emptyList()
    ) : DetailPageItem
}

fun CardDetail.toDetailDto(): DetailDto {
    return DetailDto(
        imgSrc = data.landscape,
        title = data.name,
        description = data.description,
        releaseDate = data.dateUploaded,
        genre = data.genres?.mapNotNull { it.name }  // safely extract non-null names
            ?.joinToString(", "),
        duration = data.duration.toString(),
        imdbRating = data.rating,
    )
}


sealed class DetailScreenEvent {
    data class WatchNowFocusChanged(val isFocused: Boolean) : DetailScreenEvent()
    data class RelatedTextFocusChanged(val isFocused: Boolean) : DetailScreenEvent()
    data class RelatedItemFocused(val index: Int, val item: PosterCardDto?) : DetailScreenEvent()
}

sealed class DetailScreenState(
    val isWatchNowFocused: Boolean = false,
    val isRelatedTextFocused: Boolean = false,
    val focusedRelatedItemIndex: Int = -1,
    val focusedRelatedItem: PosterCardDto? = null
) {
    data object Idle : DetailScreenState()

    data class Active(
        val watchNowFocused: Boolean,
        val relatedTextFocused: Boolean,
        val relatedItemIndex: Int,
        val relatedItem: PosterCardDto?
    ) : DetailScreenState(
        watchNowFocused,
        relatedTextFocused,
        relatedItemIndex,
        relatedItem
    )
}