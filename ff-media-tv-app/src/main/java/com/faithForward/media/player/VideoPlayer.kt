package com.faithForward.media.player

import android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.faithForward.media.viewModel.PlayerViewModel
import kotlinx.coroutines.delay

data class VideoPlayerDto(
    val url: String?,
    val itemId: String,
    val progress: Long = 0,
)

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoPlayerItem: List<VideoPlayerDto>,
    initialIndex: Int,
    playerViewModel: PlayerViewModel,
    onVideoEnd: () -> Unit,
) {
    // val playerState = playerViewModel.playerState
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    //  val isLoading = exoPlayerViewModel.isLoading
    // var hasVideoEnded = exoPlayerViewModel.hasVideoEnded
    val currentPosition = playerViewModel.currentPosition
    val duration = playerViewModel.duration
    val is_Playing = playerViewModel.is_Playing
    val isControlsVisible by playerViewModel.isControlsVisible.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var current_Position by remember { mutableIntStateOf(initialIndex) }
    var currentPlayingVideoPosition by remember { mutableStateOf(0) }
    var currentVideoDuration by remember { mutableStateOf(0) }

    // val addType = exoPlayerViewModel.addType


//    android.util.Log.e("PLAYER", "exoPlayerCompose is opended")
//
//    BackHandler {
//        Log.e("PLAYER", "on back press is called in exoPlayerCompose")
//        onBackClick.invoke()
//    }


    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = false // Start with playback paused
            videoScalingMode = VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_OFF // No looping (optional)

            addListener(object : Player.Listener {

                private var lastUpdateTime = 0L

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int,
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
                        Player.STATE_READY -> {
                            // exoPlayerViewModel.handleLoadingState(false)
                            playWhenReady = true
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastUpdateTime >= 10000) { // Update every 10 seconds
                                val currentItemIndex = currentMediaItemIndex
                                lastUpdateTime = currentTime
                            }
                        }

                        Player.STATE_ENDED -> {
                            //  hasVideoEnded = true
                            val currentVideoIndex = currentMediaItemIndex
                            onVideoEnd()
                        }
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    // hasVideoEnded = false
                    current_Position = currentMediaItemIndex
                    currentVideoDuration = duration.toInt()
                    if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                        val previousIndex = currentMediaItemIndex - 1
                    }
                }
            })
        }
    }


    exoPlayer.addListener(object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            playerViewModel.handleIsPlaying(isPlaying)
        }
    })


//    LaunchedEffect(playerState) {
//        Log.e("EXOPLAY", "launchEffect in player is runned with $playerState")
//        when (playerState) {
//            PlayerViewModel.PlayerState.PLAYING -> {
//                Log.e("EXOPLAY", "playState in player is playing")
//                exoPlayer.play()
//                // Start playing video
//            }
//
//            PlayerViewModel.PlayerState.PAUSED -> {
//                Log.e("EXOPLAY", "playState in player is paused")
//                exoPlayer.pause()
//                // Pause video
//            }
//
//            PlayerViewModel.PlayerState.REWINDING -> {
//                Log.e("EXOPLAY", "playState in player is rewind")
//                exoPlayer.seekBack()
//                // Rewind video
//            }
//
//            PlayerViewModel.PlayerState.FORWARDING -> {
//                Log.e("EXOPLAY", "playState in player is forwarding")
//                exoPlayer.seekForward()
//                // Fast forward video
//            }
//
//            PlayerViewModel.PlayerState.IDLE -> {
//                // Do nothing
//                Log.e("EXOPLAY", "playState in player is IDLE")
//            }
//
//        }
//    }

    // Set Playlist (LaunchedEffect to update when URLs change)
    LaunchedEffect(videoPlayerItem) {

        // Convert all URLs to MediaItems
        val mediaItems = videoPlayerItem.map { item ->
            MediaItem.Builder().setUri(item.url).apply {
            }.build()
        }

        exoPlayer.setMediaItems(mediaItems, initialIndex, 0L)
        exoPlayer.prepare()


        val seekTo = videoPlayerItem[exoPlayer.currentMediaItemIndex]

        if (seekTo.progress > 0) {
            exoPlayer.seekTo(seekTo.progress)
        }

        while (true) {
            playerViewModel.handleCurrentPosition(exoPlayer.currentPosition)
            playerViewModel.handleDuration(exoPlayer.duration.coerceAtLeast(1L))
            currentPlayingVideoPosition = exoPlayer.currentPosition.toInt()
            delay(500) // Update every 500ms
        }
    }

    LaunchedEffect(isControlsVisible) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {

        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    //exoPlayerViewModel.handleIsVideoPlaying(false)
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_STOP -> {
                    //    Log.e("EXOPLAYER", "onStop is called in exoPlayerCompose")
                    // exoPlayerViewModel.handleIsVideoPlaying(false)
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    // exoPlayerViewModel.handleIsVideoPlaying(true)
                    // Log.e("EXOPLAYER", "onResume is called in exoPlayerCompose")
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

        // Wrap AnimatedVisibility in a Box with Bottom Alignment
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter) // Ensures controls stay at the bottom
            , contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(visible = isControlsVisible,
                enter = fadeIn() + slideInVertically { it },  // Slide up from bottom
                exit = fadeOut() + slideOutVertically { it }  // Slide down to bottom
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    PlayerControls(currentPosition = currentPosition,
                        duration = duration,
                        onSeekTo = { exoPlayer.seekTo(it) },
                        onPlayPause = {
                            exoPlayer.playWhenReady = !exoPlayer.isPlaying
                            //  playerViewModel.userInteraction()
                        },
                        onRewind = {
                            val rewindMillis = 15_000L // 15 seconds in milliseconds
                            val newPosition =
                                (exoPlayer.currentPosition - rewindMillis).coerceAtLeast(0L)
                            exoPlayer.seekTo(newPosition)
                            //   playerViewModel.userInteraction()
                        },
                        onForward = {
                            exoPlayer.seekForward();
                            //  playerViewModel.userInteraction()
                        },
                        onPrev = {
                            exoPlayer.seekToPreviousMediaItem();
                            //playerViewModel.userInteraction()
                        },
                        onNext = {
                            exoPlayer.seekToNextMediaItem();
//                            playerViewModel.userInteraction()
                        },
                        inControllerUp = {
                            // playerViewModel.hideControls()
                            true
                        },
                        isPlaying = is_Playing,
                        focusRequester = focusRequester,
                        onBackClick = {
                            //  onBackClick.invoke()
                        })
                }
            }
        }

        // Show Loader
//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f)),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator(
//                    color = ydhColor, modifier = Modifier.size(40.dp)
//                )
//            }
//        }
    }

}