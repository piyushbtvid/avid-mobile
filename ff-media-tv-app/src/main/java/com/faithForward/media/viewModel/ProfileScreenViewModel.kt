package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.user_profile.UserProfileUiItem
import com.faithForward.media.viewModel.uiModels.toUserProfileUiItem
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {


    private val _allProfiles: MutableStateFlow<Resource<List<UserProfileUiItem>?>> =
        MutableStateFlow(Resource.Unspecified())
    val allProfiles = _allProfiles.asStateFlow()

    init {
        getUserAllProfiles()
    }


    private fun getUserAllProfiles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.getAllProfiles()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    val userProfileList = data?.map {
                        it.toUserProfileUiItem()
                    }
                    _allProfiles.emit(Resource.Success(userProfileList))
                } else {
                    Log.e("ALL_PROFILE", "all profiles response error is ${response.message()}")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("exception", "ex is ${ex.message}")
            }

        }

    }

}