package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {


    fun searchGivenQuery(
        query: String,
    ) {
        viewModelScope.launch {
            try {
                val response = networkRepository.searchContent(query)
                if (response.isSuccessful) {
                    Log.e("SEARCH_CONTENT", " search content in viewModel is ${response.body()}")
                } else {
                    Log.e("SEARCH_CONTENT", " search error in viewModel is ${response.message()}")

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("SEARCH_CONTENT", "search exception in viewModel is ${ex.message}")
            }
        }
    }

}