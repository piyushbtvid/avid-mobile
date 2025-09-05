package com.faithForward.media.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.sections.common_ui.carousel.CarouselContentRowDto
import com.faithForward.media.ui.sections.creator.card.CreatorCardDto
import com.faithForward.media.viewModel.uiModels.HomePageItem
import com.faithForward.media.viewModel.uiModels.toCarouselItemDto
import com.faithForward.media.viewModel.uiModels.toCreatorCardDtoList
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatorViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _creatorPageData: MutableStateFlow<Resource<List<HomePageItem>>> =
        MutableStateFlow(Resource.Unspecified())
    val creatorPageData: StateFlow<Resource<List<HomePageItem>>> = _creatorPageData.asStateFlow()

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set


    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    init {
        fetchCreatorData(1)
    }

    fun fetchCreatorData(sectionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _creatorPageData.emit(Resource.Loading())
            try {
                // Fetch both APIs concurrently
                //   val sectionDataDeferred = async { networkRepository.getGivenSectionData(sectionId) }
                val creatorsDataDeferred = async { networkRepository.getCreatorsList() }

                //   val sectionData = sectionDataDeferred.await()
                val creatorsData = creatorsDataDeferred.await()


                // Process category data
                val creatorRow = if (creatorsData.isSuccessful) {
                    creatorsData.body()?.data?.toCreatorCardDtoList()
                } else {
                    listOf()
                }

                // Combine the data with CategoryRow at index 1
                val combinedItems: List<HomePageItem> = buildList {

                    val creatorCarousel = creatorRow?.firstOrNull()

                    if (creatorCarousel != null) {
                        val carouselRow = listOf(creatorCarousel.toCarouselItemDto())
                        add(HomePageItem.CarouselRow(CarouselContentRowDto(carouselRow)))
                    }


                    //add creator Grid
                    if (!creatorRow.isNullOrEmpty()) {
                        val newCreatorRow = creatorRow.toMutableList().apply { removeAt(0) }
                        add(HomePageItem.CreatorGrid(newCreatorRow))
                    }
                }

                _creatorPageData.emit(Resource.Success(combinedItems))
            } catch (ex: Exception) {
                ex.printStackTrace()
                _creatorPageData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }


    private val sampleCreatorCards by lazy {
        List(22) {
            CreatorCardDto(
                creatorSubscriberText = "11M Subscriber",
                creatorName = "Virat Khohli",
                creatorImageUrl = "https://rukminim2.flixcart.com/image/850/1000/l22724w0/poster/a/x/o/small-virat-kohli-multicolour-photo-paper-print-poster-virat-original-imagdhycghmdyr3j.jpeg?q=90&crop=false",
                channelDescription = "hbs,hbshs",
                id = 1
            )
        }
    }

}