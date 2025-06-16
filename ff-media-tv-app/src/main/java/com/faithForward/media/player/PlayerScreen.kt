package com.faithForward.media.player

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
//    LaunchedEffect(Unit) {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        (context as? Activity)?.window?.decorView?.let { view ->
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }

    DisposableEffect(Unit) {
        Log.d("FOCUS", "Current Focus: ${(context as? Activity)?.currentFocus}")
        onDispose { }
    }


    BackHandler {
        Log.e("CONTINUE", "onBack clicked of player with $isFromContinueWatching")
        onPlayerBackClick.invoke()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
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