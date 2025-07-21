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
import com.faithForward.util.MyFavList
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
class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _homepageData: MutableStateFlow<Resource<List<HomePageItem>>> =
        MutableStateFlow(Resource.Unspecified())
    val homePageData: StateFlow<Resource<List<HomePageItem>>> = _homepageData

    private val _uiEvent = MutableSharedFlow<UiEvent?>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _carouselClickUiState = MutableSharedFlow<CarouselClickUiState>()
    val carouselClickUiState = _carouselClickUiState.asSharedFlow()

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set

    init {
        Log.e("HOME_VIEWMODEL", "home viewModel init called")
        // fetchHomePageData()
    }

    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    fun fetchHomePageData() {
        Log.e("REFRESH_TOKEN", "fetch home data called in home viewModel")
        viewModelScope.launch(Dispatchers.IO) {
            //     _homepageData.emit(Resource.Loading())
            try {
                // Fetch both APIs concurrently
                val sectionDataDeferred = async { networkRepository.getHomeSectionData() }
                val myListResponse = async { networkRepository.getMyListSectionData("my-list") }
                val likedListResponse = async { networkRepository.getLikedList() }
                val disLikedListResponse = async { networkRepository.getDisLikedList() }

                val sectionData = sectionDataDeferred.await()
                val myListData = myListResponse.await()
                val likedListData = likedListResponse.await()
                val disLikedData = disLikedListResponse.await()

                //Setting MyList and liked and disLiked List data

                if (myListData.isSuccessful && likedListData.isSuccessful && disLikedData.isSuccessful) {
                    MyFavList.myFavList = myListData.body()?.data
                    MyFavList.likedList = likedListData.body()?.data
                    MyFavList.disLikedList = disLikedData.body()?.data
                }

                Log.e("HOME_DATA", "home data in viewModel is ${sectionData.body()?.data}")

                // Process section data (Carousel and Poster rows)
                val sectionItems = if (sectionData.isSuccessful) {
                    sectionData.body()?.toHomePageItems() ?: listOf()
                } else {
                    listOf()
                }

                // Combine the data with CategoryRow at index 1
                val combinedItems = buildList {
                    // Add Carousel first (if it exists)
                    val carousel = sectionItems.find { it is HomePageItem.CarouselRow }
                    if (carousel != null) {
                        add(carousel)
                    }

                    // Add CategoryRow second (if it exists)
                    val category = sectionItems.find { it is HomePageItem.CategoryRow }
                    if (category != null) {
                        add(category)
                    }

                    // Add remaining items (PosterRows)
                    addAll(sectionItems.filter { it !is HomePageItem.CarouselRow && it !is HomePageItem.CategoryRow })
                }
                _homepageData.emit(Resource.Success(combinedItems))
            } catch (ex: Exception) {
                ex.printStackTrace()
                _homepageData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
                _uiEvent.emit(UiEvent("Error: ${ex.message ?: "Something went wrong"}"))
            }
        }
    }

    fun loadDetailForUrl(
        slug: String,
        progress: Long = 0,
        isFromContinueWatching: Boolean,
    ) {
        viewModelScope.launch {
            try {
                Log.e(
                    "CONTINUE_WATCHING_CLICK",
                    "load Detail for Url called in homeViewModel with $slug $progress $isFromContinueWatching"
                )
                val response = networkRepository.getGivenCardDetail(slug)
                if (response.isSuccessful) {
                    val cardDetail = response.body()
                    if (cardDetail != null) {
                        var detailPosterCardDto = cardDetail.toDetailDto().toPosterCardDto()
                        Log.e(
                            "IS_FROM_CONTINUE",
                            " slugs are series ${cardDetail.data.seriesSlug}  slug ${cardDetail.data.slug}"
                        )
                        if (isFromContinueWatching) {
                            detailPosterCardDto = detailPosterCardDto.copy(
                                slug = detailPosterCardDto.slug,
                                progress = progress,
                                seriesSlug = cardDetail.data.seriesSlug

                            )
                            Log.e(
                                "CONTINUE_WATCHING_CLICK",
                                "load Detail response for continue watching response is $detailPosterCardDto"
                            )
                        }
                        _carouselClickUiState.emit(
                            CarouselClickUiState.NavigateToPlayer(
                                posterCardDto = detailPosterCardDto,
                                isFromContinueWatching = isFromContinueWatching
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
}