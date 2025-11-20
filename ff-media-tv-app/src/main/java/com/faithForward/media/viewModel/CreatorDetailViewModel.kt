package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.detail.creator_detail.content.ContentDto
import com.faithForward.media.viewModel.uiModels.CreatorDetailItem
import com.faithForward.media.viewModel.uiModels.toContentDto
import com.faithForward.media.viewModel.uiModels.toCreatorDetailDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatorDetailViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
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

    val id: Int? = savedStateHandle["creatorId"]

    init {
        id?.let {
            fetchCreatorDetail(id)
        }
    }

    private fun fetchCreatorDetail(id: Int) {
        viewModelScope.launch {
            _creatorDetailPageData.emit(Resource.Loading())
            try {
                val creatorResponse = async { networkRepository.getCreatorDetail(id) }
                val creatorContentList = async { networkRepository.getCreatorContentList(id) }

                val creatorDetailResponse = creatorResponse.await()
                val creatorContentListResponse = creatorContentList.await()

                val contentList = if (creatorContentListResponse.isSuccessful) {
                    creatorContentListResponse.body()?.data?.map { it.toContentDto() }.orEmpty()
                } else {
                    emptyList()
                }

                if (creatorDetailResponse.isSuccessful) {
                    val creatorDetail = creatorDetailResponse.body()?.toCreatorDetailDto()
                    val combined = creatorDetail?.let {
                        CreatorDetailItem.CreatorDetail(
                            creatorDetailDto = it,
                            contentList = contentList
                        )
                    }

                    _creatorDetailPageData.emit(Resource.Success(combined))
                    Log.e(
                        "CREATOR_DETAIL",
                        "creator detail Success: ${creatorDetailResponse.body()}"
                    )
                } else {
                    Log.e(
                        "CREATOR_DETAIL",
                        "creator detail error: ${creatorDetailResponse.message()}"
                    )
                    _creatorDetailPageData.emit(Resource.Error(creatorDetailResponse.message()))
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("CREATOR_DETAIL", "Exception: ${ex.message}")
                _creatorDetailPageData.emit(Resource.Error(ex.message ?: "Unknown error"))
            }
        }
    }


}