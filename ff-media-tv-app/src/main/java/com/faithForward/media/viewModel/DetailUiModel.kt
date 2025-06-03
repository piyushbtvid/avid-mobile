package com.faithForward.media.viewModel

import androidx.compose.ui.graphics.Color
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.DetailDto
import com.faithForward.media.detail.SeasonDto
import com.faithForward.media.detail.SeasonsNumberDto
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.network.dto.detail.CardDetail
import com.faithForward.network.dto.series.Episode
import com.faithForward.network.dto.series.Season


sealed interface DetailPageItem {
    data class Card(
        val detailDto: DetailDto
    ) : DetailPageItem
}

sealed interface RelatedContentData {
    data class RelatedMovies(val movies: List<PosterCardDto>) : RelatedContentData
    data class SeriesSeasons(
        val seasonNumberList: List<SeasonsNumberDto>,
        val selectedSeasonEpisodes: List<PosterCardDto>,
        val allSeasons: List<SeasonDto>
    ) : RelatedContentData
    data object None : RelatedContentData
}

data class SeasonData(
    val seasonNumberList: List<SeasonsNumberDto>,
    val selectedSeasonEpisodes: List<PosterCardDto>,
    val allSeasons: List<SeasonDto>
)

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

fun Season.toSeasonDto(): SeasonDto {
    return SeasonDto(episodesContentDto = episodes.map {
        it.toPosterDto()
    })
}

fun Season.toSeasonNumberDto(): SeasonsNumberDto {
    return SeasonsNumberDto(
        seasonNumber = season_number
    )
}

fun Episode.toPosterDto(): PosterCardDto {
    return PosterCardDto(id = id,
        posterImageSrc = landscape,
        title = name,
        description = description,
        genre = genres.mapNotNull { it.name }  // safely extract non-null names
            .joinToString(", "),
        duration = duration.toString(),
        imdbRating = rating,
        releaseDate = dateUploaded)
}


// Data class for UI-specific states
data class UiState(
    val targetHeight: Int = 280,
    val isContentVisible: Boolean = true
)

// Sealed class for UI events
sealed interface DetailScreenEvent {
    data class LoadCardDetail(val id: String, val relatedList: List<PosterCardDto>) : DetailScreenEvent
    data class RelatedRowFocusChanged(val hasFocus: Boolean) : DetailScreenEvent
    data object RelatedRowUpClick : DetailScreenEvent
    data class SeasonSelected(val seasonNumber: Int) : DetailScreenEvent
}