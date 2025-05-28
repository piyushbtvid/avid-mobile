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