package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: NetworkRepository
) : ViewModel() {


    fun getGivenGenreDetail(
        id: String
    ) {
        viewModelScope.launch {

            try {
                val response = repository.getGivenItemDetail(id)
                if (response.isSuccessful) {
                    Log.e("GENRE", "response when success is ${response.body()}")
                } else {
                    Log.e("GENRE", "response when error is ${response.message()}")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("GENRE", "exception is ${ex.message}")
            }

        }
    }

}