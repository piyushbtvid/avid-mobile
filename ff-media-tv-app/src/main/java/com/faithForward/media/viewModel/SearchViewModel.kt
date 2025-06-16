package com.faithForward.media.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.viewModel.uiModels.SearchEvent
import com.faithForward.media.viewModel.uiModels.SearchUiState
import com.faithForward.media.viewModel.uiModels.toSearchContentDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SubmitQuery -> searchGivenQuery(event.query)
        }
    }

    private fun searchGivenQuery(query: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(query = query, searchResults = Resource.Loading()) }
                val response = networkRepository.searchContent(query)
                if (response.isSuccessful) {
                    val body = response.body()
                    _uiState.update {
                        it.copy(
                            searchResults = when {
                                body == null || body.data.isEmpty() -> Resource.Error("No results found")
                                else -> Resource.Success(body.toSearchContentDto())
                            }
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(searchResults = Resource.Error("Error: ${response.code()}"))
                    }
                }
            } catch (ex: Exception) {
                _uiState.update {
                    it.copy(searchResults = Resource.Error(ex.message ?: "Unknown error"))
                }
            }
        }
    }

}