package com.faithForward.media.player

import android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import kotlinx.coroutines.delay

data class VideoPlayerDto(
    val url: String?,
    val itemId: String,
    val progress: Long = 0,
)

@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoPlayerItem: List<VideoPlayerDto>,
    initialIndex: Int,
    playerViewModel: PlayerViewModel,
    onVideoEnd: () -> Unit,
) {
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
                            playWhenReady = true
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastUpdateTime >= 10000) {
                                val currentItemIndex = currentMediaItemIndex
                                lastUpdateTime = currentTime
                            }
                        }

                        Player.STATE_ENDED -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                            onVideoEnd()
                        }

                        else -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                        }
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentPosition = currentMediaItemIndex
                    currentVideoDuration = duration.toInt()
                    if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                        val previousIndex = currentMediaItemIndex - 1
                    }
                }
            })
        }
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerViewModel.handleEvent(PlayerEvent.UpdateIsPlaying(isPlaying))
        }
    })

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
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                    exoPlayer.prepare()
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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Loading Indicator
        if (state.isLoading || state.isPlayerBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Log.e("PLAYER", "Video player is loading or buffering")
                CircularProgressIndicator(
                    color = focusedMainColor
                )
            }
        }

        // Controls
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = state.isControlsVisible,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlayerControls(
                        currentPosition = state.currentPosition,
                        duration = state.duration,
                        onSeekTo = { exoPlayer.seekTo(it) },
                        onPlayPause = {
                            exoPlayer.playWhenReady = !exoPlayer.isPlaying
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        onRewind = {
                            val rewindMillis = 15_000L
                            val newPosition =
                                (exoPlayer.currentPosition - rewindMillis).coerceAtLeast(0L)
                            exoPlayer.seekTo(newPosition)
                            playerViewModel.handleEvent(PlayerEvent.ShowControls)
                        },
                        onForward = {
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
                            playerViewModel.handleEvent(PlayerEvent.HideControls)
                            true
                        },
                        isPlaying = state.isPlaying,
                        focusRequester = focusRequester,
                        onBackClick = {}
                    )
                }
            }
        }
    }
}