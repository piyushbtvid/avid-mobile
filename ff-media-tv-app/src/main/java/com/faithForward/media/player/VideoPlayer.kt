package com.faithForward.media.player

import android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRow
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.player.relatedContent.RelatedContentItemDto
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

data class VideoPlayerDto(
    val url: String?,
    val itemSlug: String?,
    val itemId: String,
    val progress: Long = 0,
)


@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoPlayerItem: List<VideoPlayerDto>,
    playerRelatedContentRowDto: PlayerRelatedContentRowDto,
    initialIndex: Int,
    playerViewModel: PlayerViewModel,
    onItemClick: (RelatedContentItemDto) -> Unit,
    onVideoEnd: () -> Unit,
) {
    val playerState = playerViewModel.playerState
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by playerViewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var currentPosition by remember { mutableIntStateOf(initialIndex) }
    var currentPlayingVideoPosition by remember { mutableStateOf(0) }
    var currentVideoDuration by remember { mutableStateOf(0) }

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = false
            videoScalingMode = VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_OFF

            addListener(object : Player.Listener {
                private var lastUpdateTime = 0L

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int,
                ) {
                    currentPlayingVideoPosition = newPosition.positionMs.toInt()
                    if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                        val currentItemIndex = currentMediaItemIndex
                        val time = System.currentTimeMillis()
                        val watchTime = oldPosition.positionMs.toString()
                        val prevTime = oldPosition.positionMs.toInt()
                        val newTime = newPosition.positionMs.toInt()
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(true))
                        }

                        Player.STATE_READY -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                            playWhenReady = true
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastUpdateTime >= 10000) {
                                val currentItemIndex = currentMediaItemIndex
                                lastUpdateTime = currentTime
                            }
                        }

                        Player.STATE_ENDED -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                            playerViewModel.handleEvent(
                                PlayerEvent.UpdateVideoEndedState(true)
                            )
                            onVideoEnd()
                        }

                        else -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                        }
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    playerViewModel.handleEvent(
                        PlayerEvent.UpdateVideoEndedState(false)
                    )
                    currentPosition = currentMediaItemIndex
                    currentVideoDuration = duration.toInt()
                    if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                        val previousIndex = currentMediaItemIndex - 1
                    }
                    playerViewModel.handleEvent(PlayerEvent.ShowControls)
                }
            })
        }
    }

    LaunchedEffect(playerState) {
        androidx.media3.common.util.Log.e(
            "EXOPLAY", "launchEffect in player is runned with $playerState"
        )
        when (playerState) {
            PlayerPlayingState.PLAYING -> {
                androidx.media3.common.util.Log.e("EXOPLAY", "playState in player is playing")
                exoPlayer.play()
                playerViewModel.handleEvent(PlayerEvent.ShowControls)
                // Start playing video
            }

            PlayerPlayingState.PAUSED -> {
                androidx.media3.common.util.Log.e("EXOPLAY", "playState in player is paused")
                exoPlayer.pause()
                playerViewModel.handleEvent(PlayerEvent.ShowControls)
                // Pause video
            }

            PlayerPlayingState.REWINDING -> {
                androidx.media3.common.util.Log.e("EXOPLAY", "playState in player is rewind")
                exoPlayer.seekBack()
                playerViewModel.handleEvent(PlayerEvent.ShowControls)
                // Rewind video
            }

            PlayerPlayingState.FORWARDING -> {
                androidx.media3.common.util.Log.e("EXOPLAY", "playState in player is forwarding")
                exoPlayer.seekForward()
                playerViewModel.handleEvent(PlayerEvent.ShowControls)
                // Fast forward video
            }

            PlayerPlayingState.IDLE -> {
                // Do nothing
                androidx.media3.common.util.Log.e("EXOPLAY", "playState in player is IDLE")
            }
        }
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerViewModel.handleEvent(PlayerEvent.UpdateIsPlaying(isPlaying))
            if (isPlaying) {
                playerViewModel.handleEvent(PlayerEvent.ShowControls)
            }
        }
    })

    // Start auto-hide timer when composable is first composed and controls are visible
    LaunchedEffect(Unit) {
//        playerViewModel.handleEvent(PlayerEvent.HideRelated)
//        playerViewModel.handleEvent(PlayerEvent.ShowControls)
//        if (state.isControlsVisible) {
//            playerViewModel.handleEvent(PlayerEvent.ShowControls)
//        }
//
//        playerViewModel.interactionFlow.collectLatest {
//            Log.e("PLAYER_UI", "User interaction collected in DetailScreen")
//            // playerViewModel.handleEvent(PlayerEvent.ShowControls) // Reset the auto-play timer
//        }
    }

    // Set Playlist
    LaunchedEffect(videoPlayerItem) {
        val mediaItems = videoPlayerItem.map { item ->
            MediaItem.Builder().setUri(item.url).build()
        }

        exoPlayer.setMediaItems(mediaItems, initialIndex, 0L)
        exoPlayer.prepare()

        val seekTo = videoPlayerItem[exoPlayer.currentMediaItemIndex]
        if (seekTo.progress > 0) {
            exoPlayer.seekTo(seekTo.progress)
        }

        while (true) {
            playerViewModel.handleEvent(PlayerEvent.UpdateCurrentPosition(exoPlayer.currentPosition))
            playerViewModel.handleEvent(
                PlayerEvent.UpdateDuration(
                    exoPlayer.duration.coerceAtLeast(
                        1L
                    )
                )
            )
            currentPlayingVideoPosition = exoPlayer.currentPosition.toInt()
            delay(500)
        }
    }

    LaunchedEffect(state.isControlsVisible) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_STOP -> {
                    Log.e("CONTINUE_WATCHING", "on stop called of videoPlayer")
                    if (!state.hasVideoEnded) {
                        val item = videoPlayerItem[exoPlayer.currentMediaItemIndex]
                        if (item.itemSlug != null) {
                            playerViewModel.handleEvent(
                                PlayerEvent.SaveToContinueWatching(
                                    itemSlug = item.itemSlug,
                                    progressSeconds = exoPlayer.currentPosition.toString(), // Current progress in seconds
                                    videoDuration = exoPlayer.duration.toString()    // Total duration in seconds
                                )
                            )
                        }
                    }
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                    exoPlayer.prepare()
                    playerViewModel.handleEvent(PlayerEvent.ShowControls)
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                }
            }, modifier = Modifier.fillMaxSize()
        )

        // Loading Indicator
        if (state.isLoading || state.isPlayerBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Log.e("PLAYER", "Video player is loading or buffering")
                CircularProgressIndicator(
                    color = focusedMainColor
                )
            }
        }

        // Related content row with slide animation
        AnimatedVisibility(
            visible = state.isRelatedVisible, enter = slideInVertically(
                initialOffsetY = { it }, // Start from bottom
                animationSpec = tween(durationMillis = 500)
            ), exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out to bottom
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            PlayerRelatedContentRow(
                playerRelatedContentRowDto = playerRelatedContentRowDto,
                onUp = {
                    playerViewModel.handleEvent(PlayerEvent.HideRelated)
                    playerViewModel.handleEvent(PlayerEvent.ShowControls)
                    true
                },
                onItemClick = onItemClick

            )
        }

        // Controls with slide animation, hidden when related content is visible
        AnimatedVisibility(
            visible = state.isControlsVisible && !state.isRelatedVisible, enter = slideInVertically(
                initialOffsetY = { it }, // Start from bottom
                animationSpec = tween(durationMillis = 300)
            ), exit = slideOutVertically(
                targetOffsetY = { it }, // Slide out to bottom
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlayerControls(currentPosition = state.currentPosition,
                        duration = state.duration,
                        onSeekTo = { exoPlayer.seekTo(it) },
                        onPlayPause = {
                            Log.e(
                                "PLAYER_CONTROLERS",
                                "on pause called with ${state.isControlsVisible}"
                            )
                            exoPlayer.playWhenReady = !exoPlayer.isPlaying
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        onRewind = {
                            Log.e(
                                "PLAYER_CONTROLERS",
                                "on Rewind called with ${state.isControlsVisible}"
                            )
                            val rewindMillis = 15_000L
                            val newPosition =
                                (exoPlayer.currentPosition - rewindMillis).coerceAtLeast(0L)
                            exoPlayer.seekTo(newPosition)
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        onForward = {
                            Log.e(
                                "PLAYER_CONTROLERS",
                                "on forward called with ${state.isControlsVisible}"
                            )
                            exoPlayer.seekForward()
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        onPrev = {
                            exoPlayer.seekToPreviousMediaItem()
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        onNext = {
                            exoPlayer.seekToNextMediaItem()
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        inControllerUp = {
                            Log.e("PLAYER_UI", "IS controlerUp called in videoPlayer")
                            playerViewModel.handleEvent(PlayerEvent.ShowRelated)
                            // playerViewModel.handleEvent(PlayerEvent.HideControls)
                            true
                        },
                        isPlaying = state.isPlaying,
                        focusRequester = focusRequester,
                        onBackClick = {})
                }
            }
        }
    }
}