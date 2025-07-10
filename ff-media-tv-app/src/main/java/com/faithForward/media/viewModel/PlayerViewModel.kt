package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.player.PlayerDto
import com.faithForward.media.player.VideoPlayerDto
import com.faithForward.media.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import com.faithForward.media.viewModel.uiModels.PlayerState
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.media.viewModel.uiModels.toPosterDto
import com.faithForward.media.viewModel.uiModels.toRelatedItemDto
import com.faithForward.media.viewModel.uiModels.toVideoPlayerDto
import com.faithForward.network.dto.request.ContinueWatchingRequest
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var autoHideJob: Job? = null

    var playerState by mutableStateOf(PlayerPlayingState.IDLE)
        private set

    private var lastPlaybackState: PlayerPlayingState = PlayerPlayingState.PLAYING

    private val _interactionFlow = MutableSharedFlow<Unit>(replay = 0)
    val interactionFlow = _interactionFlow.asSharedFlow()

    fun handleEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.UpdateDuration -> {
                _state.value = _state.value.copy(duration = event.value)
            }

            is PlayerEvent.UpdateCurrentPosition -> {
                _state.value = _state.value.copy(currentPosition = event.value)
            }

            is PlayerEvent.ShowControls -> {
                Log.e("PLAYER", "Handling ShowControls event")
                _state.value = _state.value.copy(isControlsVisible = true)
                startAutoHideTimer()
            }

            is PlayerEvent.HideControls -> {
                Log.e("PLAYER_UI", "Handling HideControls even")
                Log.e("PLAYER", "Handling HideControls event")
                _state.value = _state.value.copy(isControlsVisible = false)
            }

            is PlayerEvent.HideRelated -> {
                _state.value = _state.value.copy(isRelatedVisible = false)
            }

            is PlayerEvent.ShowRelated -> {
                _state.value = _state.value.copy(isRelatedVisible = true)
            }

            is PlayerEvent.ShowNextEpisodeDialog -> {
                _state.value = _state.value.copy(isNextEpisodeDialogVisible = true)
            }

            is PlayerEvent.HideNextEpisodeDialog -> {
                _state.value = _state.value.copy(isNextEpisodeDialogVisible = false)
            }

            is PlayerEvent.UpdateIsPlaying -> {
                _state.value = _state.value.copy(isPlaying = event.isPlaying)
            }

            is PlayerEvent.UpdateOrLoadPlayerData -> {
                updateOrLoadVideoPlayerData(
                    event.itemList,
                    isFromContinueWatching = event.isFromContinueWatching,
                    index = event.index
                )
            }

            is PlayerEvent.UpdatePlayerBuffering -> {
                _state.value = _state.value.copy(isPlayerBuffering = event.isBuffering)
            }

            is PlayerEvent.SaveToContinueWatching -> {
                if(event.itemIndex!=null){
                    saveContinueWatching(
                        itemIndex = event.itemIndex,
                        progress_seconds = event.progressSeconds,
                        videoDuration = event.videoDuration
                    )
                }
            }

            is PlayerEvent.UpdateVideoEndedState -> {
                _state.value = _state.value.copy(
                    hasVideoEnded = event.isEnded
                )
            }

            is PlayerEvent.UpdateVideoPlayingIndex -> {
                _state.value = _state.value.copy(
                    videoPlayingIndex = event.value
                )
            }

            is PlayerEvent.UpdateIsEpisodePlayingOrNot -> {
                _state.value = _state.value.copy(
                    isEpisodePlaying = event.isEpisode
                )
            }

        }
    }

    private fun startAutoHideTimer() {
        autoHideJob?.cancel()
        autoHideJob = viewModelScope.launch {
            Log.e("PLAYER", "Starting auto-hide timer")
            delay(10000)
            Log.e("PLAYER", "Setting controls invisible after timer")
            _state.value = _state.value.copy(isControlsVisible = false)
        }
    }

    fun togglePlayPause() {
        playerState = if (lastPlaybackState == PlayerPlayingState.PLAYING) {
            lastPlaybackState = PlayerPlayingState.PAUSED
            PlayerPlayingState.PAUSED
        } else {
            lastPlaybackState = PlayerPlayingState.PLAYING
            PlayerPlayingState.PLAYING
        }
    }

    fun handlePlayerAction(action: PlayerPlayingState) {
        when (action) {
            PlayerPlayingState.PLAYING, PlayerPlayingState.PAUSED -> {
                lastPlaybackState = action  // Update last playback state
                playerState = action
            }

            PlayerPlayingState.REWINDING, PlayerPlayingState.FORWARDING -> {
                playerState = action

                // Reset to IDLE after a short delay
                viewModelScope.launch {
                    delay(200) // Adjust delay as needed
                    playerState = PlayerPlayingState.IDLE
                }
            }

            PlayerPlayingState.IDLE -> Unit // No action needed
            PlayerPlayingState.MUTE_UN_MUTE -> {}
        }
    }

    fun onUserInteraction(from: String) {
        viewModelScope.launch {
            Log.e("PLAYER_UI", "on use interction is called  in viewModel from $from")
            _interactionFlow.emit(Unit)
        }
    }


    private fun loadUrlWithRelatedData(slug: String) {
        Log.e("LOAD_RELATED", "load Related is called in player with slug $slug")
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val response = networkRepository.getGivenCardDetail(slug)

                if (!response.isSuccessful) {
                    return@launch
                }

                val cardDetail = response.body()
                if (cardDetail != null) {
                    val relatedList = cardDetail.data.relatedContent
                    if (!relatedList.isNullOrEmpty()) {

                        val relatedContentRowDtoList = relatedList.map {
                            it.toRelatedItemDto()
                        }
                        val currentPlayingVideoPosterCardDto =
                            cardDetail.data.toPosterCardDto().toVideoPlayerDto()

                        _state.value = _state.value.copy(
                            videoPlayerDto = Resource.Success(
                                PlayerDto(
                                    videoPlayerDtoList = listOf(currentPlayingVideoPosterCardDto),
                                    playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                        title = "Next Up...", rowList = relatedContentRowDtoList
                                    )
                                )
                            ), isLoading = false
                        )

                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("EXCEPTION", "exception in related in player viewmodel is ${ex.message}")
            }
        }

    }

    private fun updateOrLoadVideoPlayerData(
        itemList: List<PosterCardDto>,
        isFromContinueWatching: Boolean,
        index: Int?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(
                videoPlayerDto = Resource.Unspecified(),
                videoPlayingIndex = null,
                isLoading = true
            )

            val firstItem = itemList.firstOrNull()
            val hasUrl = firstItem?.videoHlsUrl?.isNotEmpty() == true
            val hasRelated = !firstItem?.relatedList.isNullOrEmpty()

            val isMovie = firstItem?.contentType == "Movie"

            Log.e(
                "CONTINUE_WATCHING_CLICK",
                "item list in PlayerViewModel with  is ${firstItem?.relatedList}"
            )

            Log.e(
                "CONTINUE_WATCHING_CLICK",
                "item content type in viewModel is ${firstItem?.contentType}"
            )

            Log.e(
                "CONTINUE_WATCHING_CLICK", "item List size  in viewModel is ${itemList.size}"
            )
            Log.e(
                "CONTINUE_WATCHING_CLICK",
                "is from continue watching $isFromContinueWatching   player viewModel with ${firstItem?.seriesSlug}"
            )

            Log.e(
                "CONTINUE_WATCHING_CLICK",
                "load Player data called in viewModel with $index and  $itemList"
            )

            try {
                //  If URL and Related List are available  use directly And this is for Movies and its related from detail
                if (hasUrl && hasRelated && isMovie && itemList.size <= 1) {
                    Log.e(
                        "CONTINUE_WATCHING_CLICK",
                        "item has related and url  calling for isMovie"
                    )
                    val relatedContentItemDtoList =
                        firstItem!!.relatedList!!.map { it.toRelatedItemDto() }
                    val videoPlayerDtoList = itemList.map { it.toVideoPlayerDto() }

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = videoPlayerDtoList,
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = "Next Up...", rowList = relatedContentItemDtoList
                                ),
                            )
                        ), isLoading = false, videoPlayingIndex = index
                    )

                }
                //this is for series season episodes for all case i.e from detail and from related
                else if (hasUrl && !isMovie && !isFromContinueWatching) {
                    Log.e("CONTINUE_WATCHING_CLICK", "has Url and not movie and is Series episode")
                    Log.e(
                        "PLAYER_CONTINUE",
                        "item has url  calling for !isMovie with item index $index "
                    )
                    val relatedContentItemDtoList = itemList.map { it.toRelatedItemDto() }
                    val videoPlayerDtoList = itemList.map { it.toVideoPlayerDto() }

                    Log.e("PLAY_CLICK", "has Url and $videoPlayerDtoList ")

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = videoPlayerDtoList,
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = if (firstItem?.contentType == "Episode" || firstItem?.contentType == "Series") "Episodes..." else "Next Up...",
                                    rowList = relatedContentItemDtoList
                                )
                            )
                        ), isLoading = false, videoPlayingIndex = index
                    )
                }
                // For movies from related Movie
                else if (firstItem?.slug != null && firstItem.contentType == "Movie") {
                    Log.e(
                        "CONTINUE_WATCHING_CLICK",
                        "item has not either related and url and content type is ${firstItem.contentType}"
                    )
                    Log.e(
                        "LOAD_RELATED",
                        "Fallback: Fetching all from API for slug: ${firstItem.slug}"
                    )

                    val response = networkRepository.getGivenCardDetail(firstItem.slug)
                    if (!response.isSuccessful) return@launch

                    val cardDetail = response.body() ?: return@launch
                    val relatedList = cardDetail.data.relatedContent ?: emptyList()

                    val relatedContentRowDtoList = relatedList.map { it.toRelatedItemDto() }
                    val currentPlayingItem =
                        cardDetail.data.toPosterCardDto().toVideoPlayerDto().copy(progress = 0)

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = listOf(currentPlayingItem),
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = "Next Up...", rowList = relatedContentRowDtoList
                                )
                            )
                        ), isLoading = false, videoPlayingIndex = index
                    )
                }
                //is from continue watching and is a Series episode
                else if (isFromContinueWatching && firstItem?.seriesSlug != null) {
                    Log.e(
                        "IS_CONTINUE_WATCHING_CLICK",
                        "is from continue watching in player viewModel with ${firstItem.seriesSlug} and progress is ${firstItem.progress}  "
                    )
                    Log.e(
                        "IS_CONTINUE_WATCHING_CLICK",
                        "is from continue watching in player viewModel with item is $firstItem  "
                    )
                    val response = networkRepository.getGivenCardDetail(firstItem.seriesSlug)
                    if (!response.isSuccessful) return@launch

                    val cardDetail = response.body() ?: return@launch

                    if (cardDetail.data.content_type == "Series") {
                        val seasons = cardDetail.data.seasons
                        val continueWatchingEpisodeSlug = firstItem.slug

                        // Find allSeasons
                        val allSeasons = seasons ?: return@launch

                        // Found matchedSeasonIndex of continue watching in all seasons
                        val matchedSeasonIndex = allSeasons.indexOfFirst { season ->
                            season.episodes.any { it.slug == continueWatchingEpisodeSlug }
                        }

                        if (matchedSeasonIndex == -1) return@launch

                        // Built a combined list from matched season to last
                        val remainingSeasons =
                            allSeasons.subList(matchedSeasonIndex, allSeasons.size)
                        //all following or remaining seasons episode in one list
                        val combinedEpisodes = remainingSeasons.flatMap { it.episodes }

                        // Updated the episode list while preserving progress to PosterCardDto
                        val updatedEpisodes = combinedEpisodes.map { episode ->
                            if (episode.slug == continueWatchingEpisodeSlug) {
                                episode.toPosterDto().copy(progress = firstItem.progress)
                            } else {
                                episode.toPosterDto()
                            }
                        }


                        // Finding Resumed Index of continue watching item from all episodes
                        val resumeIndex =
                            updatedEpisodes.indexOfFirst { it.slug == continueWatchingEpisodeSlug }

                        // converting all Episodes in VideoPlayerDto for Player
                        val videoPlayList = updatedEpisodes.map {
                            it.toVideoPlayerDto()
                        }

                        // converting all Episodes in RelatedItemDto for showing them in Related List
                        val relatedList = updatedEpisodes.map {
                            it.toRelatedItemDto()
                        }
                        Log.d("ResumeList", "Updated episodes: $updatedEpisodes")
                        Log.d("ResumeList", "Resume episode index: $resumeIndex")
                        Log.e(
                            "IS_CONTINUE_WATCHING_CLICK",
                            "is from continue watching in player viewModel with finial result of VideoList $videoPlayList  "
                        )
                        Log.e(
                            "IS_CONTINUE_WATCHING_CLICK",
                            "is from continue watching in player viewModel with finial result of RelatedList $relatedList  "
                        )
                        _state.value = _state.value.copy(
                            videoPlayerDto = Resource.Success(
                                PlayerDto(
                                    videoPlayerDtoList = videoPlayList,
                                    playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                        title = "Next Up...", rowList = relatedList
                                    )
                                )
                            ), isLoading = false, videoPlayingIndex = resumeIndex
                        )
                    }
                }


            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("PLAYER_ERROR", "Error in updateOrLoad: ${ex.message}")
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }


    private fun saveContinueWatching(
        itemIndex: Int,
        progress_seconds: String,
        videoDuration: Long,
    ) {

        val currentVideoList = state.value.videoPlayerDto

        val itemSlug = currentVideoList.data?.videoPlayerDtoList?.getOrNull(itemIndex)?.itemSlug

        Log.e(
            "TEST_WATCH",
            "current Video List size is ${currentVideoList.data?.videoPlayerDtoList?.size}"
        )

        Log.e(
            "STOP_TRACK",
            "Save to continue watching is called in ViewModel with progress $progress_seconds and duartion is ${videoDuration / 1000}"
        )
        Log.e(
            "CONTINUE_WATCHING",
            "save to continue watching called with $itemIndex  $progress_seconds  $videoDuration"
        )

        if (itemSlug != null) {
            viewModelScope.launch {
                try {
                    val request = ContinueWatchingRequest(
                        slug = itemSlug,
                        progress_seconds = progress_seconds,
                        duration = (videoDuration / 1000).toString()
                    )
                    val response = networkRepository.saveContinueWatching(request)
                    if (response.isSuccessful) {
                        Log.e("TEST_WATCH", "response success with ${response.body()}")
                        Log.e(
                            "STOP_TRACK",
                            "Save to continue watching is called in ViewModel after success with ${response.body()}"
                        )
                    } else {
                        Log.e("CONTINUE_WATCHING", "response error with ${response.message()}")
                    }
                } catch (ex: Exception) {
                    Log.e("CONTINUE_WATCHING", "response success with exception ${ex.message}")
                }
            }
        }
    }
}