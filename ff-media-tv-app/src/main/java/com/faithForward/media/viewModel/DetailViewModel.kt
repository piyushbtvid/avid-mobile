package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.SeasonDto
import com.faithForward.media.detail.SeasonsNumberDto
import com.faithForward.media.viewModel.uiModels.DetailPageItem
import com.faithForward.media.viewModel.uiModels.DetailScreenEvent
import com.faithForward.media.viewModel.uiModels.RelatedContentData
import com.faithForward.media.viewModel.uiModels.UiEvent
import com.faithForward.media.viewModel.uiModels.UiState
import com.faithForward.media.viewModel.uiModels.toDetailDto
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.media.viewModel.uiModels.toSeasonDto
import com.faithForward.network.dto.detail.CardDetail
import com.faithForward.network.dto.detail.ResumeInfo
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _cardDetail = MutableStateFlow<Resource<DetailPageItem>>(Resource.Unspecified())
    val cardDetail = _cardDetail.asStateFlow()
    private var cachedCardDetail: CardDetail? = null // Cache to store the last response

    private val _relatedContentData = MutableStateFlow<RelatedContentData>(RelatedContentData.None)
    val relatedContentData = _relatedContentData.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent?>()
    val uiEvent = _uiEvent.asSharedFlow()

    val id: String? = savedStateHandle["itemId"]
    var relatedList: List<PosterCardDto>? = emptyList()

    var resumeSeasonTxt = mutableStateOf("Resume Now")
        private set

    init {
        val encodedJson = savedStateHandle.get<String>("listJson")
//        relatedList = encodedJson?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
//            ?.let { Json.decodeFromString<List<PosterCardDto>>(it) }
        Log.e("DETAIL_VIEWMODEL", "detail viewmodel init called")
        id?.let {
            //   handleEvent(DetailScreenEvent.LoadCardDetail(id, relatedList!!))
        }
    }

    fun handleEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.LoadCardDetail -> loadCardDetail(event.slug, event.relatedList)
            is DetailScreenEvent.RelatedRowFocusChanged -> updateFocusState(event.hasFocus)
            is DetailScreenEvent.RelatedRowUpClick -> handleRelatedRowUpClick()
            is DetailScreenEvent.SeasonSelected -> updateSeasonEpisodes(event.seasonNumber)
            is DetailScreenEvent.ToggleFavorite -> toggleFavorite(event.slug)
            is DetailScreenEvent.ToggleLike -> toggleLike(event.slug)
            is DetailScreenEvent.ToggleDisLike -> toggleDislike(event.slug)
        }
    }

    // method for loading item  detail from api
    private fun loadCardDetail(slug: String, relatedList: List<PosterCardDto>) {
        Log.e("DETAIL_VIEWMODEL", "load card detail is called with $slug")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.getGivenCardDetail(slug)

                if (!response.isSuccessful) {
                    emitError("API Error: ${response.message()}")
                    return@launch
                }

                val cardDetail = response.body()
                if (cardDetail == null) {
                    emitError("No data received")
                    return@launch
                }

                // using a cachedCardDetail for comparing new data and old data change in every api call
                val newDetailDto = cardDetail.toDetailDto()
                val newData = cardDetail.data
                val oldData = cachedCardDetail?.data

                //for first call when cachedCardDetail was null for the first time
                if (cachedCardDetail == null) {
                    _cardDetail.emit(Resource.Success(DetailPageItem.Card(newDetailDto)))

                    if (newData.content_type == "Series" && !newData.seasons.isNullOrEmpty()) {
                        val seasonList = newData.seasons!!.map { it.toSeasonDto() }
                        val relatedMovieList = newData.relatedContent?.map {
                            it.toPosterCardDto().copy(isRelatedSeries = true)
                        } ?: emptyList()

                        val seasonNumberList = buildSeasonNumberList(seasonList, relatedMovieList)
                        val selectedSeasonEpisodes =
                            seasonList.firstOrNull()?.episodesContentDto ?: emptyList()
                        val resumeSeasonEpisodes =
                            buildResumeEpisodes(newData.resumeInfo, seasonList)

                        updateResumeUI(
                            progressSeconds = newData.resumeInfo?.progress_seconds?.toInt(),
                            resumeInfo = newData.resumeInfo
                        )

                        val newRelatedContentData = RelatedContentData.SeriesSeasons(
                            seasonNumberList = seasonNumberList,
                            selectedSeasonEpisodes = selectedSeasonEpisodes,
                            allSeasons = seasonList,
                            relatedSeries = relatedMovieList,
                            resumeSeasonEpisodes = resumeSeasonEpisodes
                        )
                        if (_relatedContentData.value != newRelatedContentData)
                            _relatedContentData.emit(newRelatedContentData)

                    } else {
                        val relatedMovies = newData.relatedContent?.map { it.toPosterCardDto() }
                        val newRelated = if (!relatedMovies.isNullOrEmpty()) {
                            RelatedContentData.RelatedMovies(relatedMovies)
                        } else {
                            RelatedContentData.None
                        }

                        updateResumeUI(progressSeconds = newData.progressSeconds?.toInt())
                        if (_relatedContentData.value != newRelated)
                            _relatedContentData.emit(newRelated)
                    }

                    cachedCardDetail = cardDetail


                }
                // If new card is different from cached card
                else if (cachedCardDetail != cardDetail) {
                    // Only update resume-related UI
                    //comparing only resume related data like progress seconds changed or not etc
                    if (oldData?.resumeInfo != newData.resumeInfo || oldData?.progressSeconds != newData.progressSeconds) {

                        //For Series
                        if (newData.content_type == "Series" && !newData.seasons.isNullOrEmpty()) {
                            val seasonList = newData.seasons!!.map { it.toSeasonDto() }
                            val resumeSeasonEpisodes =
                                buildResumeEpisodes(newData.resumeInfo, seasonList)

                            updateResumeUI(
                                progressSeconds = newData.resumeInfo?.progress_seconds?.toInt(),
                                resumeInfo = newData.resumeInfo
                            )

                            (_relatedContentData.value as? RelatedContentData.SeriesSeasons)?.let { current ->
                                _relatedContentData.emit(current.copy(resumeSeasonEpisodes = resumeSeasonEpisodes))
                            }
                        }
                        // For Movies etc
                        else {
                            updateResumeUI(progressSeconds = newData.progressSeconds?.toInt())
                            updateMovieProgress(progressSeconds = newData.progressSeconds)
                        }
                    }
                    cachedCardDetail = cardDetail
                    Log.e("DETAIL_VIEWMODEL", "No changes detected for slug $slug")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                emitError(ex.message ?: "Something went wrong")
            }
        }
    }


    private suspend fun emitError(message: String) {
        _cardDetail.emit(Resource.Error(message))
        _relatedContentData.emit(RelatedContentData.None)
        cachedCardDetail = null
    }

    private fun buildSeasonNumberList(
        seasons: List<SeasonDto>,
        relatedList: List<PosterCardDto>,
    ): List<SeasonsNumberDto> {
        val list = mutableListOf<SeasonsNumberDto>()
        list.addAll(seasons.indices.map { SeasonsNumberDto((it + 1).toString()) })
        if (relatedList.isNotEmpty()) list.add(SeasonsNumberDto("Related"))
        return list
    }

    private fun buildResumeEpisodes(
        resumeInfo: ResumeInfo?,
        seasons: List<SeasonDto>,
    ): List<PosterCardDto> {
        if (resumeInfo == null) return seasons.firstOrNull()?.episodesContentDto ?: emptyList()

        val matchedSeason = seasons.firstOrNull { it.seasonNumber == resumeInfo.season_number }
        val episodes = matchedSeason?.episodesContentDto ?: return emptyList()

        val resumeEpisode = episodes.firstOrNull {
            it.episodeNumber == resumeInfo.episode_number
        }?.copy(progress = resumeInfo.progress_seconds)

        val remaining = episodes.filter {
            it.episodeNumber != null &&
                    it.episodeNumber != resumeInfo.episode_number &&
                    it.episodeNumber > (resumeInfo.episode_number ?: 0)
        }

        return listOfNotNull(resumeEpisode) + remaining
    }

    private fun updateResumeUI(progressSeconds: Int?, resumeInfo: ResumeInfo? = null) {
        val isVisible = progressSeconds != null && progressSeconds > 0
        if (_uiState.value.isResumeVisible != isVisible) {
            _uiState.value = _uiState.value.copy(isResumeVisible = isVisible)
        }

        val resumeText = if (isVisible) {
            resumeInfo?.let {
                val s = it.season_number ?: 1
                val e = it.episode_number ?: 1
                "Resume S$s E$e"
            } ?: "Resume Now"
        } else ""

        if (resumeSeasonTxt.value != resumeText) {
            resumeSeasonTxt.value = resumeText
        }
    }

    private fun updateMovieProgress(progressSeconds: Long?) {
        if (progressSeconds == null) return

        val currentValue = _cardDetail.value
        if (currentValue is Resource.Success && currentValue.data is DetailPageItem.Card) {
            val currentCard = currentValue.data as DetailPageItem.Card
            val updatedDto = currentCard.detailDto.copy(progress = progressSeconds.toLong())

            // Only emit if progress has changed
            if (currentCard.detailDto.progress != progressSeconds.toLong()) {
                _cardDetail.value = Resource.Success(DetailPageItem.Card(detailDto = updatedDto))
            }
        }
    }


    private fun toggleFavorite(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentDetail = _cardDetail.value
                if (currentDetail is Resource.Success && currentDetail.data is DetailPageItem.Card) {
                    val card = currentDetail.data as DetailPageItem.Card // Safe cast
                    val isCurrentlyFavorite = card.detailDto.isFavourite ?: false
                    val response = if (isCurrentlyFavorite) {
                        networkRepository.removeFromMyWatchList(slug)
                    } else {
                        networkRepository.addToMyWatchList(slug)
                    }
                    if (response.isSuccessful) {
                        _cardDetail.emit(
                            Resource.Success(
                                DetailPageItem.Card(
                                    detailDto = card.detailDto.copy(
                                        isFavourite = !isCurrentlyFavorite
                                    )
                                )
                            )
                        )
                        // Emiting UI event for Toast
                        _uiEvent.emit(
                            UiEvent(
                                if (!isCurrentlyFavorite) "Added to MyList" else "Removed from MyList"
                            )
                        )
                    } else {
                        Log.e("TOGGLE_FAVORITE", "Failed to toggle favorite: ${response.message()}")
                        // Optionally emit an error state to the UI
                        _cardDetail.emit(Resource.Error("Failed to update favorite status"))
                        _uiEvent.emit(UiEvent("Failed to update favorite"))
                    }
                } else {
                    Log.e("TOGGLE_FAVORITE", "Invalid detail state or type")
                    _uiEvent.emit(UiEvent("Failed to update favorite"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_FAVORITE", "Error toggling favorite: ${ex.message}")
                _cardDetail.emit(Resource.Error(ex.message ?: "Something went wrong"))
                _uiEvent.emit(UiEvent("Failed to update favorite"))
            }
        }
    }

    private fun toggleLike(slug: String) {
        viewModelScope.launch {
            try {
                val currentDetail = _cardDetail.value
                if (currentDetail is Resource.Success && currentDetail.data is DetailPageItem.Card) {
                    val card = currentDetail.data as DetailPageItem.Card
                    val isCurrentlyLiked = card.detailDto.isLiked ?: false
                    val response = networkRepository.likeDisLikeContent(slug, type = "like")
                    if (response.isSuccessful) {
                        val newIsLiked = !isCurrentlyLiked // Toggle like state
                        // If liking, ensure dislike is cleared
                        val newIsDisliked =
                            if (newIsLiked) false else card.detailDto.isDisliked ?: false
                        _cardDetail.emit(
                            Resource.Success(
                                DetailPageItem.Card(
                                    detailDto = card.detailDto.copy(
                                        isLiked = newIsLiked, isDisliked = newIsDisliked
                                    )
                                )
                            )
                        )
                        // Emiting UI event for Toast
                        _uiEvent.emit(
                            UiEvent(
                                if (newIsLiked) "Added to Liked" else "Removed from Liked"
                            )
                        )
                    } else {
                        Log.e("TOGGLE_LIKE", "Failed to toggle like: ${response.message()}")
                        _cardDetail.emit(Resource.Error("Failed to update like status"))
                        _uiEvent.emit(UiEvent("Failed to update like"))
                    }
                } else {
                    Log.e("TOGGLE_LIKE", "Invalid detail state or type")
                    _cardDetail.emit(Resource.Error("Invalid detail state"))
                    _uiEvent.emit(UiEvent("Failed to update like"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_LIKE", "Error toggling like: ${ex.message}")
                _cardDetail.emit(Resource.Error(ex.message ?: "Something went wrong"))
                _uiEvent.emit(UiEvent("Failed to update like"))
            }
        }
    }

    private fun toggleDislike(slug: String) {
        viewModelScope.launch {
            try {
                val currentDetail = _cardDetail.value
                if (currentDetail is Resource.Success && currentDetail.data is DetailPageItem.Card) {
                    val card = currentDetail.data as DetailPageItem.Card
                    val isCurrentlyDisliked = card.detailDto.isDisliked ?: false
                    val response = networkRepository.likeDisLikeContent(slug, type = "dislike")
                    if (response.isSuccessful) {
                        val newIsDisliked = !isCurrentlyDisliked // Toggle dislike state
                        // If disliking, ensure like is cleared
                        val newIsLiked =
                            if (newIsDisliked) false else card.detailDto.isLiked ?: false
                        _cardDetail.emit(
                            Resource.Success(
                                DetailPageItem.Card(
                                    detailDto = card.detailDto.copy(
                                        isLiked = newIsLiked, isDisliked = newIsDisliked
                                    )
                                )
                            )
                        )
                        // Emiting UI event for Toast
                        _uiEvent.emit(
                            UiEvent(
                                if (newIsDisliked) "Disliked" else "Removed from Disliked"
                            )
                        )
                    } else {
                        Log.e(
                            "TOGGLE_DISLIKE", "Failed to toggle dislike: ${response.message()}"
                        )
                        _cardDetail.emit(Resource.Error("Failed to update dislike status"))
                    }

                } else {
                    Log.e("TOGGLE_DISLIKE", "Invalid detail state or type")
                    _cardDetail.emit(Resource.Error("Invalid detail state"))
                    _uiEvent.emit(UiEvent("Failed to update dislike"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_DISLIKE", "Error toggling dislike: ${ex.message}")
                _cardDetail.emit(Resource.Error(ex.message ?: "Something went wrong"))
                _uiEvent.emit(UiEvent("Failed to update dislike"))
            }
        }
    }

    private fun updateSeasonEpisodes(seasonNumber: String) {
        viewModelScope.launch {
            val currentData = _relatedContentData.value

            if (currentData is RelatedContentData.SeriesSeasons) {
                if (seasonNumber == "Related") {
                    val relatedSeries = currentData.relatedSeries
                    if (relatedSeries.isNotEmpty()) {
                        _relatedContentData.emit(
                            currentData.copy(
                                selectedSeasonEpisodes = relatedSeries
                            )
                        )
                    }
                } else {
                    val selectedSeason = currentData.allSeasons.getOrNull(seasonNumber.toInt() - 1)
                    if (selectedSeason != null) {
                        _relatedContentData.emit(
                            currentData.copy(
                                selectedSeasonEpisodes = selectedSeason.episodesContentDto
                            )
                        )
                    } else {
                        _relatedContentData.emit(
                            currentData.copy(
                                selectedSeasonEpisodes = emptyList()
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateFocusState(hasFocus: Boolean) {
        _uiState.value = _uiState.value.copy(
            targetHeight = if (hasFocus) Int.MAX_VALUE else 280,
            isContentVisible = !hasFocus,
        )
    }

    private fun handleRelatedRowUpClick() {
        _uiState.value = _uiState.value.copy(
            targetHeight = 280,
            isContentVisible = true,
        )
    }

}