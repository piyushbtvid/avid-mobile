package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject constructor(private val networkRepository: NetworkRepository) : ViewModel() {

    private val _cardDetail: MutableStateFlow<Resource<DetailPageItem>> =
        MutableStateFlow(Resource.Unspecified())
    val cardDetail = _cardDetail.asStateFlow()

    fun getGivenCardDetail(id: String, relatedList: List<PosterCardDto>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.getGivenCardDetail(id)
                if (response.isSuccessful) {
                    Log.e("DETAIL", "card detail success is ${response.body()}")
                    val cardDetail = response.body()
                    if (cardDetail != null) {
                        _cardDetail.emit(
                            Resource.Success(
                                DetailPageItem.CardWithRelated(
                                    cardDetail.toDetailDto(),
                                    relatedList = relatedList
                                )
                            )
                        )
                    }
                } else {
                    Log.e("DETAIL", "card detail error is ${response.message()}")
                    _cardDetail.emit(Resource.Error(response.message()))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("DETAIL", "card detail exception is ${ex.message}")
                _cardDetail.emit(Resource.Error(ex.message ?: "something went wrong"))
            }
        }
    }


}