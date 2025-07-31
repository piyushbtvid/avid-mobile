package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.epg.EpgUiModel
import com.faithForward.media.ui.universal_page.top_bar.TopBarItemDto
import com.faithForward.media.viewModel.uiModels.mapToEpgUiModel
import com.faithForward.media.viewModel.uiModels.mapToEpgUiModelWithSingleBroadcast
import com.faithForward.media.viewModel.uiModels.toCategoryComposeDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniversalViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    var topBarItems = mutableStateListOf<TopBarItemDto>()
        private set

    var liveVideo = mutableStateOf<List<String?>>(emptyList())
        private set


    private val _epgUiModel: MutableStateFlow<Resource<EpgUiModel>> =
        MutableStateFlow(Resource.Unspecified())
    val epgUiModel = _epgUiModel.asStateFlow()

    private val _guideUiModel: MutableStateFlow<Resource<EpgUiModel>> =
        MutableStateFlow(Resource.Unspecified())
    val guideEpgUiModel = _guideUiModel.asStateFlow()

    private val _categoryButtonList: MutableStateFlow<Resource<List<CategoryComposeDto>?>> =
        MutableStateFlow(Resource.Unspecified())
    val categoryButtonList = _categoryButtonList.asStateFlow()

    init {
        topBarItems.addAll(
            listOf(
                TopBarItemDto(
                    name = "STREAM", tag = "stream"
                ),
                TopBarItemDto(
                    name = "LIVE", tag = "live"
                ),
                TopBarItemDto(
                    name = "BROWSE", tag = "browse"
                )
            )
        )
        getEpgData()
    }


    fun getEpgData() {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            try {
                val response = networkRepository.getEpgData()
                if (response.isSuccessful) {
                    val epgData = response.body()
                    val firstUrl = epgData?.response?.categories
                        ?.flatMap { it.streamChannels }
                        ?.firstNotNullOfOrNull { it.sourceUrl }
                    liveVideo.value = listOf(firstUrl)

                    val categoryList = response.body()?.response?.categories
                    val epgUiModel = mapToEpgUiModelWithSingleBroadcast(categoryList)
                    val guideEpgModel = mapToEpgUiModel(categoryList)
                    val categoryButtonList = categoryList?.map {
                        it.toCategoryComposeDto()
                    }
                    _epgUiModel.emit(Resource.Success(epgUiModel))
                    _guideUiModel.emit(Resource.Success(guideEpgModel))
                    _categoryButtonList.emit(Resource.Success(categoryButtonList))
                } else {
                    Log.e("EPG", "epg error message is ${response.message()}")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("EPG", "epg error exception ${ex.message}")

            }


        }

    }


}