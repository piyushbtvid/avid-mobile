package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.DetailDto
import com.faithForward.media.detail.SeasonDto
import com.faithForward.media.detail.SeasonsNumberDto
import com.faithForward.media.util.formatDuration
import com.faithForward.network.dto.detail.CardDetail
import com.faithForward.network.dto.series.Episode
import com.faithForward.network.dto.series.Season


sealed interface DetailPageItem {
    data class Card(
        val detailDto: DetailDto,
    ) : DetailPageItem
}

sealed interface RelatedContentData {
    data class RelatedMovies(val movies: List<PosterCardDto>) : RelatedContentData
    data class SeriesSeasons(
        val seasonNumberList: List<SeasonsNumberDto>,
        val firstSeasonEpisodes: List<PosterCardDto>,
        val selectedSeasonEpisodes: List<PosterCardDto>,
        val resumeSeasonEpisodes: List<PosterCardDto>,
        val allSeasons: List<SeasonDto>,
        val relatedSeries: List<PosterCardDto>,
        val resumeIndex: Int,
    ) : RelatedContentData

    data object None : RelatedContentData
}

data class SeasonData(
    val seasonNumberList: List<SeasonsNumberDto>,
    val selectedSeasonEpisodes: List<PosterCardDto>,
    val allSeasons: List<SeasonDto>,
)

fun CardDetail.toDetailDto(): DetailDto {

    //for series detail giving 1 seasons 1 episode url
    // and for movie giving its url

    val firstEpisodeVideoLink =
        data.seasons?.firstOrNull { it.episodes.isNotEmpty() }?.episodes?.firstOrNull()?.video_link

    val resolvedVideoLink = when {
        !firstEpisodeVideoLink.isNullOrEmpty() -> firstEpisodeVideoLink
        data.seasons.isNullOrEmpty() -> data.video_link
        else -> "" // seasons present but no valid episodes
    }

    val relatedList = data.relatedContent?.map {
        it.toPosterCardDto()
    }

    return DetailDto(id = data.id.toString(),
        imgSrc = data.landscape,
        landScape = data.landscape,
        title = data.name,
        description = data.description,
        releaseDate = data.dateUploaded,
        genre = data.genres?.mapNotNull { it.name }?.joinToString(", "),
        duration = data.duration?.let { formatDuration(it.toLong()) } ?: "",
        imdbRating = data.rating,
        videoLink = resolvedVideoLink,
        slug = data.slug,
        isFavourite = data.myList,
        isLiked = data.likeDislike == "like",
        isDisliked = data.likeDislike == "dislike",
        isSeries = data.content_type == "Series" || data.content_type == "Episode",
        contentType = data.content_type,
        progress = data.progressSeconds,
        relatedList = relatedList)
}


fun DetailDto.toPosterCardDto(): PosterCardDto {
    return PosterCardDto(
        imdbRating = imdbRating,
        id = id,
        posterImageSrc = imgSrc ?: "",
        title = title ?: "",
        description = description ?: "",
        genre = genre,
        seasons = seasons,
        duration = duration,
        releaseDate = releaseDate,
        videoHlsUrl = videoLink,
        slug = slug,
        contentType = contentType,
        progress = progress,
        relatedList = relatedList,
        landScapeImg = landScape ?: ""
    )
}

fun Season.toSeasonDto(): SeasonDto {
    return SeasonDto(
        episodesContentDto = episodes
//            .sortedBy { it.episode_number } // sort episodes in ascending order
            .map { currentEpisode ->
                currentEpisode.toPosterDto()
            },
        seasonNumber = season_number
    )
}


fun Season.toSeasonNumberDto(): SeasonsNumberDto {
    return SeasonsNumberDto(
        seasonNumber = season_number.toString()
    )
}

fun Episode.toPosterDto(): PosterCardDto {
    return PosterCardDto(id = id,
        posterImageSrc = portrait,
        title = name,
        description = description,
        genre = genres.mapNotNull { it.name }  // safely extract non-null names
            .joinToString(", "),
        duration = duration?.let { formatDuration(it.toLong()) } ?: "",
        imdbRating = rating,
        releaseDate = dateUploaded,
        episodeNumber = episode_number,
        seasonNumber = season_number,
        videoHlsUrl = video_link,
        slug = slug,
        contentType = content_type,
        landScapeImg = landscape
    )
}


// Data class for UI-specific states
data class UiState(
    val targetHeight: Int = 280,
    val isContentVisible: Boolean = true,
    val isResumeVisible: Boolean = false,
)

// Sealed class for UI events
sealed interface DetailScreenEvent {
    data class LoadCardDetail(val slug: String, val relatedList: List<PosterCardDto>) :
        DetailScreenEvent

    data class RelatedRowFocusChanged(val hasFocus: Boolean) : DetailScreenEvent
    data object RelatedRowUpClick : DetailScreenEvent
    data class SeasonSelected(val seasonNumber: String) : DetailScreenEvent
    data class ToggleFavorite(val slug: String) : DetailScreenEvent // New event
    data class ToggleLike(val slug: String) : DetailScreenEvent // New event
    data class ToggleDisLike(val slug: String) : DetailScreenEvent // New event
}

data class UiEvent(val message: String)