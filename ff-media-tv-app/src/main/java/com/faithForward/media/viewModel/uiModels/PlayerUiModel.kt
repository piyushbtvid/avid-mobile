package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.player.PlayerDto
import com.faithForward.media.player.VideoPlayerDto
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.player.relatedContent.RelatedContentItemDto
import com.faithForward.network.dto.ContentItem
import com.faithForward.util.Resource

fun PosterCardDto.toVideoPlayerDto(): VideoPlayerDto {
    return VideoPlayerDto(
        url = videoHlsUrl,
        itemId = id ?: "",
        itemSlug = slug,
        progress = progress ?: 0,
        title = title,
        contentType = contentType ?: "",
        description = description,
        seriesSlug = seriesSlug ?: "",
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        image = landScapeImg
    )
}

fun VideoPlayerDto.toPosterCardDto(): PosterCardDto {
    return PosterCardDto(
        videoHlsUrl = url,
        id = itemId ?: "",
        slug = itemSlug,
        progress = progress ?: 0,
        title = title ?: "",
        contentType = contentType ?: "",
        description = description,
        seriesSlug = seriesSlug ?: "",
        posterImageSrc = image,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        landScapeImg = image
    )
}

fun PosterCardDto.toRelatedItemDto(): RelatedContentItemDto {
    return RelatedContentItemDto(
        image = landScapeImg,
        id = id ?: "",
        slug = slug ?: "",
        title = title,
        url = videoHlsUrl,
        description = description,
        contentType = contentType,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    )
}

fun RelatedContentItemDto.toPosterCardDto(): PosterCardDto {

    return PosterCardDto(
        posterImageSrc = image,
        videoHlsUrl = url,
        id = id,
        slug = slug,
        title = title,
        description = description,
        contentType = contentType,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        landScapeImg = image
    )
}

fun ContentItem.toRelatedItemDto(): RelatedContentItemDto {
    //video Url coming from detail api while converting related list
    return RelatedContentItemDto(
        image = landscape ?: portrait ?: "",
        id = id?.toString() ?: "",
        slug = slug ?: "",
        title = name ?: "",
        url = video_link,
        description = description ?: "",
        contentType = content_type,
    )
}


data class PlayerState(
    val currentPosition: Long = 0L,
    val duration: Long = 1L,
    val videoPlayerDto: Resource<PlayerDto> = Resource.Unspecified(),
    val playerRelatedContentRowDto: PlayerRelatedContentRowDto? = null,
    val isRelatedVisible: Boolean = false,
    val isNextEpisodeDialogVisible: Boolean = false,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val isPlayerBuffering: Boolean = false,
    val hasVideoEnded: Boolean = false,
    val isEpisodePlaying: Boolean = false,
    val videoPlayingIndex: Int? = 0,
    val currentTitle: String? = null,
)

data class SharedPlayerViewModelState(
    val isControlsVisible: Boolean = true,
)

sealed class PlayerEvent {
    data class UpdateDuration(val value: Long) : PlayerEvent()
    data class UpdateCurrentPosition(val value: Long) : PlayerEvent()
    data object OnContinueWatchingUpdate : PlayerEvent()
    data object ShowNextEpisodeDialog : PlayerEvent()
    data object HideNextEpisodeDialog : PlayerEvent()
    data object ShowRelated : PlayerEvent()
    data object HideRelated : PlayerEvent()
    data class UpdateIsEpisodePlayingOrNot(val isEpisode: Boolean) : PlayerEvent()
    data class UpdateIsPlaying(val isPlaying: Boolean) : PlayerEvent()
    data class UpdateVideoPlayingIndex(val value: Int) : PlayerEvent()
    data class UpdateOrLoadPlayerData(
        val itemList: List<PosterCardDto>,
        val index: Int? = null,
        val isFromContinueWatching: Boolean = false,
    ) :
        PlayerEvent()

    data class UpdatePlayerBuffering(val isBuffering: Boolean) : PlayerEvent()
    data class UpdateVideoEndedState(val isEnded: Boolean) : PlayerEvent()
    data class UpdateTitleText(val text: String) : PlayerEvent()
    data class SaveToContinueWatching(
        val itemIndex: Int?,
        val progressSeconds: String,
        val videoDuration: Long,
        val shouldNavigateFromContinueWatching: Boolean = true,
    ) : PlayerEvent()
}


sealed class SharedPlayerEvent {
    data object ShowControls : SharedPlayerEvent()
    data object HideControls : SharedPlayerEvent()
}


enum class PlayerPlayingState {
    PLAYING, PAUSED, REWINDING, FORWARDING, MUTE_UN_MUTE, IDLE
}

