package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.viewModel.uiModels.HomePageItem
import com.faithForward.media.viewModel.uiModels.toHomePageItems
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
class ContentViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _homepageData: MutableStateFlow<Resource<List<HomePageItem>>> =
        MutableStateFlow(Resource.Unspecified())
    val homePageData: StateFlow<Resource<List<HomePageItem>>> = _homepageData

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set

    init {
        loadSectionContent(sectionId = "movies")
    }

    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    private fun loadSectionContent(sectionId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _homepageData.emit(Resource.Loading())
            try {
                // Fetch both APIs concurrently
                val sectionDataDeferred = async { networkRepository.getGivenSectionData(sectionId) }

                val sectionData = sectionDataDeferred.await()

                // Process section data (Carousel and Poster rows)
                val sectionItems = if (sectionData.isSuccessful) {
                    Log.e("MOVIES", "movies response is success with ${sectionData.body()}")
                    sectionData.body()?.toHomePageItems() ?: listOf()
                } else {
                    Log.e("MOVIES", "movies response is error with ${sectionData.message()}")
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
                Log.e("MOVIES", "movies response is exception with ${ex.message}")
                _homepageData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }

}