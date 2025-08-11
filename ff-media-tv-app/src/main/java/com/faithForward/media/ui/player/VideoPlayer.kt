package com.faithForward.media.ui.player

import android.annotation.SuppressLint
import android.media.MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.player.relatedContent.PlayerRelatedContentRow
import com.faithForward.media.ui.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.ui.player.relatedContent.RelatedContentItemDto
import com.faithForward.media.ui.player.seriesNextUi.EpisodeNextUpDialog
import com.faithForward.media.ui.player.seriesNextUi.EpisodeNextUpItemDto
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.SharedPlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import com.faithForward.media.viewModel.uiModels.SharedPlayerEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class VideoPlayerDto(
    val contentType: String,
    val title: String?,
    val url: String?,
    val itemSlug: String?,
    val itemId: String,
    val progress: Long = 0,
    val image: String,
    val seriesSlug: String,
    val description: String,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null,
)


@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoPlayerItem: List<VideoPlayerDto>,
    playerRelatedContentRowDto: PlayerRelatedContentRowDto,
    initialIndex: Int,
    playerViewModel: PlayerViewModel,
    sharedPlayerViewModel: SharedPlayerViewModel,
    onRelatedItemClick: (RelatedContentItemDto?, List<RelatedContentItemDto>?, index: Int?) -> Unit,
    onEpisodePlayNowClick: (List<VideoPlayerDto>, index: Int?) -> Unit,
    onVideoEnd: () -> Unit,
) {
    val playerState = sharedPlayerViewModel.playerState
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerScreenState by playerViewModel.state.collectAsState()
    val sharedPlayerScreenState by sharedPlayerViewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var currentPosition by remember { mutableIntStateOf(initialIndex) }
    var currentPlayingVideoPosition by remember { mutableStateOf(0) }
    var currentVideoDuration by remember { mutableStateOf(0) }

    //temporary for error in live data
    var hasPlaybackError by remember { mutableStateOf(false) }


    val isVisible =
        sharedPlayerScreenState.isControlsVisible && !playerScreenState.isRelatedVisible && !playerScreenState.isNextEpisodeDialogVisible

    val scope = rememberCoroutineScope()
    val dialogThresholdMs = 20_000L

// Animate alpha for player controlers hide show
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = if (isVisible) 300 else 500),
        label = "controlsAlpha"
    )

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

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.e("ExoPlayer_error", "Playback error: ${error.message}")
                }

                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(true))
                        }

                        Player.STATE_READY -> {
                            Log.e(
                                "IS_CONTINUE_WATCHING_CLICK",
                                "player state end called and State Redy Also with duration $duration and current Pos $currentPosition"
                            )
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                            playWhenReady = true
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastUpdateTime >= 10000) {
                                val currentItemIndex = currentMediaItemIndex
                                lastUpdateTime = currentTime
                            }
                        }

                        Player.STATE_ENDED -> {

                            scope.launch {

                                val isValidEndState = duration != C.TIME_UNSET && duration > 0

                                Log.e(
                                    "CONTINUE_WATCHING",
                                    "player state end called and onVideoEnd Also with duration $duration and current Pos $currentPosition and isValidEndState is $isValidEndState"
                                )

                                if (isValidEndState) {
                                    val item = videoPlayerItem.getOrNull(currentMediaItemIndex)
                                    Log.e(
                                        "TEST_WATCH",
                                        "onVideo Ended called with isValidEndState as $isValidEndState"
                                    )
                                    Log.e(
                                        "TEST_WATCH",
                                        "onVideo Ended called with currentMediaItem Index Item is $item"
                                    )

                                    Log.e(
                                        "TEST_WATCH",
                                        "onVideo Ended called with vidPlayerItemList Size  is ${videoPlayerItem.size}  and List is $videoPlayerItem "
                                    )


                                    val safeProgressMillis = (duration - 60_000L).coerceAtLeast(0L)

                                    Log.e(
                                        "TEST_WATCH",
                                        "onVideo Ended called with safe progress as $safeProgressMillis and itemMedia Index as $currentMediaItemIndex"
                                    )


                                    playerViewModel.handleEvent(
                                        PlayerEvent.SaveToContinueWatching(
                                            itemIndex = currentMediaItemIndex,
                                            progressSeconds = (safeProgressMillis / 1000).toString(),
                                            videoDuration = duration
                                        )
                                    )
                                    playerViewModel.handleEvent(
                                        PlayerEvent.UpdatePlayerBuffering(
                                            false
                                        )
                                    )
                                    playerViewModel.handleEvent(
                                        PlayerEvent.UpdateVideoEndedState(
                                            true
                                        )
                                    )
                                }
                            }
                        }


                        else -> {
                            playerViewModel.handleEvent(PlayerEvent.UpdatePlayerBuffering(false))
                        }
                    }
                }

                @SuppressLint("SwitchIntDef")
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    Log.e(
                        "MEDIA_ITEM_TRANSITION",
                        "on media item transition called with ${playerScreenState.currentPosition} and $currentPosition and duartion ${playerScreenState.duration} and $duration  and reason $reason"
                    )

                    val previousIndex = currentMediaItemIndex - 1

                    when (reason) {
                        Player.MEDIA_ITEM_TRANSITION_REASON_AUTO -> {
                            Log.e("Transition", "Auto: video finished, next started")
                            if (previousIndex >= 0 && previousIndex < videoPlayerItem.size) {
                                val isValidEndState =
                                    playerScreenState.duration != C.TIME_UNSET && playerScreenState.duration > 0

                                if (isValidEndState) {
                                    PlayerEvent.SaveToContinueWatching(
                                        itemIndex = previousIndex,
                                        progressSeconds = (playerScreenState.currentPosition / 1000).toString(),
                                        videoDuration = playerScreenState.duration
                                    )
                                }

                            }
                        }

                        Player.MEDIA_ITEM_TRANSITION_REASON_SEEK -> {
                            Log.e("Transition", "User seeked to another item")
                            if (previousIndex >= 0 && previousIndex < videoPlayerItem.size) {
                                PlayerEvent.SaveToContinueWatching(
                                    itemIndex = previousIndex,
                                    progressSeconds = (playerScreenState.currentPosition / 1000).toString(),
                                    videoDuration = playerScreenState.duration
                                )
                            }
                        }
                    }
                    val currentTitle = videoPlayerItem[currentMediaItemIndex].title
                    playerViewModel.handleEvent(PlayerEvent.UpdateTitleText(currentTitle ?: ""))
                    playerViewModel.handleEvent(PlayerEvent.UpdateVideoEndedState(false))
                    currentPosition = currentMediaItemIndex
                    currentVideoDuration = duration.toInt()
                    playerViewModel.handleEvent(PlayerEvent.HideRelated)
                    playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
                    Log.e("SHOW_CONTROLES", "show controles in media trabsciation")
                    sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                }
            })
        }
    }

    BackHandler {
        val isValidEndState = exoPlayer.duration != C.TIME_UNSET && exoPlayer.duration > 0

        Log.e(
            "ON_CONTINUE_WATCHING",
            "on back called from video Player compose  with positon as ${exoPlayer.currentPosition}"
        )

        if (isValidEndState) {
            val item = videoPlayerItem.getOrNull(exoPlayer.currentMediaItemIndex)
            Log.e(
                "TEST_WATCH", "onVideo Ended called with isValidEndState as $isValidEndState"
            )
            Log.e(
                "TEST_WATCH", "onVideo Ended called with currentMediaItem Index Item is $item"
            )

            Log.e(
                "TEST_WATCH",
                "onVideo Ended called with vidPlayerItemList Size  is ${videoPlayerItem.size}  and List is $videoPlayerItem "
            )


//            val safeProgressMillis =
//                (exoPlayer.duration - 60_000L).coerceAtLeast(0L)

            playerViewModel.handleEvent(
                PlayerEvent.SaveToContinueWatching(
                    itemIndex = exoPlayer.currentMediaItemIndex,
                    progressSeconds = (exoPlayer.currentPosition / 1000).toString(),
                    videoDuration = exoPlayer.duration
                )
            )
        } else if (hasPlaybackError) {
            onVideoEnd()
        }
    }

    LaunchedEffect(playerState) {
        when (playerState) {
            PlayerPlayingState.PLAYING -> {
                if (!playerScreenState.isNextEpisodeDialogVisible) {
                    exoPlayer.play()
                }
            }

            PlayerPlayingState.PAUSED -> {
                if (!playerScreenState.isNextEpisodeDialogVisible) {
                    exoPlayer.pause()
                }
            }

            PlayerPlayingState.REWINDING -> {
                if (!playerScreenState.isNextEpisodeDialogVisible) {
                    exoPlayer.seekBack()
                }
            }

            PlayerPlayingState.FORWARDING -> {

                if (!playerScreenState.isNextEpisodeDialogVisible) {
                    exoPlayer.seekForward()
                }
            }

            PlayerPlayingState.IDLE -> {}
            PlayerPlayingState.MUTE_UN_MUTE -> {}
        }
        if (!playerScreenState.isNextEpisodeDialogVisible && !playerScreenState.isRelatedVisible) {
            Log.e("VIDEO_PLAYER_SHOW", "show controles in player state change")
            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
        }

    }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {
        }

        sharedPlayerViewModel.interactionFlow.collect {
            Log.e("ON_USER_INTERCATION", "om user instercation collected in videoPlayer ")
            if (playerScreenState.isRelatedVisible) {
                playerViewModel.handleEvent(PlayerEvent.StartRelatedDialogAutoHide)
            }
            if (!sharedPlayerScreenState.isControlsVisible && !playerScreenState.isRelatedVisible && !playerScreenState.isNextEpisodeDialogVisible) {
                //     sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                playerViewModel.handleEvent(PlayerEvent.HideRelated)
                playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
            }
        }
    }

    LaunchedEffect(Unit) {
        playerViewModel.continueWatchingUpdateFlow.collect {
            Log.e(
                "ON_CONTINUE_WATCHING",
                "on continue watching update called in player so navigating back"
            )
            //  playerViewModel.handleEvent(PlayerEvent.UpdateTitleText(""))
            exoPlayer.seekTo(0)
            exoPlayer.release()
            onVideoEnd()
        }
    }

    LaunchedEffect(!playerScreenState.isRelatedVisible) {
        if (!playerScreenState.isRelatedVisible) {
            Log.e("SHOW_CONTROLES", "show controles in player Screen State Launch effect")
            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
        }
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            playerViewModel.handleEvent(PlayerEvent.UpdateIsPlaying(isPlaying))
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.e("PLAYER_ERROR", "on player error with ${error.message}")
            hasPlaybackError = true
        }
    })

    LaunchedEffect(videoPlayerItem) {
        if (videoPlayerItem.isNotEmpty()) {
            val mediaItems = videoPlayerItem.map { item ->
                MediaItem.Builder().setUri(item.url).build()
            }

            val safeIndex = initialIndex.coerceIn(0, mediaItems.lastIndex)
            exoPlayer.setMediaItems(mediaItems, safeIndex, 0L)
            exoPlayer.prepare()

            val seekTo = videoPlayerItem[safeIndex]
            if (seekTo.progress > 0) {
                exoPlayer.seekTo(seekTo.progress * 1000L) // converting seconds to milli seconds
            }


            while (true) {
                val duration = exoPlayer.duration
                val currentPos = exoPlayer.currentPosition

                //updating duration and position
                playerViewModel.handleEvent(PlayerEvent.UpdateCurrentPosition(currentPos))
                playerViewModel.handleEvent(PlayerEvent.UpdateDuration(duration.coerceAtLeast(1L)))
                currentPlayingVideoPosition = currentPos.toInt()

                val currentItem = videoPlayerItem.getOrNull(exoPlayer.currentMediaItemIndex)
//                Log.e("CURRENT_MEDIA_INDEX", "current Item is ")
//                Log.e("CURRENT_EPISODE", "item slug is ${currentItem?.contentType}")
                //checking condition for showing episodeNextDialog if episode
                delay(500)
            }
        }
    }

    LaunchedEffect(exoPlayer.currentMediaItem) {
        val currentItem = videoPlayerItem.getOrNull(exoPlayer.currentMediaItemIndex)
        val currentTitle = currentItem?.title ?: ""
        Log.e(
            "Player", "Current MediaItem changed: $currentItem"
        )
        Log.e(
            "Player",
            "Current Media Item: ${currentItem?.title}, position: ${exoPlayer.currentPosition}, duration: ${exoPlayer.duration}"
        )

        // Update episode playing state
        playerViewModel.handleEvent(
            PlayerEvent.UpdateIsEpisodePlayingOrNot(currentItem?.contentType == "Episode")
        )

        // Track whether dialog has been shown to prevent repeated triggers
        var hasShownDialog = false

        while (isActive) {
            val duration = exoPlayer.duration
            val currentPos = exoPlayer.currentPosition

            // Skip if duration is unset or invalid
            if (duration == C.TIME_UNSET || duration <= 0 || currentPos < 0) {
                delay(1000) // Increased polling interval
                continue
            }

            // Check if within threshold and no user interaction
            if (!hasShownDialog && duration - currentPos <= dialogThresholdMs && !playerScreenState.isNextEpisodeDialogVisible && !playerScreenState.isRelatedVisible) {
                when (currentItem?.contentType) {
                    "Episode" -> {
                        Log.e(
                            "Player",
                            "Showing next episode dialog: ${currentItem.contentType}, duration: $duration, position: $currentPos"
                        )
                        playerViewModel.handleEvent(PlayerEvent.HideRelated)
                        sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                        playerViewModel.handleEvent(PlayerEvent.ShowNextEpisodeDialog)
                        hasShownDialog = true // Prevent re-triggering
                    }

                    "Movie" -> {
                        Log.e(
                            "Player",
                            "Showing related movie dialog: ${currentItem.contentType}, duration: $duration, position: $currentPos"
                        )
                        sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                        playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
                        playerViewModel.handleEvent(PlayerEvent.ShowRelated)
                        hasShownDialog = true // Prevent re-triggering
                    }
                }
            }

            // Reset dialog flag if video moves out of threshold (e.g., user seeks backward)
            if (hasShownDialog && duration - currentPos > dialogThresholdMs) {
                hasShownDialog = false
            }

            delay(1000) // Poll every 1 second instead of 300ms
        }
    }


    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_STOP -> {
                    Log.e("STOP_TRACK", "on Stop is called with ${playerScreenState.hasVideoEnded}")
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                    exoPlayer.prepare()
                    Log.e("SHOW_CONTROLES", "show controles on Resume")
                    sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.e("EXO_PLAYER_LIFE_CYCL", "on dispose called ")
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
                    keepScreenOn = true
                }
            }, modifier = Modifier.fillMaxSize()
        )

        //  Animated Next Episode Dialog
        AnimatedVisibility(
            visible = playerScreenState.isNextEpisodeDialogVisible, enter = slideInVertically(
                initialOffsetY = { it }, animationSpec = tween(500)
            ), exit = slideOutVertically(
                targetOffsetY = { it }, animationSpec = tween(500)
            ), modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            val currentIndex = exoPlayer.currentMediaItemIndex
            //next playing video
            val nextItem = videoPlayerItem.getOrNull(currentIndex + 1)
            //next episode in related List
            //val nextRelatedEpisode = playerRelatedContentRowDto.rowList.getOrNull(initialIndex + 1)
            Log.e("ITEM_SLUG", "next item slug is ${nextItem?.itemSlug}")
            if (nextItem != null && nextItem.contentType == "Episode") {
                EpisodeNextUpDialog(time = "", episodeNextUpItemDto = EpisodeNextUpItemDto(
                    image = nextItem.image,
                    title = nextItem.title ?: "",
                    description = nextItem.description,
                    seasonEpisode = "Season ${nextItem.seasonNumber}, Episode ${nextItem.episodeNumber}",
                    episodeSlug = nextItem.itemSlug ?: "",
                    seriesSlug = nextItem.seriesSlug
                ), onCancelClick = {
                    Log.e(
                        "VIDEO_END",
                        "onCancel click of Episode Dialog called and onVideoEnd also with ${playerScreenState.hasVideoEnded}"
                    )
                    scope.launch {
                        if (!playerScreenState.hasVideoEnded) {
                            val item = videoPlayerItem[exoPlayer.currentMediaItemIndex]
                            if (item.itemSlug != null) {
                                playerViewModel.handleEvent(
                                    PlayerEvent.SaveToContinueWatching(
                                        itemIndex = currentIndex,
                                        progressSeconds = (exoPlayer.duration / 1000).toString(),
                                        videoDuration = exoPlayer.duration
                                    )
                                )
                            }
                        }
                        //currentTitle = ""
                    }
                }, onPlayNowClick = {
                    Log.e("PLAY_CLICK", "onPlay click with current Index $currentIndex")
                    if (!playerScreenState.hasVideoEnded) {
                        val item = videoPlayerItem[exoPlayer.currentMediaItemIndex]
                        if (item.itemSlug != null) {
                            playerViewModel.handleEvent(
                                PlayerEvent.SaveToContinueWatching(
                                    itemIndex = currentIndex,
                                    progressSeconds = (exoPlayer.duration / 1000).toString(),
                                    videoDuration = exoPlayer.duration,
                                    shouldNavigateFromContinueWatching = false
                                )
                            )
                        }
                    }
                    exoPlayer.seekTo(0)
                    //currentTitle = ""
                    playerViewModel.handleEvent(PlayerEvent.UpdateTitleText(""))
                    Log.e(
                        "EPISODE_NEXT_UI",
                        "episode on PLayNowClick exoPlayer position is ${exoPlayer.currentPosition} and duration is ${exoPlayer.duration}"
                    )
                    onEpisodePlayNowClick.invoke(videoPlayerItem, currentIndex + 1)
                })
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(
                visible = isVisible && playerScreenState.currentTitle != null,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                TitleText(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 20.dp, top = 16.dp),
                    text = playerScreenState.currentTitle!!,
                    textSize = 28,
                    color = whiteMain,
                    lineHeight = 28,
                    fontWeight = FontWeight.Bold
                )
            }
        }


        if (playerScreenState.isLoading || playerScreenState.isPlayerBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = focusedMainColor)
            }
        }

        AnimatedVisibility(
            visible = playerScreenState.isRelatedVisible,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(500)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
        ) {
            PlayerRelatedContentRow(playerRelatedContentRowDto = playerRelatedContentRowDto,
                onUp = {
                    playerViewModel.handleEvent(PlayerEvent.HideRelated)
                    playerViewModel.handleEvent(PlayerEvent.HideNextEpisodeDialog)
                    Log.e("SHOW_CONTROLES", "show controles in media up")
                    sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                    true
                },
                onItemClick = { relatedItem, relatedItemList, index ->
                    if (!playerScreenState.hasVideoEnded) {
                        val item = videoPlayerItem[exoPlayer.currentMediaItemIndex]
                        if (item.itemSlug != null) {
                            playerViewModel.handleEvent(
                                PlayerEvent.SaveToContinueWatching(
                                    itemIndex = exoPlayer.currentMediaItemIndex,
                                    progressSeconds = (exoPlayer.currentPosition / 1000).toString(),
                                    videoDuration = exoPlayer.duration,
                                    shouldNavigateFromContinueWatching = false
                                )
                            )
                        }
                    }
                    onRelatedItemClick(relatedItem, relatedItemList, index)
                })
        }


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
                    .alpha(alpha)
            ) {
                PlayerControls(currentPosition = playerScreenState.currentPosition,
                    duration = playerScreenState.duration,
                    onSeekTo = { exoPlayer.seekTo(it) },
                    onSeekBarCenterClick = {
                        if (!sharedPlayerScreenState.isControlsVisible) {
                            Log.e("SHOW_CONTROLES", "show controles in on center click")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        } else {
                            sharedPlayerViewModel.startAutoHideTimer()
                        }
                    },
                    onPrevAndNext = {
                        if (!sharedPlayerScreenState.isControlsVisible) {
                            Log.e("SHOW_CONTROLES", "show controles in prev and next")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        } else {
                            sharedPlayerViewModel.startAutoHideTimer()
                        }
                    },
                    onKeyEvent = {
                        if (!sharedPlayerScreenState.isControlsVisible) {
                            Log.e("SHOW_CONTROLES", "show controles in keyEvent")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                            true
                        } else {
                            false
                        }
                    },
                    onPlayPause = {
                        Log.e(
                            "VIDEO_PLAYER",
                            "on PlayPause called with ${sharedPlayerScreenState.isControlsVisible}"
                        )
                        if (sharedPlayerScreenState.isControlsVisible) {
                            exoPlayer.playWhenReady = !exoPlayer.isPlaying
                            sharedPlayerViewModel.startAutoHideTimer()
                        } else {
                            Log.e("SHOW_CONTROLES", "show controles in play pause ")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        }
                        //   sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                    },
                    onRewind = {
                        val rewindMillis = 15_000L
                        val newPosition =
                            (exoPlayer.currentPosition - rewindMillis).coerceAtLeast(0L)
                        if (sharedPlayerScreenState.isControlsVisible) {
                            exoPlayer.seekTo(newPosition)
                            sharedPlayerViewModel.startAutoHideTimer()
                        } else {
                            Log.e("SHOW_CONTROLES", "show controles on Rewind")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        }
                        //   sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                    },
                    onForward = {
                        if (sharedPlayerScreenState.isControlsVisible) {
                            exoPlayer.seekForward()
                            sharedPlayerViewModel.startAutoHideTimer()
                        } else {
                            Log.e("SHOW_CONTROLES", "show controles in on Forward")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        }

                        //     sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                    },
                    onPrev = {
                        if (sharedPlayerScreenState.isControlsVisible) {
                            exoPlayer.seekToPreviousMediaItem()
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                        } else {
                            Log.e("SHOW_CONTROLES", "show controles on prev")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        }

                    },
                    onNext = {
                        if (sharedPlayerScreenState.isControlsVisible) {
                            exoPlayer.seekToNextMediaItem()
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                        } else {
                            Log.e("SHOW_CONTROLES", "show controles on next")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        }
                    },
                    inControllerUp = {
                        if (sharedPlayerScreenState.isControlsVisible) {
                            playerViewModel.handleEvent(PlayerEvent.ShowRelated)
                        } else {
                            Log.e("SHOW_CONTROLES", "show controles is Contoler up")
                            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
                        }
                        true
                    },
                    isPlaying = playerScreenState.isPlaying,
                    focusRequester = focusRequester,
                    shouldShowNextAndPrevVideo = playerScreenState.isEpisodePlaying,
                    onBackClick = {})
            }
        }
    }
}
