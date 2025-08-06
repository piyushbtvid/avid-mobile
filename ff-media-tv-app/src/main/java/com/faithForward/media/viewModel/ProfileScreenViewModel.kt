package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.user_profile.UserProfileUiItem
import com.faithForward.media.ui.user_profile.create_profile.AvatarItem
import com.faithForward.media.viewModel.uiModels.ProfileEvent
import com.faithForward.media.viewModel.uiModels.toAvatarUiItem
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

    private val _allAvatars: MutableStateFlow<Resource<List<AvatarItem>?>> =
        MutableStateFlow(Resource.Unspecified())
    val allAvatars = _allAvatars.asStateFlow()


    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.GetAllProfiles -> {
                getUserAllProfiles()
            }

            is ProfileEvent.GetAllAvatars -> {
                getAllAvatars()
            }
        }
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


    private fun getAllAvatars() {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            try {
                val response = networkRepository.getAllAvatars()

                if (response.isSuccessful) {
                    val data = response.body()?.data
                    val avatarList = data?.map {
                        it.toAvatarUiItem()
                    }
                    _allAvatars.emit(Resource.Success(avatarList))
                } else {
                    Log.e("Avatars", "error is ${response.message()}")
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

}