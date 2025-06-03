package com.faithForward.media.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.SeasonsNumberDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _cardDetail = MutableStateFlow<Resource<DetailPageItem>>(Resource.Unspecified())
    val cardDetail = _cardDetail.asStateFlow()

    private val _relatedContentData = MutableStateFlow<RelatedContentData>(RelatedContentData.None)
    val relatedContentData = _relatedContentData.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.LoadCardDetail -> loadCardDetail(event.id, event.relatedList)
            is DetailScreenEvent.RelatedRowFocusChanged -> updateFocusState(event.hasFocus)
            is DetailScreenEvent.RelatedRowUpClick -> handleRelatedRowUpClick()
            is DetailScreenEvent.SeasonSelected -> updateSeasonEpisodes(event.seasonNumber)
        }
    }

    private fun loadCardDetail(id: String, relatedList: List<PosterCardDto>) {
        viewModelScope.launch(Dispatchers.IO) {
            _cardDetail.value = Resource.Loading()
            _relatedContentData.value = RelatedContentData.None // Clear previous data
            try {
                val response = networkRepository.getGivenCardDetail(id)
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
                        // Set related content based on whether it's a movie or series
                        if (cardDetail.data.seasons.isNullOrEmpty()) {
                            _relatedContentData.emit(
                                if (relatedList.isNotEmpty()) {
                                    RelatedContentData.RelatedMovies(relatedList)
                                } else {
                                    RelatedContentData.None
                                }
                            )
                        } else {
                            val seasonList = cardDetail.data.seasons!!.map { it.toSeasonDto() }
                            // Generate seasonNumberList based on seasonList indices
                            //if required change seasonNumber List from SeasonNumber Field
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
            isContentVisible = !hasFocus, // Hide content when related content is focused
        )
    }

    private fun handleRelatedRowUpClick() {
        _uiState.value = _uiState.value.copy(
            targetHeight = 280,
            isContentVisible = true, // Show content when related row is unfocused
        )
    }
}