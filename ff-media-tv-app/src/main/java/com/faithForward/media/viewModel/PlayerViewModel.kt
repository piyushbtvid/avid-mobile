package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.player.PlayerDto
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

    private val _interactionFlow = MutableSharedFlow<Unit>()
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

            is PlayerEvent.UpdateIsPlaying -> {
                _state.value = _state.value.copy(isPlaying = event.isPlaying)
            }

            is PlayerEvent.UpdateOrLoadPlayerData -> {
                updateOrLoadVideoPlayerData(event.itemList)
            }

            is PlayerEvent.UpdatePlayerBuffering -> {
                _state.value = _state.value.copy(isPlayerBuffering = event.isBuffering)
            }

            is PlayerEvent.SaveToContinueWatching -> {
                saveContinueWatching(
                    itemSlug = event.itemSlug,
                    progress_seconds = event.progressSeconds,
                    videoDuration = event.videoDuration
                )
            }

            is PlayerEvent.UpdateVideoEndedState -> {
                _state.value = _state.value.copy(
                    hasVideoEnded = event.isEnded
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

    private fun updateOrLoadVideoPlayerData(itemList: List<PosterCardDto>) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(isLoading = true)

            val firstItem = itemList.firstOrNull()
            val hasUrl = firstItem?.videoHlsUrl?.isNotEmpty() == true
            val hasRelated = !firstItem?.relatedList.isNullOrEmpty()

            Log.e("RELATED_PLAYER", "related in player is ${firstItem?.relatedList}")

            try {
                //  If URL and Related List are available  use directly
                if (hasUrl && hasRelated) {
                    val relatedContentItemDtoList =
                        firstItem!!.relatedList!!.map { it.toRelatedItemDto() }
                    val videoPlayerDtoList = itemList.map { it.toVideoPlayerDto() }

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = videoPlayerDtoList,
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = if (firstItem.contentType == "Episode"
                                        || firstItem.contentType == "Series"
                                    )
                                        "Episodes..." else "Next Up...",
                                    rowList = relatedContentItemDtoList
                                )
                            )
                        ), isLoading = false
                    )
                }
                //  Anything missing  fetch all from API using slug for Episodes
                else if (firstItem?.slug != null && firstItem.contentType == "Episode") {
                    Log.e(
                        "SERIES_CONTENT",
                        "Fetching episode details for slug: ${firstItem.slug} and Content Type is ${firstItem.contentType}"
                    )

                    val episodeResponse = networkRepository.getGivenCardDetail(firstItem.slug)
                    if (!episodeResponse.isSuccessful) return@launch

                    val episodeDetail = episodeResponse.body()?.data ?: return@launch

                    // If it's an Episode, then we need to fetch the series data
                    if (episodeDetail.content_type == "Episode" && episodeDetail.seriesSlug != null) {
                        val seriesSlug = episodeDetail.seriesSlug ?: ""
                        val seasonNumber = episodeDetail.seasonNumber

                        Log.e(
                            "LOAD_RELATED",
                            "Episode detected. Fetching series data for slug: $seriesSlug"
                        )

                        val seriesResponse = networkRepository.getGivenCardDetail(seriesSlug)
                        if (!seriesResponse.isSuccessful) return@launch

                        val seriesDetail = seriesResponse.body()?.data ?: return@launch

                        // Find the matching season
                        val season = seriesDetail.seasons?.find { it.season_number == seasonNumber }
                        val allEpisodes = season?.episodes ?: emptyList()

                        val relatedEpisodes = allEpisodes.filter { it.slug != episodeDetail.slug }

                        val relatedContentRowDtoList =
                            relatedEpisodes.map { it.toPosterDto().toRelatedItemDto() }

                        // 1. Create the current playing item
                        val currentPlayingItem = episodeDetail
                            .toPosterCardDto()
                            .toVideoPlayerDto()
                            .copy(progress = 0)

                        // 2. Create the remaining episodes excluding the current one
                        val nextEpisodes =
                            relatedEpisodes.map { it.toPosterDto().toVideoPlayerDto() }

                        // 3. Final playlist: current + next episodes
                        val fullPlaylist = listOf(currentPlayingItem) + nextEpisodes

                        // 4. Set state with the full playlist
                        _state.value = _state.value.copy(
                            videoPlayerDto = Resource.Success(
                                PlayerDto(
                                    videoPlayerDtoList = fullPlaylist,
                                    playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                        title = "Episodes...",
                                        rowList = relatedContentRowDtoList
                                    )
                                )
                            ),
                            isLoading = false
                        )

                    }
                }
                // For movies
                else if (firstItem?.slug != null && firstItem.contentType == "Movie") {
                    Log.e(
                        "LOAD_RELATED",
                        "Fallback: Fetching all from API for slug: ${firstItem.slug}"
                    )

                    val response = networkRepository.getGivenCardDetail(firstItem.slug)
                    if (!response.isSuccessful) return@launch

                    val cardDetail = response.body() ?: return@launch
                    val relatedList = cardDetail.data.relatedContent ?: emptyList()

                    val relatedContentRowDtoList = relatedList.map { it.toRelatedItemDto() }
                    val currentPlayingItem = cardDetail.data
                        .toPosterCardDto()
                        .toVideoPlayerDto()
                        .copy(progress = 0)

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = listOf(currentPlayingItem),
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = "Next Up...",
                                    rowList = relatedContentRowDtoList
                                )
                            )
                        ),
                        isLoading = false
                    )
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("PLAYER_ERROR", "Error in updateOrLoad: ${ex.message}")
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }


    private fun saveContinueWatching(
        itemSlug: String,
        progress_seconds: String,
        videoDuration: String,
    ) {
        Log.e(
            "CONTINUE_WATCHING",
            "save to continue watching called with $itemSlug  $progress_seconds  $videoDuration"
        )
        viewModelScope.launch {
            try {
                val request = ContinueWatchingRequest(
                    slug = itemSlug, progress_seconds = progress_seconds, duration = videoDuration
                )
                val response = networkRepository.saveContinueWatching(request)
                if (response.isSuccessful) {
                    Log.e("CONTINUE_WATCHING", "response success with ${response.body()}")
                } else {
                    Log.e("CONTINUE_WATCHING", "response error with ${response.message()}")
                }
            } catch (ex: Exception) {
                Log.e("CONTINUE_WATCHING", "response success with exception ${ex.message}")
            }
        }
    }
}