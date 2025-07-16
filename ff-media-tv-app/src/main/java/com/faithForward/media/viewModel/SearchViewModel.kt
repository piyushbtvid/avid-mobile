package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.sections.search.SearchScreenUi
import com.faithForward.media.viewModel.uiModels.SearchEvent
import com.faithForward.media.viewModel.uiModels.SearchScreenUiState
import com.faithForward.media.viewModel.uiModels.SearchUiState
import com.faithForward.media.viewModel.uiModels.toSearchContentDto
import com.faithForward.media.viewModel.uiModels.toSearchUiDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchUiState = MutableStateFlow(SearchScreenUiState())
    val searchUiState = _searchUiState.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SubmitQuery -> searchGivenQuery(event.query)
        }
    }

    private fun searchGivenQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { it.copy(query = query, searchResults = Resource.Loading()) }
                _searchUiState.update {
                    it.copy(isLoading = true)
                }
                val response = async { networkRepository.searchContent(query) }
                val recentResponse = async { networkRepository.getRecentSearchContent() }
                val result = response.await()
                val recentResult = recentResponse.await()
                Log.e("SEARCH_RESULT", "search response in viewModel is $response")
                if (result.isSuccessful && recentResult.isSuccessful) {
                    val body = result.body()
                    val recentSearchList = recentResult.body()?.data?.map {
                        it.term
                    }
                    Log.e("SEARCH_RESULT", "search result in viewModel is ${body?.data}")
                    _uiState.update {
                        it.copy(
                            searchResults = when {
                                body == null || body.data.isEmpty() -> Resource.Error("No results found")
                                else -> Resource.Success(body.toSearchContentDto())
                            }
                        )
                    }
                    _searchUiState.update {
                        it.copy(
                            isLoading = false,
                            result = body?.toSearchUiDto(),
                            recentSearch = recentSearchList
                        )
                    }

                } else {
                    Log.e("SEARCH_RESULT", "search error in viewModel is ${result.message()}")
                    _uiState.update {
                        it.copy(searchResults = Resource.Error("Error: ${result.code()}"))
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("SEARCH_RESULT", "search exception in viewModel is ${ex.message}")
                _uiState.update {
                    it.copy(searchResults = Resource.Error(ex.message ?: "Unknown error"))
                }
            }
        }
    }

}