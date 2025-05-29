package com.faithForward.media.viewModel

import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.home.genre.GenreCardDto
import com.faithForward.media.home.genre.GenreGridDto
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.genre.GenreResponse


fun ContentItem.toGenreCardDto(): GenreCardDto {
    return GenreCardDto(
        genreId = id ?: "",
        name = creator?.name ?: "",
        image = portrait ?: "",
        description = name ?: "",
        views = "$views Views"
    )
}


fun GenreResponse.toGenreCardGridDto(): GenreGridDto {
    val list = data.map {
        it.toGenreCardDto()
    }
    return GenreGridDto(
        title = title ?: "",
        genreCardList = list
    )
}


fun GenreCardDto.toPosterCardDto(): PosterCardDto {
    return PosterCardDto(
        id = genreId,
        posterImageSrc = image
    )
}