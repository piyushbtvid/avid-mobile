package com.faithForward.media.viewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.home.creator.card.CreatorCardDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _homepageData: MutableStateFlow<Resource<List<HomePageItem>>> =
        MutableStateFlow(Resource.Unspecified())
    val homePageData: StateFlow<Resource<List<HomePageItem>>> = _homepageData

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set


    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    fun fetchHomePageData(sectionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _homepageData.emit(Resource.Loading())
            try {
                // Fetch both APIs concurrently
                val sectionDataDeferred = async { networkRepository.getGivenSectionData(sectionId) }
                val categoriesDataDeferred = async { networkRepository.getCategories() }

                val sectionData = sectionDataDeferred.await()
                val categoriesData = categoriesDataDeferred.await()

                // Process section data (Carousel and Poster rows)
                val sectionItems = if (sectionData.isSuccessful) {
                    sectionData.body()?.toHomePageItems() ?: listOf()
                } else {
                    listOf()
                }

                // Process category data
                val categoryRow = if (categoriesData.isSuccessful) {
                    categoriesData.body()?.toCategoryRow()
                } else {
                    null
                }

                // Combine the data with CategoryRow at index 1
                val combinedItems = buildList {
                    // Add Carousel first (if it exists)
                    val carousel = sectionItems.find { it is HomePageItem.CarouselRow }
                    if (carousel != null) {
                        add(carousel)
                    }

                    // Add CategoryRow second (if it exists)
                    if (categoryRow != null) {
                        add(categoryRow)
                    }

                    // Add remaining items (PosterRows)
                    addAll(sectionItems.filter { it !is HomePageItem.CarouselRow })
                }
                _homepageData.emit(Resource.Success(combinedItems))
            } catch (ex: Exception) {
                ex.printStackTrace()
                _homepageData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }

    fun fetchCreatorData(sectionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _homepageData.emit(Resource.Loading())
            try {
                // Fetch both APIs concurrently
                val sectionDataDeferred = async { networkRepository.getGivenSectionData(sectionId) }
                val categoriesDataDeferred = async { networkRepository.getCategories() }

                val sectionData = sectionDataDeferred.await()
                val categoriesData = categoriesDataDeferred.await()

                // Process section data (Carousel and Poster rows)
                val sectionItems = if (sectionData.isSuccessful) {
                    sectionData.body()?.toHomePageItems() ?: listOf()
                } else {
                    listOf()
                }

                // Process category data
                val categoryRow = if (categoriesData.isSuccessful) {
                    categoriesData.body()?.toCategoryRow()
                } else {
                    null
                }

                // Combine the data with CategoryRow at index 1
                val combinedItems: List<HomePageItem> = buildList {
                    // Add Carousel first (if it exists)
                    val carousel = sectionItems.find { it is HomePageItem.CarouselRow }
                    if (carousel != null) {
                        add(carousel)
                    }

                    // Add CategoryRow second (if it exists)
//                    if (categoryRow != null) {
//                        add(categoryRow)
//                    }

                    // Add remaining items (PosterRows)
//                    addAll(sectionItems.filter { it !is HomePageItem.CarouselRow })


                    add(HomePageItem.CreatorGrid(sampleCreatorCards))
                }

                _homepageData.emit(Resource.Success(combinedItems))
            } catch (ex: Exception) {
                ex.printStackTrace()
                _homepageData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }

    private val sampleCreatorCards = List(20) {
        CreatorCardDto(
            creatorSubscriberText = "11M Subscriber",
            creatorName = "Virat Khohli",
            creatorImageUrl = "https://rukminim2.flixcart.com/image/850/1000/l22724w0/poster/a/x/o/small-virat-kohli-multicolour-photo-paper-print-poster-virat-original-imagdhycghmdyr3j.jpeg?q=90&crop=false"
        )
    }
}