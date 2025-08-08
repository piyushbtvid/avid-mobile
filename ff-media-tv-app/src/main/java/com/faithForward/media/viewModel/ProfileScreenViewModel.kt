package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.user_profile.UserProfileUiItem
import com.faithForward.media.ui.user_profile.comman.AvatarItem
import com.faithForward.media.viewModel.uiModels.ProfileEvent
import com.faithForward.media.viewModel.uiModels.UiEvent
import com.faithForward.media.viewModel.uiModels.toAvatarUiItem
import com.faithForward.media.viewModel.uiModels.toUserProfileUiItem
import com.faithForward.repository.NetworkRepository
import com.faithForward.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
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

    private val _uiEvent = MutableSharedFlow<UiEvent?>(
    )
    val uiEvent = _uiEvent.asSharedFlow()


    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.GetAllProfiles -> {
                getUserAllProfiles()
            }

            is ProfileEvent.GetAllAvatars -> {
                getAllAvatars()
            }

            is ProfileEvent.CreateProfile -> {
                createProfile(
                    name = event.name,
                    avatarId = event.avatarId
                )
            }

            is ProfileEvent.UpdateProfile -> {
                updateProfile(
                    name = event.name,
                    avatarId = event.avatarId,
                    profileId = event.profileId
                )
            }

            is ProfileEvent.DeleteProfile -> {
                deleteProfile(
                    name = event.name,
                    avatarId = event.avatarId,
                    profileId = event.profileId
                )
            }

            is ProfileEvent.SetProfile -> {
                setProfile(
                    profileId = event.profileId
                )
            }
        }
    }


    fun resetUiEvent() {
        viewModelScope.launch {
            _uiEvent.emit(null)
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

    private fun createProfile(
        name: String,
        avatarId: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.createUserProfile(
                    userName = name,
                    avatarId = avatarId
                )
                if (response.isSuccessful) {
                    _uiEvent.emit(UiEvent(response.body()?.message ?: ""))
                } else {
                    val errorMessage = parseErrorMessage(response)
                    _uiEvent.emit(UiEvent(errorMessage))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                _uiEvent.emit(UiEvent("Something went wrong!"))
            }
        }
    }


    private fun updateProfile(
        profileId: Int,
        name: String,
        avatarId: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.updateProfile(
                    profileId = profileId,
                    userName = name,
                    avatarId = avatarId
                )

                if (response.isSuccessful) {
                    _uiEvent.emit(UiEvent(response.body()?.message ?: ""))
                } else {
                    val errorMessage = parseErrorMessage(response)
                    _uiEvent.emit(UiEvent(errorMessage))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


    private fun deleteProfile(
        profileId: Int,
        name: String,
        avatarId: Int,
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = networkRepository.deleteProfile(
                    profileId = profileId,
                    userName = name,
                    avatarId = avatarId
                )

                if (response.isSuccessful) {
                    _uiEvent.emit(UiEvent(response.body()?.message ?: ""))
                } else {
                    val errorMessage = parseErrorMessage(response)
                    _uiEvent.emit(UiEvent(errorMessage))
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }

    private fun setProfile(
        profileId: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = networkRepository.setProfile(
                    profileId = profileId
                )

                if (response.isSuccessful) {
                    _uiEvent.emit(UiEvent(response.body()?.message ?: ""))
                } else {
                    val errorMessage = parseErrorMessage(response)
                    _uiEvent.emit(UiEvent(errorMessage))
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }


    private fun parseErrorMessage(response: Response<*>): String {
        return try {
            val errorJson = response.errorBody()?.string()
            if (errorJson != null) {
                val jsonObject = JSONObject(errorJson)
                jsonObject.getString("message")
            } else {
                "Unknown error"
            }
        } catch (e: Exception) {
            "SomeThing went wrong!"
        }
    }


}