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
        contentType = contentType ?: ""
    )
}

fun PosterCardDto.toRelatedItemDto(): RelatedContentItemDto {
    return RelatedContentItemDto(
        image = posterImageSrc,
        id = id ?: "",
        slug = slug ?: "",
        title = title,
        url = videoHlsUrl,
        description = description,
        contentType = contentType,
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
    )
}

fun ContentItem.toRelatedItemDto(): RelatedContentItemDto {
    //video Url coming from detail api while converting related list
    return RelatedContentItemDto(
        image = portrait ?: landscape ?: "",
        id = id?.toString() ?: "",
        slug = slug ?: "",
        title = name ?: "",
        url = video_link,
        description = description ?: "",
        contentType = content_type
    )
}


data class PlayerState(
    val currentPosition: Long = 0L,
    val duration: Long = 1L,
    val videoPlayerDto: Resource<PlayerDto> = Resource.Unspecified(),
    val playerRelatedContentRowDto: PlayerRelatedContentRowDto? = null,
    val isControlsVisible: Boolean = true,
    val isRelatedVisible: Boolean = false,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val isPlayerBuffering: Boolean = false,
    val hasVideoEnded: Boolean = false,
    val videoPlayingIndex: Int? = 0,
)

sealed class PlayerEvent {
    data class UpdateDuration(val value: Long) : PlayerEvent()
    data class UpdateCurrentPosition(val value: Long) : PlayerEvent()
    data object ShowControls : PlayerEvent()
    data object HideControls : PlayerEvent()
    data object ShowRelated : PlayerEvent()
    data object HideRelated : PlayerEvent()
    data class UpdateIsPlaying(val isPlaying: Boolean) : PlayerEvent()
    data class UpdateOrLoadPlayerData(
        val itemList: List<PosterCardDto>,
        val index: Int? = null,
        val isFromContinueWatching: Boolean = false,
    ) :
        PlayerEvent()

    data class UpdatePlayerBuffering(val isBuffering: Boolean) : PlayerEvent()
    data class UpdateVideoEndedState(val isEnded: Boolean) : PlayerEvent()
    data class SaveToContinueWatching(
        val itemSlug: String,
        val progressSeconds: String,
        val videoDuration: String,
    ) : PlayerEvent()
}


enum class PlayerPlayingState {
    PLAYING, PAUSED, REWINDING, FORWARDING, IDLE
}

