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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _homepageData: MutableStateFlow<Resource<List<HomePageItem>>> =
        MutableStateFlow(Resource.Unspecified())
    val homePageData: StateFlow<Resource<List<HomePageItem>>> = _homepageData

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set


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
}