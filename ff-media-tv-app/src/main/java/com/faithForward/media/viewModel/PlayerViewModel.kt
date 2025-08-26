package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.player.PlayerDto
import com.faithForward.media.ui.player.relatedContent.PlayerRelatedContentRowDto
import com.faithForward.media.viewModel.uiModels.PlayerEvent
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private val _onContinueWatchingUpdateFlow = MutableSharedFlow<Unit>(replay = 0)
    val continueWatchingUpdateFlow = _onContinueWatchingUpdateFlow.asSharedFlow()

    private var relatedAutoDismissJob: Job? = null
    private var topBarAutoDismissJob: Job? = null

    private val _userType = MutableStateFlow<String?>(null)
    val userType: StateFlow<String?> = _userType

    init {
        getCurrentUserInfo()
    }

    fun handleEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.UpdateDuration -> {
                _state.value = _state.value.copy(duration = event.value)
            }

            is PlayerEvent.UpdateCurrentPosition -> {
                Log.d("UpdateCurrentPosition","UpdateCurrentPosition:${event.value}")
                _state.value = _state.value.copy(currentPosition = event.value)
            }

            is PlayerEvent.HideRelated -> {
                _state.value = _state.value.copy(isRelatedVisible = false)
            }

            is PlayerEvent.ShowTopBar -> {
                _state.value = _state.value.copy(isUniversalTopBarVisible = true)
                startTopBarAutoDismissTimer()
            }

            is PlayerEvent.HideTopBar -> {
                _state.value = _state.value.copy(isUniversalTopBarVisible = false)
            }

            is PlayerEvent.ShowRelated -> {
                if (_state.value.videoPlayerDto.data?.playerRelatedContentRowDto?.rowList?.isNotEmpty() == true) {
                    _state.value = _state.value.copy(isRelatedVisible = true)
                    startAutoDismissTimerForRelated()
                }
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

            is PlayerEvent.UpdateTitleText -> {
                _state.value = _state.value.copy(currentTitle = event.text)
            }

            is PlayerEvent.UpdateOrLoadPlayerData -> {
                updateOrLoadVideoPlayerData(
                    event.itemList,
                    isFromContinueWatching = event.isFromContinueWatching,
                    isPlayTrailer = event.isTrailer,
                    index = event.index
                )
            }

            is PlayerEvent.UpdatePlayerBuffering -> {
                _state.value = _state.value.copy(isPlayerBuffering = event.isBuffering)
            }

            is PlayerEvent.SaveToContinueWatching -> {
                if (event.itemIndex != null) {
                    saveContinueWatching(
                        itemIndex = event.itemIndex,
                        progress_seconds = event.progressSeconds,
                        videoDuration = event.videoDuration,
                        shouldNaviagte = event.shouldNavigateFromContinueWatching
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

            is PlayerEvent.OnContinueWatchingUpdate -> {
                viewModelScope.launch {
                    Log.e("ON_CONTINUE_WATCHING", "on continue watching update called")
                    _onContinueWatchingUpdateFlow.emit(Unit)
                }
            }

            is PlayerEvent.StartRelatedDialogAutoHide -> {
                startAutoDismissTimerForRelated()
            }

            is PlayerEvent.StartTopBarAutoHide -> {
                startTopBarAutoDismissTimer()
            }

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
        isPlayTrailer: Boolean,
        index: Int?,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(
                videoPlayerDto = Resource.Unspecified(),
                videoPlayingIndex = null,
                isLoading = true,
                currentTitle = null
            )

            val firstItem = itemList.firstOrNull()
            val hasUrl = firstItem?.videoHlsUrl?.isNotEmpty() == true
            val hasRelated = !firstItem?.relatedList.isNullOrEmpty()

            val isMovie = firstItem?.contentType == "Movie"



            try {
                //  If URL and Related List are available  use directly And this is for Movies and its related from detail

                if (isPlayTrailer && hasUrl && itemList.size <= 1) {
                    val videoPlayerDtoList = itemList.map { it.toVideoPlayerDto() }

                    val title = videoPlayerDtoList.getOrNull(index ?: 0)?.title

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = videoPlayerDtoList,
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = "Next Up...", rowList = emptyList()
                                ),
                            )
                        ), isLoading = false, videoPlayingIndex = index, currentTitle = title
                    )
                } else if (hasUrl && hasRelated && isMovie && itemList.size <= 1) {
                    val relatedContentItemDtoList =
                        firstItem!!.relatedList!!.map { it.toRelatedItemDto() }
                    val videoPlayerDtoList = itemList.map { it.toVideoPlayerDto() }
                    val title = videoPlayerDtoList.getOrNull(index ?: 0)?.title

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = videoPlayerDtoList,
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = "Next Up...", rowList = relatedContentItemDtoList
                                ),
                            )
                        ), isLoading = false, videoPlayingIndex = index, currentTitle = title
                    )

                }
                //this is for series season episodes for all case i.e from detail and from related
                else if (hasUrl && !isMovie && !isFromContinueWatching) {
                    val relatedContentItemDtoList = itemList.map { it.toRelatedItemDto() }
                    val videoPlayerDtoList = itemList.map { it.toVideoPlayerDto() }

                    val title = videoPlayerDtoList.getOrNull(index ?: 0)?.title

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
                        ), isLoading = false, videoPlayingIndex = index, currentTitle = title
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
                        cardDetail.data.toPosterCardDto().toVideoPlayerDto().copy(
                            progress = if (isFromContinueWatching) firstItem.progress
                                ?: 0 else 0
                        )

                    val title = currentPlayingItem.title

                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(
                                videoPlayerDtoList = listOf(currentPlayingItem),
                                playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                    title = "Next Up...", rowList = relatedContentRowDtoList
                                )
                            )
                        ), isLoading = false, videoPlayingIndex = index, currentTitle = title
                    )
                }
                //is from continue watching and is a Series episode
                else if (isFromContinueWatching && firstItem?.seriesSlug != null) {
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
//                        val remainingSeasons =
//                            allSeasons.subList(matchedSeasonIndex, allSeasons.size)

                        //matched season all episodes
                        val matchedSeasonEpisodes =
                            allSeasons.getOrNull(matchedSeasonIndex)?.episodes


                        //all following or remaining seasons episode in one list
                        //  val combinedEpisodes = remainingSeasons.flatMap { it.episodes }

                        // Updated the episode list while preserving progress to PosterCardDto
//                        val updatedEpisodes = combinedEpisodes.map { episode ->
//                            if (episode.slug == continueWatchingEpisodeSlug) {
//                                episode.toPosterDto().copy(progress = firstItem.progress)
//                            } else {
//                                episode.toPosterDto()
//                            }
//                        }

                        // updating matched season episode to PosterCardDto and preserving progress
                        val matchedSeasonUpdatedEpisodes =
                            matchedSeasonEpisodes?.map { episode ->
                                if (episode.slug == continueWatchingEpisodeSlug) {
                                    episode.toPosterDto().copy(progress = firstItem.progress)
                                } else {
                                    episode.toPosterDto()
                                }
                            }

                        // Finding Resumed Index of continue watching item from all episodes
                        val resumeIndex =
                            matchedSeasonUpdatedEpisodes?.indexOfFirst { it.slug == continueWatchingEpisodeSlug }

                        // converting all Episodes in VideoPlayerDto for Player
                        val videoPlayList = matchedSeasonUpdatedEpisodes?.map {
                            it.toVideoPlayerDto()
                        }

                        // converting all Episodes in RelatedItemDto for showing them in Related List
                        val relatedList = matchedSeasonUpdatedEpisodes?.map {
                            it.toRelatedItemDto()
                        }
                        Log.e("ResumeList", "Updated episodes: $matchedSeasonUpdatedEpisodes")
                        Log.e("ResumeList", "Resume episode index: $resumeIndex")
                        val title = videoPlayList?.getOrNull(index ?: 0)?.title

                        if (videoPlayList != null && relatedList != null) {
                            _state.value = _state.value.copy(
                                videoPlayerDto = Resource.Success(
                                    PlayerDto(
                                        videoPlayerDtoList = videoPlayList,
                                        playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                                            title = "Episodes...", rowList = relatedList
                                        )
                                    )
                                ),
                                isLoading = false,
                                videoPlayingIndex = resumeIndex,
                                currentTitle = title
                            )
                        }
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
        shouldNaviagte: Boolean,
    ) {

        val currentVideoList = state.value.videoPlayerDto

        val itemSlug = currentVideoList.data?.videoPlayerDtoList?.getOrNull(itemIndex)?.itemSlug


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
                    if (shouldNaviagte) {
                        handleEvent(PlayerEvent.OnContinueWatchingUpdate)
                    }
                } catch (ex: Exception) {
                    if (shouldNaviagte) {
                        handleEvent(PlayerEvent.OnContinueWatchingUpdate)
                    }
                    Log.e("CONTINUE_WATCHING", "response success with exception ${ex.message}")
                }
            }
        } else {
            if (shouldNaviagte) {
                handleEvent(PlayerEvent.OnContinueWatchingUpdate)
            }
        }
    }

    private fun startAutoDismissTimerForRelated() {
        relatedAutoDismissJob?.cancel()
        relatedAutoDismissJob = viewModelScope.launch {
            delay(10_000) // 10 seconds
            _state.update {
                it.copy(
                    isRelatedVisible = false,
                )
            }
        }
    }

    private fun startTopBarAutoDismissTimer() {
        topBarAutoDismissJob?.cancel()
        topBarAutoDismissJob = viewModelScope.launch {
            delay(10_000) // 10 seconds
            _state.update {
                it.copy(
                    isUniversalTopBarVisible = false,
                )
            }
        }
    }

    private fun getCurrentUserInfo() {
        viewModelScope.launch {
            val response = networkRepository.getCurrentSession()
            response?.season?.user?.user_type?.let {
                Log.e("USER_TYPE", "user type in PlayerViewModel checkVideo is $it")
                _userType.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        relatedAutoDismissJob?.cancel()
        topBarAutoDismissJob?.cancel()
    }
}