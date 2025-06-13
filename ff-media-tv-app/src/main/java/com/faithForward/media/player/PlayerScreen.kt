package com.faithForward.media.player

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.util.Resource

data class PlayerDto(
    val videoPlayerDtoList: List<VideoPlayerDto>,
)

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    isFromContinueWatching: Boolean = false,
    playerViewModel: PlayerViewModel,
    onPlayerBackClick: () -> Unit,
) {
    val state by playerViewModel.state.collectAsState()


    BackHandler {
        Log.e("CONTINUE", "onBack clicked of player with $isFromContinueWatching")
        onPlayerBackClick.invoke()
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(color = Color.Black)) {
        when (val resource = state.videoPlayerDto) {
            is Resource.Unspecified, is Resource.Error -> {
                // Do nothing, just return empty Box
            }

            is Resource.Loading -> {
                Log.e("PLAYER", "player screen is loading called")
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center),
//                    color = focusedMainColor
//                )
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