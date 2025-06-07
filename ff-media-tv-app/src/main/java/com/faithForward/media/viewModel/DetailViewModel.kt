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
import com.faithForward.media.viewModel.uiModels.UiState
import com.faithForward.media.viewModel.uiModels.toDetailDto
import com.faithForward.media.viewModel.uiModels.toSeasonDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
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

    val id: String? = savedStateHandle["itemId"]
    var relatedList: List<PosterCardDto>? = emptyList()

    init {
        val encodedJson = savedStateHandle.get<String>("listJson")
        relatedList = encodedJson
            ?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            ?.let { Json.decodeFromString<List<PosterCardDto>>(it) }

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
                        if (cardDetail.data.seasons.isNullOrEmpty()) {
                            Log.e(
                                "DETAIL_SLUG",
                                "detail isFavourite in viewModel is ${cardDetail.data.myList}"
                            )
                            _relatedContentData.emit(
                                if (relatedList.isNotEmpty()) {
                                    RelatedContentData.RelatedMovies(relatedList)
                                } else {
                                    RelatedContentData.None
                                }
                            )
                        } else {
                            val seasonList = cardDetail.data.seasons!!.map { it.toSeasonDto() }
                            val seasonNumberList = List(seasonList.size) { index ->
                                SeasonsNumberDto(seasonNumber = index + 1)
                            }
                            _relatedContentData.emit(
                                RelatedContentData.SeriesSeasons(
                                    seasonNumberList = seasonNumberList,
                                    selectedSeasonEpisodes = seasonList.firstOrNull()?.episodesContentDto
                                        ?: emptyList(),
                                    allSeasons = seasonList
                                )
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
                    } else {
                        Log.e("TOGGLE_FAVORITE", "Failed to toggle favorite: ${response.message()}")
                        // Optionally emit an error state to the UI
                        _cardDetail.emit(Resource.Error("Failed to update favorite status"))
                    }
                } else {
                    Log.e("TOGGLE_FAVORITE", "Invalid detail state or type")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_FAVORITE", "Error toggling favorite: ${ex.message}")
                _cardDetail.emit(Resource.Error(ex.message ?: "Something went wrong"))
            }
        }
    }

    private fun updateSeasonEpisodes(seasonNumber: Int) {
        viewModelScope.launch {
            val currentData = _relatedContentData.value
            if (currentData is RelatedContentData.SeriesSeasons) {
                val selectedSeason = currentData.allSeasons.getOrNull(seasonNumber - 1)
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