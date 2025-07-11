package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.login.LoginEvent
import com.faithForward.media.login.LoginState
import com.faithForward.network.dto.login.ErrorResponse
import com.faithForward.preferences.UserPrefData
import com.faithForward.repository.NetworkRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        Log.e("CHECK_LOGIN", "login viewModel init called")
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    Log.e("GOING_TO_HOME", "checkLogin status called in Login VieWModel")
                    Log.e("CHECK_LOGIN", "check login status in viewModel called")
                    val session = networkRepository.getCurrentSession()
                    Log.e(
                        "CHECK_LOGIN",
                        "Initial session check in checkLoginStatus is called: $session"
                    )
                    _isLoggedIn.value = session?.season?.token != null
                    Log.e(
                        "GOING_TO_HOME",
                        "isLoged in value in checkLoginStatus is ${_isLoggedIn.value} and sesson is $session"
                    )
                    delay(200)
                    _loginState.update { it.copy(isCheckingLoginStatus = false) } // Mark check as complete
                } catch (ex: Exception) {
                    Log.e("CHECK_LOGIN", "exception is ${ex.printStackTrace()}")
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginState.update { it.copy(email = event.email, errorMessage = null) }
            }

            is LoginEvent.PasswordChanged -> {
                _loginState.update { it.copy(password = event.password, errorMessage = null) }
            }

            is LoginEvent.SubmitLogin -> {
                loginUser(
                    deviceType = event.deviceType,
                    deviceId = event.deviceId
                )
            }

            is LoginEvent.ResetIsLogin -> {
                _loginState.value = _loginState.value.copy(
                    isLoggedIn = event.boolean
                )
            }
        }
    }

    private fun loginUser(
        deviceId: String,
        deviceType: String,
    ) {
        val state = _loginState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _loginState.update {
                it.copy(errorMessage = "Email and Password cannot be empty")
            }
            return
        }

        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = networkRepository.loginUser(
                    state.email, state.password,
                    deviceType = deviceType,
                    deviceId = deviceId
                )

                if (response.isSuccessful) {
                    val loginData = response.body()
                    Log.e("GOING_TO_HOME", "Success: ${loginData?.message}")

                    if (loginData != null) {
                        Log.e("GOING_TO_HOME", "Login data is not empty with $loginData")
                        if (!loginData.data.token.isNullOrEmpty() &&
                            !loginData.data.refreshToken.isNullOrEmpty() &&
                            !loginData.data.tokenType.isNullOrEmpty()
                        ) {
                            Log.e("GOING_TO_HOME", "Login data is not empty in checkLogin")
                            withContext(Dispatchers.IO) {
                                networkRepository.saveUserSession(
                                    UserPrefData(
                                        season = loginData.data,
                                        deviceID = deviceId,
                                        deviceType = deviceType
                                    )
                                )
                            }
                            _loginState.update {
                                it.copy(isLoading = false, isLoggedIn = true)
                            }
                            _loginState.update {
                                it.copy(
                                    email = "",
                                    password = ""
                                )
                            }
                        }
                        // _isLoggedIn.value = true
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

                    Log.e("CHECK_LOGIN", "Error: $combinedErrorMessage")

                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = combinedErrorMessage.ifBlank { "User not found" }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("CHECK_LOGIN", "Exception: ${e.message}")
                _loginState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Network error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                networkRepository.clearSession()
            }
            _isLoggedIn.value = false
            _loginState.update {
                it.copy(
                    isLoggedIn = false,
                    email = "",
                    password = "",
                    errorMessage = null
                )
            }
        }
    }


}