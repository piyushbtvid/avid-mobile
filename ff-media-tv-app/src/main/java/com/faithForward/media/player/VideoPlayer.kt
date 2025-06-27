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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRow
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.player.relatedContent.RelatedContentItemDto
import com.faithForward.media.player.seriesNextUi.EpisodeNextUpDialog
import com.faithForward.media.player.seriesNextUi.EpisodeNextUpItemDto
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import kotlinx.coroutines.delay

data class VideoPlayerDto(
    val contentType : String,
    val title: String?,
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
    onRelatedItemClick: (RelatedContentItemDto?, List<RelatedContentItemDto>?, index: Int?) -> Unit,
    onVideoEnd: () -> Unit,
) {
    val playerState = playerViewModel.playerState
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerScreenState by playerViewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var currentPosition by remember { mutableIntStateOf(initialIndex) }
    var currentPlayingVideoPosition by remember { mutableStateOf(0) }
    var currentVideoDuration by remember { mutableStateOf(0) }
    var currentTitle by remember { mutableStateOf(videoPlayerItem.firstOrNull()?.title) }
    var showNextEpisodeDialog by remember { mutableStateOf(false) }

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
                            if (!playerScreenState.hasVideoEnded) {
                                val item = videoPlayerItem[currentMediaItemIndex]
                                if (item.itemSlug != null) {
                                    val safeProgressMillis = (duration - 60_000L)
                                    playerViewModel.handleEvent(
                                        PlayerEvent.SaveToContinueWatching(
                                            itemSlug = item.itemSlug,
                                            progressSeconds = safeProgressMillis.toString(),
                                            videoDuration = duration.toString()
                                        )
                                    )
                                }
                            }
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                            playerViewModel.handleEvent(PlayerEvent.UpdateVideoEndedState(true))
                            onVideoEnd()
                        }

                        else -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                        }
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    playerViewModel.handleEvent(PlayerEvent.UpdateVideoEndedState(false))
                    currentPosition = currentMediaItemIndex
                    currentVideoDuration = duration.toInt()
                    showNextEpisodeDialog = false // Reset dialog
                    playerViewModel.handleEvent(PlayerEvent.ShowControls)
                }
            })
        }
    }

    LaunchedEffect(playerState) {
        when (playerState) {
            PlayerPlayingState.PLAYING -> exoPlayer.play()
            PlayerPlayingState.PAUSED -> exoPlayer.pause()
            PlayerPlayingState.REWINDING -> exoPlayer.seekBack()
            PlayerPlayingState.FORWARDING -> exoPlayer.seekForward()
            PlayerPlayingState.IDLE -> {}
        }
        playerViewModel.handleEvent(PlayerEvent.ShowControls)
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerViewModel.handleEvent(PlayerEvent.UpdateIsPlaying(isPlaying))
            if (isPlaying) {
                playerViewModel.handleEvent(PlayerEvent.ShowControls)
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val index = exoPlayer.currentMediaItemIndex
            currentTitle = videoPlayerItem.getOrNull(index)?.title ?: ""
            playerViewModel.handleEvent(PlayerEvent.HideRelated)
            playerViewModel.handleEvent(PlayerEvent.ShowControls)
        }
    })

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
            val duration = exoPlayer.duration
            val currentPos = exoPlayer.currentPosition

            playerViewModel.handleEvent(PlayerEvent.UpdateCurrentPosition(currentPos))
            playerViewModel.handleEvent(PlayerEvent.UpdateDuration(duration.coerceAtLeast(1L)))
            currentPlayingVideoPosition = currentPos.toInt()

            val currentItem = videoPlayerItem.getOrNull(exoPlayer.currentMediaItemIndex)
            Log.e("CURRENT_EPISODE", "item slug is ${currentItem?.contentType}")
            if (
                currentItem?.contentType == "Episode" &&
                duration > 0 &&
                duration - currentPos <= 10_000 &&
                !showNextEpisodeDialog
            ) {
                showNextEpisodeDialog = true
            }

            delay(500)
        }
    }

    LaunchedEffect(playerScreenState.isControlsVisible) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_STOP -> {
                    if (!playerScreenState.hasVideoEnded) {
                        val currentIndex = exoPlayer.currentMediaItemIndex
                        val item = videoPlayerItem.getOrNull(currentIndex)
                        if (item?.itemSlug != null) {
                            playerViewModel.handleEvent(
                                PlayerEvent.SaveToContinueWatching(
                                    itemSlug = item.itemSlug,
                                    progressSeconds = exoPlayer.currentPosition.toString(),
                                    videoDuration = exoPlayer.duration.toString()
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

        // ➕ Animated Next Episode Dialog
        AnimatedVisibility(
            visible = showNextEpisodeDialog,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(500)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(500)
            ),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val currentIndex = exoPlayer.currentMediaItemIndex
            val nextItem = videoPlayerItem.getOrNull(currentIndex + 1)

            if (nextItem != null && nextItem.contentType == "Episode") {
                EpisodeNextUpDialog(
                    time = "",
                    episodeNextUpItemDto = EpisodeNextUpItemDto(
                        image = "",
                        title = nextItem.title ?: "",
                        description = "jgvvjjgjfcjfcc",
                        seasonEpisode = "Season Episode", // make sure this field exists in your DTO
                        episodeSlug = nextItem.itemSlug ?: "",
                        seriesSlug = ""
                    )
                )
            }
        }


        if (currentTitle != null) {
            TitleText(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp),
                text = currentTitle!!,
                textSize = 28,
                color = whiteMain,
                lineHeight = 28,
                fontWeight = FontWeight.Bold
            )
        }

        if (playerScreenState.isLoading || playerScreenState.isPlayerBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = focusedMainColor)
            }
        }

        AnimatedVisibility(
            visible = playerScreenState.isRelatedVisible,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
        ) {
            PlayerRelatedContentRow(
                playerRelatedContentRowDto = playerRelatedContentRowDto,
                onUp = {
                    playerViewModel.handleEvent(PlayerEvent.HideRelated)
                    playerViewModel.handleEvent(PlayerEvent.ShowControls)
                    true
                },
                onItemClick = { relatedItem, relatedItemList, index ->
                    if (!playerScreenState.hasVideoEnded) {
                        val item = videoPlayerItem[exoPlayer.currentMediaItemIndex]
                        if (item.itemSlug != null) {
                            playerViewModel.handleEvent(
                                PlayerEvent.SaveToContinueWatching(
                                    itemSlug = item.itemSlug,
                                    progressSeconds = exoPlayer.currentPosition.toString(),
                                    videoDuration = exoPlayer.duration.toString()
                                )
                            )
                        }
                    }
                    onRelatedItemClick(relatedItem, relatedItemList, index)
                }
            )
        }

        AnimatedVisibility(
            visible = playerScreenState.isControlsVisible && !playerScreenState.isRelatedVisible,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
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
                    PlayerControls(
                        currentPosition = playerScreenState.currentPosition,
                        duration = playerScreenState.duration,
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
                            playerViewModel.handleEvent(PlayerEvent.ShowRelated)
                            true
                        },
                        isPlaying = playerScreenState.isPlaying,
                        focusRequester = focusRequester,
                        onBackClick = {}
                    )
                }
            }
        }
    }
}
