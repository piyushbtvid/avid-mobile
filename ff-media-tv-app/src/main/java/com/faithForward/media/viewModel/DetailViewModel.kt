package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.SeasonsNumberDto
import com.faithForward.media.viewModel.uiModels.DetailPageItem
import com.faithForward.media.viewModel.uiModels.DetailScreenEvent
import com.faithForward.media.viewModel.uiModels.RelatedContentData
import com.faithForward.media.viewModel.uiModels.UiEvent
import com.faithForward.media.viewModel.uiModels.UiState
import com.faithForward.media.viewModel.uiModels.toDetailDto
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.media.viewModel.uiModels.toSeasonDto
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

    private val _relatedContentData = MutableStateFlow<RelatedContentData>(RelatedContentData.None)
    val relatedContentData = _relatedContentData.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent?>()
    val uiEvent = _uiEvent.asSharedFlow()

    val id: String? = savedStateHandle["itemId"]
    var relatedList: List<PosterCardDto>? = emptyList()

    init {
        val encodedJson = savedStateHandle.get<String>("listJson")
//        relatedList = encodedJson?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
//            ?.let { Json.decodeFromString<List<PosterCardDto>>(it) }

        id?.let {
            handleEvent(DetailScreenEvent.LoadCardDetail(id, relatedList!!))
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

    private fun loadCardDetail(slug: String, relatedList: List<PosterCardDto>) {
        viewModelScope.launch(Dispatchers.IO) {
            _cardDetail.value = Resource.Loading()
            _relatedContentData.value = RelatedContentData.None
            try {
                val response = networkRepository.getGivenCardDetail(slug)
                if (response.isSuccessful) {
                    val cardDetail = response.body()
                    if (cardDetail != null) {
                        _cardDetail.emit(
                            Resource.Success(
                                DetailPageItem.Card(
                                    detailDto = cardDetail.toDetailDto()
                                )
                            )
                        )
                        // if Content type is Series then Series seasons and related content else  Movie , Podcast etc
                        if (cardDetail.data.content_type == "Series" && !cardDetail.data.seasons.isNullOrEmpty()) {
                            Log.e(
                                "DETAIL_SLUG",
                                "detail isFavourite in viewModel is ${cardDetail.data.myList}"
                            )

                            val seasonList = cardDetail.data.seasons!!.map { it.toSeasonDto() }
                            val relatedMovieList = cardDetail.data.relatedContent?.map {
                                it.toPosterCardDto().copy(isRelatedSeries = true)
                            } ?: emptyList()


                            // Create season number list
                            val seasonNumberList = mutableListOf<SeasonsNumberDto>()

                            if (seasonList.isNotEmpty()) {
                                seasonNumberList.addAll(
                                    List(seasonList.size) { index ->
                                        SeasonsNumberDto(seasonNumber = (index + 1).toString())
                                    }
                                )
                                if (relatedMovieList.isNotEmpty()) {
                                    seasonNumberList.add(SeasonsNumberDto(seasonNumber = "Related"))
                                }
                            } else if (relatedMovieList.isNotEmpty()) {
                                seasonNumberList.add(SeasonsNumberDto(seasonNumber = "Related"))
                            }

                            val selectedSeasonEpisodes =
                                seasonList.firstOrNull()?.episodesContentDto ?: emptyList()

                            _relatedContentData.emit(
                                RelatedContentData.SeriesSeasons(
                                    seasonNumberList = seasonNumberList,
                                    selectedSeasonEpisodes = selectedSeasonEpisodes,
                                    allSeasons = seasonList,
                                    relatedSeries = relatedMovieList
                                )
                            )
                        } else {
                            val relatedMovieList = cardDetail.data.relatedContent?.map {
                                it.toPosterCardDto()
                            }
                            _relatedContentData.emit(
                                if (!relatedMovieList.isNullOrEmpty()) {
                                    RelatedContentData.RelatedMovies(relatedMovieList)
                                } else {
                                    RelatedContentData.None
                                }
                            )
                        }
                    } else {
                        _cardDetail.emit(Resource.Error("No data received"))
                        _relatedContentData.emit(RelatedContentData.None)
                    }
                } else {
                    _cardDetail.emit(Resource.Error(response.message()))
                    _relatedContentData.emit(RelatedContentData.None)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _cardDetail.emit(Resource.Error(ex.message ?: "Something went wrong"))
                _relatedContentData.emit(RelatedContentData.None)
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