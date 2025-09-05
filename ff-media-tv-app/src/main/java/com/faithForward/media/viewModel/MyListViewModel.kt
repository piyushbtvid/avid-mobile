package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.viewModel.uiModels.CarouselClickUiState
import com.faithForward.media.viewModel.uiModels.HomePageItem
import com.faithForward.media.viewModel.uiModels.UiEvent
import com.faithForward.media.viewModel.uiModels.toDetailDto
import com.faithForward.media.viewModel.uiModels.toHomePageItems
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _homepageData: MutableStateFlow<Resource<List<HomePageItem>>> =
        MutableStateFlow(Resource.Unspecified())
    val homePageData: StateFlow<Resource<List<HomePageItem>>> = _homepageData

    private val _uiEvent = MutableSharedFlow<UiEvent?>()
    val uiEvent = _uiEvent.asSharedFlow()


    var contentRowFocusedIndex by mutableStateOf(-1)
        private set

    private val _carouselClickUiState =
        MutableSharedFlow<CarouselClickUiState>()
    val carouselClickUiState = _carouselClickUiState.asSharedFlow()



    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    init {
        loadSectionContent("my-list")
    }

    private fun loadSectionContent(sectionId: String) {
        Log.e("HOME_DATA", "loadSection is called in viewModel with sectionId: $sectionId")
        viewModelScope.launch {
            _homepageData.emit(Resource.Loading())
            try {
                // Fetch data for the given sectionId
                val sectionDataDeferred =
                    async { networkRepository.getMyListSectionData(sectionId) }

                val sectionData = sectionDataDeferred.await()

                // Process section data (Carousel and Poster rows)
                val sectionItems = if (sectionData.isSuccessful) {
                    Log.e("HOME_DATA", "response is success with ${sectionData.body()}")
                    sectionData.body()?.toHomePageItems() ?: listOf()
                } else {
                    Log.e("HOME_DATA", "response is error with ${sectionData.message()}")
                    listOf()
                }

                // Combine the data with CategoryRow at index 1
                val combinedItems = buildList {
                    val carousel = sectionItems.find { it is HomePageItem.CarouselRow }
                    if (carousel != null) {
                        add(carousel)
                    }
                    val category = sectionItems.find { it is HomePageItem.CategoryRow }
                    if (category != null) {
                        add(category)
                    }
                    addAll(sectionItems.filter { it !is HomePageItem.CarouselRow && it !is HomePageItem.CategoryRow })
                }
                _homepageData.emit(Resource.Success(combinedItems))
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("HOME_DATA", "response is exception with ${ex.message}")
                _homepageData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }

    fun toggleFavorite(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentData = _homepageData.value
                if (currentData is Resource.Success) {
                    val carouselRow =
                        currentData.data?.find { it is HomePageItem.CarouselRow } as? HomePageItem.CarouselRow
                    if (carouselRow != null && carouselRow.dto.carouselItemsDto.isNotEmpty()) {
                        val carouselItem = carouselRow.dto.carouselItemsDto[0]
                        val isCurrentlyFavorite = carouselItem.isFavourite ?: false
                        val response = if (isCurrentlyFavorite) {
                            networkRepository.removeFromMyWatchList(slug)
                        } else {
                            networkRepository.addToMyWatchList(slug)
                        }
                        if (response.isSuccessful) {
                            // Update the carousel item's isFavourite
                            val updatedCarouselItem =
                                carouselItem.copy(isFavourite = !isCurrentlyFavorite)
                            val updatedCarouselRow = carouselRow.copy(
                                dto = carouselRow.dto.copy(
                                    carouselItemsDto = listOf(updatedCarouselItem)
                                )
                            )
                            // Update the homepage data with the new carousel row
                            val updatedItems = currentData.data?.map { item ->
                                if (item is HomePageItem.CarouselRow) updatedCarouselRow else item
                            }
                            _homepageData.emit(Resource.Success(updatedItems ?: emptyList()))
                            // Emit UI event for Toast
                            _uiEvent.emit(
                                UiEvent(
                                    if (!isCurrentlyFavorite) "Added to MyList" else "Removed from MyList"
                                )
                            )
                        } else {
                            Log.e(
                                "TOGGLE_FAVORITE",
                                "Failed to toggle favorite: ${response.message()}"
                            )
                            _uiEvent.emit(UiEvent("Failed to update favorite"))
                        }
                    } else {
                        Log.e("TOGGLE_FAVORITE", "Carousel row not found or empty")
                        _uiEvent.emit(UiEvent("Failed to update favorite"))
                    }
                } else {
                    Log.e("TOGGLE_FAVORITE", "Invalid homepage data state")
                    _uiEvent.emit(UiEvent("Failed to update favorite"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_FAVORITE", "Error toggling favorite: ${ex.message}")
                _uiEvent.emit(UiEvent("Error: ${ex.message ?: "Something went wrong"}"))
            }
        }
    }

    fun toggleLike(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentData = _homepageData.value
                if (currentData is Resource.Success) {
                    val carouselRow =
                        currentData.data?.find { it is HomePageItem.CarouselRow } as? HomePageItem.CarouselRow
                    if (carouselRow != null && carouselRow.dto.carouselItemsDto.isNotEmpty()) {
                        val carouselItem = carouselRow.dto.carouselItemsDto[0]
                        val isCurrentlyLiked = carouselItem.isLiked ?: false
                        val response = networkRepository.likeDisLikeContent(slug, type = "like")
                        if (response.isSuccessful) {
                            val newIsLiked = !isCurrentlyLiked
                            val newIsDisliked =
                                if (newIsLiked) false else carouselItem.isDisliked ?: false
                            // Update the carousel item's isLiked and isDisliked
                            val updatedCarouselItem = carouselItem.copy(
                                isLiked = newIsLiked, isDisliked = newIsDisliked
                            )
                            val updatedCarouselRow = carouselRow.copy(
                                dto = carouselRow.dto.copy(
                                    carouselItemsDto = listOf(updatedCarouselItem)
                                )
                            )
                            // Update the homepage data with the new carousel row
                            val updatedItems = currentData.data?.map { item ->
                                if (item is HomePageItem.CarouselRow) updatedCarouselRow else item
                            }
                            _homepageData.emit(Resource.Success(updatedItems ?: emptyList()))
                            // Emit UI event for Toast
                            _uiEvent.emit(
                                UiEvent(
                                    if (newIsLiked) "Added to Liked" else "Removed from Liked"
                                )
                            )
                        } else {
                            Log.e("TOGGLE_LIKE", "Failed to toggle like: ${response.message()}")
                            _uiEvent.emit(UiEvent("Failed to update like"))
                        }
                    } else {
                        Log.e("TOGGLE_LIKE", "Carousel row not found or empty")
                        _uiEvent.emit(UiEvent("Failed to update like"))
                    }
                } else {
                    Log.e("TOGGLE_LIKE", "Invalid homepage data state")
                    _uiEvent.emit(UiEvent("Failed to update like"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_LIKE", "Error toggling like: ${ex.message}")
                _uiEvent.emit(UiEvent("Error: ${ex.message ?: "Something went wrong"}"))
            }
        }
    }

    fun toggleDislike(slug: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentData = _homepageData.value
                if (currentData is Resource.Success) {
                    val carouselRow =
                        currentData.data?.find { it is HomePageItem.CarouselRow } as? HomePageItem.CarouselRow
                    if (carouselRow != null && carouselRow.dto.carouselItemsDto.isNotEmpty()) {
                        val carouselItem = carouselRow.dto.carouselItemsDto[0]
                        val isCurrentlyDisliked = carouselItem.isDisliked ?: false
                        val response = networkRepository.likeDisLikeContent(slug, type = "dislike")
                        if (response.isSuccessful) {
                            val newIsDisliked = !isCurrentlyDisliked
                            val newIsLiked =
                                if (newIsDisliked) false else carouselItem.isLiked ?: false
                            // Update the carousel item's isLiked and isDisliked
                            val updatedCarouselItem = carouselItem.copy(
                                isLiked = newIsLiked, isDisliked = newIsDisliked
                            )
                            val updatedCarouselRow = carouselRow.copy(
                                dto = carouselRow.dto.copy(
                                    carouselItemsDto = listOf(updatedCarouselItem)
                                )
                            )
                            // Update the homepage data with the new carousel row
                            val updatedItems = currentData.data?.map { item ->
                                if (item is HomePageItem.CarouselRow) updatedCarouselRow else item
                            }
                            _homepageData.emit(Resource.Success(updatedItems ?: emptyList()))
                            // Emit UI event for Toast
                            _uiEvent.emit(
                                UiEvent(
                                    if (newIsDisliked) "Disliked" else "Removed from Disliked"
                                )
                            )
                        } else {
                            Log.e(
                                "TOGGLE_DISLIKE", "Failed to toggle dislike: ${response.message()}"
                            )
                            _uiEvent.emit(UiEvent("Failed to update dislike"))
                        }
                    } else {
                        Log.e("TOGGLE_DISLIKE", "Carousel row not found or empty")
                        _uiEvent.emit(UiEvent("Failed to update dislike"))
                    }
                } else {
                    Log.e("TOGGLE_DISLIKE", "Invalid homepage data state")
                    _uiEvent.emit(UiEvent("Failed to update dislike"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("TOGGLE_DISLIKE", "Error toggling dislike: ${ex.message}")
                _uiEvent.emit(UiEvent("Error: ${ex.message ?: "Something went wrong"}"))
            }
        }
    }

    fun loadBannerDetail(slug: String) {
        viewModelScope.launch {
            try {
                val response = networkRepository.getGivenCardDetail(slug)
                if (response.isSuccessful) {
                    val cardDetail = response.body()
                    if (cardDetail != null) {
                        _carouselClickUiState.emit(
                            CarouselClickUiState.NavigateToPlayer(
                                cardDetail.toDetailDto().toPosterCardDto()
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("BANNER_DETAIL", "banner detail excepiton is ${ex.message}")
            }
        }
    }
}