package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.viewModel.uiModels.MyAccountEvent
import com.faithForward.media.viewModel.uiModels.MyAccountUiState
import com.faithForward.media.viewModel.uiModels.toWatchSectionItem
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyAccountUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(MyAccountEvent.GetContinueWatching)
    }

    fun onEvent(myAccountEvent: MyAccountEvent) {

        when (myAccountEvent) {

            is MyAccountEvent.GetContinueWatching -> {
                getContinueWatchingList()
            }

            is MyAccountEvent.GetMyList -> {

            }

        }

    }

    private fun getContinueWatchingList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.getContinueWatchingList()
                if (response.isSuccessful) {
                    val continueWatchingList = response.body()?.data
                    Log.e(
                        "GET_CONTINUE_WATCHING",
                        "continue watching list in my account is $continueWatchingList"
                    )
                    val watchSectionList = continueWatchingList?.map {
                        it.toWatchSectionItem()
                    }
                    _uiState.update {
                        it.copy(
                            myWatchSectionItemDtoList = watchSectionList
                        )
                    }
                } else {

                }
            } catch (ex: Exception) {
                ex.printStackTrace()

            }
        }
    }

}