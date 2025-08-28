package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.ui.login.LoginEvent
import com.faithForward.media.ui.login.LoginState
import com.faithForward.network.dto.login.ErrorResponse
import com.faithForward.preferences.UserPrefData
import com.faithForward.repository.NetworkRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
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

    private val _isBuffer = MutableStateFlow(true)
    val isBuffer = _isBuffer.asStateFlow()

    private var refreshJob: Job? = null


    init {
        //doing is logged in and refresh token in this method on start of the app
        startRefreshTokenImplementation()
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
                    deviceType = event.deviceType, deviceId = event.deviceId
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
                    state.email, state.password, deviceType = deviceType, deviceId = deviceId
                )

                if (response.isSuccessful) {
                    val loginData = response.body()
                    Log.e("GOING_TO_HOME", "Success: ${loginData?.message}")

                    if (loginData != null) {
                        Log.e("GOING_TO_HOME", "Login data is not empty with $loginData")
                        if (!loginData.data.token.isNullOrEmpty() && !loginData.data.refreshToken.isNullOrEmpty() && !loginData.data.tokenType.isNullOrEmpty()) {
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
                                    email = "", password = ""
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
                        it.copy(isLoading = false,
                            errorMessage = combinedErrorMessage.ifBlank { "User not found" })
                    }
                }
            } catch (e: Exception) {
                Log.e("CHECK_LOGIN", "Exception: ${e.message}")
                _loginState.update {
                    it.copy(
                        isLoading = false, errorMessage = "Network error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }


    fun cancelRefreshJob() {
        refreshJob?.cancel()
        refreshJob = null
    }


    private fun startRefreshTokenImplementation() {
        viewModelScope.launch {
            _isBuffer.emit(true)
            // user saved season for checking isLoggedIn or Not
            val userSeason = networkRepository.getCurrentSession()
            if (userSeason?.season?.refreshToken != null) {
                val refreshToken = userSeason.season.refreshToken
                val expireTime = userSeason.season.expire_date
                val currentTime = System.currentTimeMillis() / 1000 // in seconds
                val timeLeft = expireTime - currentTime
                // refreshing token for one time only for first time (if refresh not get success then will send isLogged as false)
                if (timeLeft <= 3600) {
                    val handleRefresh = handleRefresh(refreshToken!!)
                    // means refresh api is giving error (mostly refresh token expire error)
                    if (!handleRefresh) {
                        networkRepository.clearSession()
                        _isBuffer.emit(false)
                        _isLoggedIn.value = false
                    }
                    // means token refreshed successfully  so user is logged in and start check refreshToken
                    else {
                        _isBuffer.emit(false)
                        _isLoggedIn.value = true
                        checkRefreshToken()
                    }
                }
                // still have time left for refresh token api so directly calling check refresh which will delay it
                else {
                    _isBuffer.emit(false)
                    _isLoggedIn.value = true
                    checkRefreshToken()
                }
            } else {
                _isBuffer.emit(false)
                _isLoggedIn.value = false
            }
        }
    }

    fun checkRefreshToken() {
        // Canceling any existing job
        cancelRefreshJob()

        refreshJob = viewModelScope.launch(Dispatchers.IO) {
            Log.e("REFRESH_TOKEN", "checkRefreshToken started")

            while (isActive) {
                try {
                    val session = networkRepository.getCurrentSession()
                    val season = session?.season
                    val refreshToken = season?.refreshToken
                    val expireTime = season?.expire_date ?: break
                    val currentTime = System.currentTimeMillis() / 1000 // in seconds
                    val timeLeft = expireTime - currentTime

                    if (timeLeft <= 3600) {
                        // Immediate refresh
                        refreshToken?.let {
                            Log.e("REFRESH_TOKEN", "Refreshing now — time left: $timeLeft sec")
                            handleRefresh(it)
                        }
                    } else {
                        val delayTime = (timeLeft - 3600) * 1000L // milliseconds
                        Log.e("REFRESH_TOKEN", "Delaying refresh for $delayTime ms")
                        delay(delayTime)

                        refreshToken?.let {
                            Log.e("REFRESH_TOKEN", "Refreshing after delay")
                            handleRefresh(it)
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("REFRESH_TOKEN", "Exception in checkRefreshToken: ${ex.message}")
                    ex.printStackTrace()
                    break // break the loop to avoid infinite failures
                }
            }

            Log.e("REFRESH_TOKEN", "Refresh Job ended or canceled")
        }
    }

    //function to handle refresh logic
    private suspend fun handleRefresh(refreshToken: String): Boolean {
        return try {
            val refreshResponse = networkRepository.refreshToken(refreshToken)
            if (refreshResponse.isSuccessful) {
                val newUserData = refreshResponse.body()?.data
                if (newUserData != null) {
                    val update = networkRepository.updateTokenSeason(
                        newToken = newUserData.token,
                        newRefreshToken = newUserData.refresh_token,
                        newExpireTime = newUserData.expire_date,
                        newTokenType = newUserData.token_type
                    )
                    Log.e("REFRESH_TOKEN", "updateTokenSeason result: $update")
                    return update
                }
            } else {
                //  Tracking 401 here
                if (refreshResponse.code() == 401) {
                    Log.e("REFRESH_TOKEN", "401 Unauthorized detected during token refresh")
//                    networkRepository.clearSession()
//                    _logoutEvent.emit(Unit)
                } else {
                    Log.e("REFRESH_TOKEN", "Refresh API failed: ${refreshResponse.code()}")
                }
            }
            false
        } catch (e: Exception) {
            Log.e("REFRESH_TOKEN", "Exception in handleRefresh: ${e.message}")
            false
        }
    }


     fun updateUserSubscriptionDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = networkRepository.getUserSubscriptionDetail()
                if (response.isSuccessful) {
                    Log.e(
                        "CHECK_USER_SUBSCRIPTION",
                        "check user subscription resonse when sucess is ${response.body()}"
                    )
                    val userUpdatedData = response.body()
                    if (userUpdatedData?.data != null) {
                        Log.e(
                            "CHECK_USER_SUBSCRIPTION",
                            "check user subscription User New Data is not null so updating user Subscription locally"
                        )
                        val isUserDataSaved = networkRepository.updateUserInfo(userUpdatedData.data)
                        if(isUserDataSaved){
                            Log.e(
                                "CHECK_USER_SUBSCRIPTION",
                                "check user subscription User New Data is Saved Sucess in Local"
                            )
                        }else{
                            Log.e(
                                "CHECK_USER_SUBSCRIPTION",
                                "check user subscription User New Data is Not Saved Sucess in Local"
                            )
                        }
                    }
                } else {
                    Log.e(
                        "CHECK_USER_SUBSCRIPTION",
                        "check user subscription resonse when error is ${response.message()}"
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelRefreshJob()
    }

}