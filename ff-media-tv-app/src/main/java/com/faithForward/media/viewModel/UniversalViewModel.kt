package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.top_bar.TopBarItemDto
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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