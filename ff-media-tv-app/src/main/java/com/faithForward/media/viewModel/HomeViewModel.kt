package com.faithForward.media.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.network.dto.SectionApiResponse
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _sectionData: MutableStateFlow<Resource<SectionApiResponse?>> =
        MutableStateFlow(Resource.Unspecified())
    val sectionData = _sectionData.asStateFlow()


    fun getGivenSectionData(sectionId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _sectionData.emit(Resource.Loading())
            try {
                val data = networkRepository.getGivenSectionData(sectionId)
                if (data.isSuccessful) {
                    _sectionData.emit(Resource.Success(data.body()))
                } else {
                    _sectionData.emit(Resource.Error(data.message()))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _sectionData.emit(Resource.Error(ex.message ?: "Something went wrong!"))
            }
        }
    }

}