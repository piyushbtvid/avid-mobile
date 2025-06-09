package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.login.LoginEvent
import com.faithForward.media.login.LoginState
import com.faithForward.network.dto.login.ErrorResponse
import com.faithForward.network.dto.login.LoginData
import com.faithForward.repository.NetworkRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        Log.e("USER_PREF", "login viewModel init called")
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("USER_PREF", "check login status  in  viewModel  called")
                val session = networkRepository.getCurrentSession()
                Log.e("USER_PREF", "Initial session check in checkLoginStatus is called: $session")
                //checking login based on token is null or not
                _isLoggedIn.value = session?.token != null
                Log.e(
                    "USER_PREF",
                    "isLoged in value in checkLoginStatus is ${_isLoggedIn.value} and sesson is $session"
                )
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
                loginUser()
            }
        }
    }

    private fun loginUser() {
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
                val response = networkRepository.loginUser(state.email, state.password)

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("Login", "Success: ${body?.message}")

                    _loginState.update {
                        it.copy(isLoading = false, isLoggedIn = true)
                    }
                    if (body != null) {
                        withContext(Dispatchers.IO) {
                            networkRepository.saveUserSession(body.data)
                        }
                        _isLoggedIn.value = true
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

                    Log.e("Login", "Error: $combinedErrorMessage")

                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = combinedErrorMessage.ifBlank { "User not found" }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Login", "Exception: ${e.message}")
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