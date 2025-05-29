package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {


    fun fetchSeriesDetail(seriesID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = networkRepository.getSingleSeriesDetail(
                    seriesID
                )
                if (data.isSuccessful) {
                    Log.e("SERIES", "series detail when success is ${data.body()}")
                } else {
                    Log.e("SERIES", "series detail when Error is ${data.message()}")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("SERIES", "exception in series ViewModel is ${ex.message}")
            }
        }
    }

}