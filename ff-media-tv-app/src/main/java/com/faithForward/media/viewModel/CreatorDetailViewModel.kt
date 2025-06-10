package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.viewModel.uiModels.CreatorDetailItem
import com.faithForward.media.viewModel.uiModels.HomePageItem
import com.faithForward.media.viewModel.uiModels.toCreatorDetailDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatorDetailViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _creatorDetailPageData: MutableStateFlow<Resource<CreatorDetailItem?>> =
        MutableStateFlow(Resource.Unspecified())
    val creatorDetailPageData = _creatorDetailPageData.asStateFlow()

    var contentRowFocusedIndex by mutableStateOf(-1)
        private set


    fun onContentRowFocusedIndexChange(value: Int) {
        contentRowFocusedIndex = value
    }

    init {

    }

    fun fetchCreatorDetail(id: Int) {
        viewModelScope.launch {
            _creatorDetailPageData.emit(Resource.Loading())
            try {
                val creatorResponse = networkRepository.getCreatorDetail(id)
                if (creatorResponse.isSuccessful) {
                    val creatorDetail = creatorResponse.body()
                    _creatorDetailPageData.emit(Resource.Success(creatorDetail?.toCreatorDetailDto()
                        ?.let {
                            CreatorDetailItem.CreatorDetail(
                                it
                            )
                        }))
                    Log.e(
                        "CREATOR_DETAIL",
                        "creator detail Success in viewModel is ${creatorResponse.body()}"
                    )
                } else {
                    Log.e(
                        "CREATOR_DETAIL",
                        "creator detail error in viewModel is ${creatorResponse.message()}"
                    )
                    _creatorDetailPageData.emit(Resource.Error(creatorResponse.message()))
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e(
                    "CREATOR_DETAIL", "creator detail error in viewModel is ${ex.message}"
                )
            }
        }
    }

}