package com.faithForward.media.player

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.LoaderScreen
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.theme.focusedMainColor
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
    val state by playerViewModel.state.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        when (val resource = state.videoPlayerDto) {
            is Resource.Unspecified, is Resource.Error -> {
                // Do nothing, just return empty Box
            }

            is Resource.Loading -> {
                Log.e("PLAYER", "player screen is loading called")
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = focusedMainColor
                )
            }

            is Resource.Success -> {
                val playerDtoItems = resource.data?.videoPlayerDtoList ?: return@Box
                VideoPlayer(
                    videoPlayerItem = playerDtoItems,
                    initialIndex = 0,
                    playerViewModel = playerViewModel,
                    onVideoEnd = {}
                )
            }
        }
    }
}