package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _cardDetail = MutableStateFlow<Resource<DetailPageItem>>(Resource.Unspecified())
    val cardDetail = _cardDetail.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun handleEvent(event: DetailScreenEvent) {
        when (event) {
            is DetailScreenEvent.LoadCardDetail -> loadCardDetail(event.id, event.relatedList)
            is DetailScreenEvent.RelatedRowFocusChanged -> updateFocusState(event.hasFocus)
            is DetailScreenEvent.RelatedRowUpClick -> handleRelatedRowUpClick()
        }
    }

    private fun loadCardDetail(id: String, relatedList: List<PosterCardDto>) {
        viewModelScope.launch(Dispatchers.IO) {
            _cardDetail.value = Resource.Loading()
            try {
                val response = networkRepository.getGivenCardDetail(id)
                if (response.isSuccessful) {
                    Log.e("DETAIL", "card detail success is ${response.body()}")
                    val cardDetail = response.body()
                    if (cardDetail != null) {
                        _cardDetail.emit(
                            Resource.Success(
                                DetailPageItem.CardWithRelated(
                                    detailDto = cardDetail.toDetailDto(),
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
                _cardDetail.emit(Resource.Error(ex.message ?: "Something went wrong"))
            }
        }
    }

    private fun updateFocusState(hasFocus: Boolean) {
        _uiState.value = _uiState.value.copy(
            targetHeight = if (hasFocus) Int.MAX_VALUE else 250,
            contentColor = if (hasFocus) Color.Transparent else Color.Black,
            buttonUnfocusedColor = if (hasFocus) Color.Transparent else Color.White,
            textUnfocusedColor = if (hasFocus) Color.Transparent else textUnFocusColor,
            contentRowTint = if (hasFocus) Color.Transparent else Color.White,
            relatedContentColor = if (hasFocus) Color.White else Color.Transparent
        )
    }

    private fun handleRelatedRowUpClick() {
        _uiState.value = _uiState.value.copy(
            targetHeight = 250,
            contentColor = Color.Black,
            buttonUnfocusedColor = Color.White,
            textUnfocusedColor = textUnFocusColor,
            contentRowTint = Color.White,
            relatedContentColor = Color.Transparent
        )
    }
}