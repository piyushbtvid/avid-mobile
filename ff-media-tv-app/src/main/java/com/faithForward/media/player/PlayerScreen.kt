package com.faithForward.media.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.util.Resource

data class PlayerDto(
    val videoPlayerDtoList: List<VideoPlayerDto>,
)

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel,
) {

    val playerDtoResource by playerViewModel.videoPlayerDto.collectAsStateWithLifecycle()

    if (playerDtoResource is Resource.Unspecified || playerDtoResource is Resource.Error || playerDtoResource is Resource.Loading) return

    val playerDtoItems = playerDtoResource.data ?: return


    Box(
        modifier = modifier.fillMaxSize()
    ) {

        VideoPlayer(
            videoPlayerItem = playerDtoItems.videoPlayerDtoList,
            initialIndex = 0,
            playerViewModel = playerViewModel,
            onVideoEnd = {

            },
        )

    }

}