package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.player.VideoPlayerDto

fun PosterCardDto.toVideoPlayerDto(): VideoPlayerDto {
    return VideoPlayerDto(
        url = videoHlsUrl,
        itemId = id ?: "",

        )
}