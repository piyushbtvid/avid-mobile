package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.sections.my_account.WatchSectionUiModel
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
        onEvent(MyAccountEvent.GetCurrentUser)
    }

    fun onEvent(myAccountEvent: MyAccountEvent) {

        when (myAccountEvent) {

            is MyAccountEvent.GetContinueWatching -> {
                getContinueWatchingList()
            }

            is MyAccountEvent.GetMyList -> {
                getMyList()
            }

            is MyAccountEvent.GetCurrentUser -> {
                getCurrentUserInfo()
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
                    val newSection = WatchSectionUiModel(
                        title = "Continue Watching",
                        items = watchSectionList
                    )

                    _uiState.update {
                        it.copy(
                            continueWatchSections = newSection,
                            myListWatchSections = null
                        )
                    }
                } else {

                }
            } catch (ex: Exception) {
                ex.printStackTrace()

            }
        }
    }

    private fun getMyList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val myListResponse = networkRepository.getMyListSectionData("my-list")

                if (myListResponse.isSuccessful) {

                    val dataList = myListResponse.body()?.data

                    val watchSectionList = dataList?.flatMap { section ->
                        section.content.map { result ->
                            result.toWatchSectionItem()
                        }
                    }

                    val newSection = WatchSectionUiModel(
                        title = "My List",
                        items = watchSectionList
                    )

                    _uiState.update {
                        it.copy(
                            myListWatchSections = newSection,
                            continueWatchSections = null
                        )
                    }

                } else {
                    Log.e("MY_ACCOUNT", "my list not success with ${myListResponse.message()}")
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("EX", "ex is ${ex.message}")
            }
        }
    }

    private fun getCurrentUserInfo() {
        viewModelScope.launch {
            val response = networkRepository.getCurrentSession()
            if (response != null) {
                val initials = getInitials(response.season.user?.name)
                _uiState.update {
                    it.copy(
                        currentUserEmail = response.season.user?.email ?: "",
                        currentUserName = response.season.user?.name ?: "",
                        initialName = initials
                    )
                }
            }
        }
    }

    private fun getInitials(name: String?): String {
        return name
            ?.trim()
            ?.split("\\s+".toRegex()) // Split by whitespace
            ?.mapNotNull { it.firstOrNull()?.uppercaseChar() }
            ?.joinToString("") ?: ""
    }


}