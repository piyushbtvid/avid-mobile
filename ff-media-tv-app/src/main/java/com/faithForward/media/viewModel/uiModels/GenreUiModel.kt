package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.sections.genre.GenreCardDto
import com.faithForward.media.ui.sections.genre.GenreGridDto
import com.faithForward.media.util.formatDuration
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.genre.GenreResponse


fun ContentItem.toGenreCardDto(): GenreCardDto {
    val viewsCount = views?.toIntOrNull()
    val durationSeconds = duration?.toLongOrNull()
    return GenreCardDto(
        genreId = id?.toString().takeIf { !it.isNullOrBlank() },
        name = creator?.name.takeIf { !it.isNullOrBlank() },
        image = portrait.takeIf { !it.isNullOrBlank() },
        description = name.takeIf { !it.isNullOrBlank() },
        views = viewsCount?.takeIf { it > 0 }?.let { "$it Views" },
        genre = genres?.mapNotNull { it.name }?.filter { it.isNotBlank() }?.joinToString(", ")
            .takeIf { !it.isNullOrBlank() },
        seasons = seasons?.takeIf { it.isNotEmpty() }?.size,
        duration = durationSeconds?.let { formatDuration(it) } ?: "",
        imdbRating = rating.takeIf { !it.isNullOrBlank() },
        releaseDate = dateUploaded.takeIf { !it.isNullOrBlank() },
        videoUrl = videoLink.takeIf { !it.isNullOrBlank() },
        slug = slug.takeIf { !it.isNullOrBlank() },
        contentType = contentType
    )
}


fun GenreResponse.toGenreCardGridDto(): GenreGridDto {
    val list = data.orEmpty().map {
        it.toGenreCardDto()
    }
    return GenreGridDto(
        title = title ?: "",
        genreCardList = list
    )
}


fun GenreCardDto.toPosterCardDto(): PosterCardDto {
    return PosterCardDto(
        id = genreId?.takeIf { it.isNotBlank() },
        posterImageSrc = image?.takeIf { it.isNotBlank() } ?: "",
        title = name?.takeIf { it.isNotBlank() } ?: "",
        description = description?.takeIf { it.isNotBlank() } ?: "",
        genre = genre?.takeIf { it.isNotBlank() },
        seasons = seasons?.takeIf { it > 0 },
        duration = duration?.takeIf { it.isNotBlank() },
        imdbRating = imdbRating?.takeIf { it.isNotBlank() },
        releaseDate = releaseDate?.takeIf { it.isNotBlank() },
        videoHlsUrl = videoUrl?.takeIf { it.isNotBlank() },
        slug = slug?.takeIf { it.isNotBlank() },
        landScapeImg = image?.takeIf { it.isNotBlank() } ?: ""
    )
}
