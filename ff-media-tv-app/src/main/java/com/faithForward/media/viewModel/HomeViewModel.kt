package com.faithForward.media.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.R
import com.faithForward.media.sidebar.SideBarItem
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.Item
import com.faithForward.network.dto.SectionApiResponse
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _sectionData: MutableStateFlow<Resource<SectionApiResponse?>> =
        MutableStateFlow(Resource.Unspecified())
    val sectionData = _sectionData.asStateFlow()

    private val _carouselList: MutableStateFlow<List<Item>> = MutableStateFlow(emptyList())
    val carouselList = _carouselList.asStateFlow()

    private val _categoriesList: MutableStateFlow<Resource<CategoryResponse?>> =
        MutableStateFlow(Resource.Unspecified())
    val categoriesList = _categoriesList.asStateFlow()

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set

    var sideBarItems = mutableStateListOf<SideBarItem>()
        private set

    init {
        sideBarItems.addAll(
            listOf(
                SideBarItem("Search", R.drawable.search_ic, "search"),
                SideBarItem("Home", R.drawable.home_ic, "home"),
                SideBarItem("MyList", R.drawable.plus_ic, "myList"),
                SideBarItem("Creators", R.drawable.group_person_ic, "creators"),
                SideBarItem("Series", R.drawable.screen_ic, "series"),
                SideBarItem("Movies", R.drawable.film_ic, "movie"),
                SideBarItem("Tithe", R.drawable.fi_rs_hand_holding_heart, "tithe"),
            )
        )
    }


    fun getGivenSectionData(sectionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _sectionData.emit(Resource.Loading())
            try {
                val data = networkRepository.getGivenSectionData(sectionId)
                if (data.isSuccessful) {
                    _sectionData.emit(Resource.Success(data.body()))
                    val allSections = data.body()?.data.orEmpty()
                    val carouselSections = allSections.filter { it.type == "Carousel" }
                    _carouselList.emit(carouselSections[0].items)
                } else {
                    _sectionData.emit(Resource.Error(data.message()))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _sectionData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }

    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    fun getCategoriesList() {
        viewModelScope.launch(Dispatchers.IO) {
            _categoriesList.emit(Resource.Loading())
            try {
                val data = networkRepository.getCategories()
                if (data.isSuccessful) {
                    _categoriesList.emit(Resource.Success(data.body()))
                } else {
                    _categoriesList.emit(Resource.Error(data.message()))
                }
            } catch (ex: Exception) {
                _categoriesList.emit(Resource.Error(ex.message ?: "something went wrong"))
            }
        }
    }


}