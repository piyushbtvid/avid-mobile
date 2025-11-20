package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.sections.genre.GenreGridDto
import com.faithForward.media.viewModel.uiModels.toGenreCardGridDto
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: NetworkRepository,
) : ViewModel() {

    private val _genreData: MutableStateFlow<Resource<GenreGridDto?>> =
        MutableStateFlow(Resource.Unspecified())
    val genreData = _genreData.asStateFlow()

    val id: String? = savedStateHandle["genreId"]

    init {
        id?.let {
            getGivenGenreDetail(id)
        }
    }

    private fun getGivenGenreDetail(
        id: String,
    ) {
        viewModelScope.launch {
            _genreData.emit(Resource.Loading())
            try {
                val response = repository.getGivenGenreData(id)
                if (response.isSuccessful) {
                    Log.e("GENRE", "response when success is ${response.body()}")
                    _genreData.emit(Resource.Success(response.body()?.toGenreCardGridDto()))
                } else {
                    Log.e("GENRE", "response when error is ${response.message()}")
                    _genreData.emit(Resource.Error(response.message()))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("GENRE", "exception is ${ex.message}")
                _genreData.emit(Resource.Error(ex.message ?: "Something went wrong"))
            }

        }
    }

}