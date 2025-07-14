package com.faithForward.media.player

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.player.relatedContent.RelatedContentItemDto
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.SharedPlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.SharedPlayerEvent
import com.faithForward.util.Resource

data class PlayerDto(
    val videoPlayerDtoList: List<VideoPlayerDto>,
    val playerRelatedContentRowDto: PlayerRelatedContentRowDto,
)

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    isFromContinueWatching: Boolean = false,
    playerViewModel: PlayerViewModel,
    sharedPlayerViewModel: SharedPlayerViewModel,
    onPlayerBackClick: () -> Unit,
    initialIndex: Int = 0,
    onVideoEnded: () -> Unit,
    onEpisodePlayNowClick: (List<VideoPlayerDto>, index: Int?) -> Unit,
    onRelatedItemClick: (RelatedContentItemDto?, List<RelatedContentItemDto>?, index: Int?) -> Unit,
) {
    val state by playerViewModel.state.collectAsState()

    var videoIndex by remember { mutableStateOf(initialIndex) }

    // Set videoIndex based on isFromContinueWatching
    LaunchedEffect(state.videoPlayingIndex, isFromContinueWatching) {
        Log.e("VIDEO_INDEX", "VIDEO INDEX IN PAYER IS $initialIndex and ${state.videoPlayingIndex}")
        if (isFromContinueWatching && state.videoPlayingIndex != null) {
            videoIndex = state.videoPlayingIndex ?: 0
        } else {
            videoIndex = initialIndex
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        (context as? Activity)?.window?.decorView?.let { view ->
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    DisposableEffect(Unit) {
        Log.d("FOCUS", "Current Focus: ${(context as? Activity)?.currentFocus}")
        onDispose { }
    }


    BackHandler {
        Log.e("CONTINUE", "onBack clicked of player with $isFromContinueWatching")
        playerViewModel.handleEvent(PlayerEvent.HideRelated)
        playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
        sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
        onPlayerBackClick.invoke()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        when (val resource = state.videoPlayerDto) {

            is Resource.Unspecified,
            is Resource.Error,
                -> {
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
                val relatedList = resource.data?.playerRelatedContentRowDto ?: return@Box
                VideoPlayer(videoPlayerItem = playerDtoItems,
                    initialIndex = videoIndex,
                    playerViewModel = playerViewModel,
                    sharedPlayerViewModel = sharedPlayerViewModel,
                    playerRelatedContentRowDto = relatedList,
                    onVideoEnd = {
                        onVideoEnded.invoke()
                        Log.e("VIDEO_ENDED", "ON VIDEO ENDED IS CALLED")
                    },
                    onEpisodePlayNowClick = { list, index ->
                        playerViewModel.handleEvent(PlayerEvent.HideRelated)
                        playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
                        sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        onEpisodePlayNowClick.invoke(list, index)
                    },
                    onRelatedItemClick = { item, list, index ->
                        playerViewModel.handleEvent(PlayerEvent.HideRelated)
                        playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
                        sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        onRelatedItemClick.invoke(item, list, index)
                    })
            }
        }
    }
}