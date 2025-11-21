package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.signup.SignupEvent
import com.faithForward.media.ui.signup.SignupState
import com.faithForward.network.dto.login.ErrorResponse
import com.faithForward.network.dto.login.LoginData
import com.faithForward.preferences.UserPrefData
import com.faithForward.repository.NetworkRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _signupState = MutableStateFlow(SignupState())
    val signupState: StateFlow<SignupState> = _signupState.asStateFlow()

    private val _isSignedUp = MutableStateFlow(false)
    val isSignedUp: StateFlow<Boolean> = _isSignedUp.asStateFlow()

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.NameChanged -> {
                _signupState.update { it.copy(name = event.name, errorMessage = null) }
            }

            is SignupEvent.EmailChanged -> {
                _signupState.update { it.copy(email = event.email, errorMessage = null) }
            }

            is SignupEvent.PasswordChanged -> {
                _signupState.update { it.copy(password = event.password, errorMessage = null) }
            }

            is SignupEvent.SubmitSignup -> {
                signupUser(
                    deviceType = event.deviceType, deviceId = event.deviceId
                )
            }

            is SignupEvent.ResetIsSignedUp -> {
                _signupState.value = _signupState.value.copy(
                    isSignedUp = event.boolean
                )
            }
        }
    }

    private fun signupUser(
        deviceId: String,
        deviceType: String,
    ) {
        val state = _signupState.value

        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _signupState.update {
                it.copy(errorMessage = "Name, Email and Password cannot be empty")
            }
            return
        }

        viewModelScope.launch {
            _signupState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = networkRepository.signupUser(
                    name = state.name,
                    email = state.email,
                    password = state.password,
                    deviceType = deviceType,
                    deviceId = deviceId
                )

                if (response.isSuccessful) {
                    val signupData = response.body()
                    Log.e("SIGNUP_SUCCESS", "Success: ${signupData?.message}")

                    if (signupData != null) {
                        Log.e("SIGNUP_SUCCESS", "Signup data is not empty with $signupData")
                        if (!signupData.data.token.isNullOrEmpty() && 
                            !signupData.data.refreshToken.isNullOrEmpty() && 
                            !signupData.data.tokenType.isNullOrEmpty()) {
                            
                            // Convert SignupData to LoginData for UserPrefData
                            val loginData = LoginData(
                                user = signupData.data.user,
                                token = signupData.data.token,
                                refreshToken = signupData.data.refreshToken,
                                tokenType = signupData.data.tokenType,
                                expire_date = signupData.data.expireDate ?: 0L,
                                activation_status = null
                            )

                            Log.e("SIGNUP_SUCCESS", "Saving user session after signup")
                            withContext(Dispatchers.IO) {
                                networkRepository.saveUserSession(
                                    UserPrefData(
                                        season = loginData,
                                        deviceID = deviceId,
                                        deviceType = deviceType
                                    )
                                )
                            }
                            _signupState.update {
                                it.copy(isLoading = false, isSignedUp = true)
                            }
                            _signupState.update {
                                it.copy(
                                    name = "", email = "", password = ""
                                )
                            }
                        }
                    }
                } else {
                    val errorBody = response.errorBody()
                    val gson = Gson()
                    val error = errorBody?.charStream()?.let {
                        gson.fromJson(it, ErrorResponse::class.java)
                    }

                    val combinedErrorMessage = buildString {
                        error?.errors?.forEach { (field, messages) ->
                            messages.forEach { msg ->
                                appendLine("$field: $msg")
                            }
                        }
                    }

                    Log.e("SIGNUP_ERROR", "Error: $combinedErrorMessage")

                    _signupState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = combinedErrorMessage.ifBlank { "Signup failed. Please try again." })
                    }
                }
            } catch (e: Exception) {
                Log.e("SIGNUP_ERROR", "Exception: ${e.message}")
                _signupState.update {
                    it.copy(
                        isLoading = false, errorMessage = "Network error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}

