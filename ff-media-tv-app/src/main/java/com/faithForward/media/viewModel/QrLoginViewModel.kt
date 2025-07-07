package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.login.qr.LoginQrScreenDto
import com.faithForward.media.viewModel.uiModels.QrLoginEvent
import com.faithForward.media.viewModel.uiModels.QrLoginState
import com.faithForward.preferences.UserPrefData
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrLoginViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(QrLoginState())
    val state: StateFlow<QrLoginState> = _state.asStateFlow()

    private var countdownJob: Job? = null
    private var statusPollingJob: Job? = null
    private var expireTimestamp: Long? = null

    private var storedDeviceId: String? = null
    private var storedDeviceType: String? = null
    private var lastActivationStatus: String? = null

    fun onEvent(event: QrLoginEvent) {
        when (event) {
            is QrLoginEvent.StartLogin -> {
                Log.e("CHECK_LOGIN", "Start Login called in QR Login ViewModel")
                storedDeviceId = event.deviceId
                storedDeviceType = event.deviceType
                generateLoginQrCode(event.deviceId, event.deviceType)
            }

            is QrLoginEvent.RetryQrCode -> {
                retryQrCode()
            }

            is QrLoginEvent.StopPolling -> {
                stopPolling()
            }
        }
    }

    private fun generateLoginQrCode(deviceId: String, deviceType: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                Log.e("CHECK_LOGIN", "generate QR called in viewModel")
                val response = networkRepository.generateLoginQrCode(deviceId, deviceType)
                if (response.isSuccessful) {
                    val data = response.body()
                    expireTimestamp = data?.data?.expires_in

                    if (data?.data?.expires_in != null) {
                        lastActivationStatus = data.data.activation_status

                        _state.update {
                            it.copy(
                                isLoading = false,
                                qrScreenDto = LoginQrScreenDto(
                                    qrImage = data.data.qr_img,
                                    expiredTime = convertUnixToTime(data.data.expires_in),
                                    code = data.data.activation_code,
                                    url = data.data.verification_url
                                ),
                                timeLeftSeconds = data.data.expires_in - (System.currentTimeMillis() / 1000)
                            )
                        }

                        startCountdown()
                        startPolling(deviceId, deviceType)
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to generate QR code: ${response.code()}"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Unexpected Error"
                    )
                }
            }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val newTime = (_state.value.timeLeftSeconds - 1).coerceAtLeast(0)
                _state.update { it.copy(timeLeftSeconds = newTime) }

                if (newTime <= 0) {
                    Log.e("CHECK_LOGIN", "Countdown expired, triggering QR code regeneration")
                    onEvent(QrLoginEvent.RetryQrCode)
                    break
                }
            }
        }
    }

    private fun startPolling(deviceId: String, deviceType: String) {
        statusPollingJob?.cancel()
        statusPollingJob = viewModelScope.launch {
            while (isActive) {
                delay(4000)
                try {
                    Log.e("CHECK_LOGIN", "Checking login status for deviceId: $deviceId")
                    val response = networkRepository.checkLoginStatus(deviceId, deviceType)
                    if (response.isSuccessful) {
                        val loginData = response.body()
                        Log.e("CHECK_LOGIN", "Login data is not empty with $loginData")
                        if (!loginData?.data?.token.isNullOrEmpty() &&
                            !loginData?.data?.refreshToken.isNullOrEmpty() &&
                            !loginData?.data?.tokenType.isNullOrEmpty()
                        ) {
                            Log.e("CHECK_LOGIN", "Login data is not empty in checkLogin")
                            networkRepository.saveUserSession(
                                UserPrefData(
                                    season = loginData!!.data,
                                    deviceID = deviceId,
                                    deviceType = deviceType
                                )
                            )
                            stopPolling()
                            _state.update { it.copy(isLoggedIn = true) }
                        }
                    } else {
                        Log.e("CHECK_LOGIN", "Status check failed with code: ${response.code()}")
                        // Optionally handle specific status codes
                    }
                } catch (e: Exception) {
                    Log.e("CHECK_LOGIN", "Error checking login status: ${e.message}")
                    // Continue polling despite errors
                }
            }
        }
    }

    private fun retryQrCode() {
        expireTimestamp = null
        _state.update {
            it.copy(
                timeLeftSeconds = 0L,
                isLoggedIn = false,
                errorMessage = null
            )
        }

        val id = storedDeviceId ?: return
        val type = storedDeviceType ?: return

        if (lastActivationStatus == "activated") {
            Log.e(
                "CHECK_LOGIN",
                "QR already activated, skipping regeneration. Polling status instead."
            )
            startPolling(id, type)
        } else {
            Log.e("CHECK_LOGIN", "Retrying QR generation...")
            generateLoginQrCode(id, type)
        }
    }

    private fun stopPolling() {
        lastActivationStatus = null
        storedDeviceId = null
        storedDeviceType = null
        countdownJob?.cancel()
        statusPollingJob?.cancel()
    }

    private fun convertUnixToTime(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
        sdf.timeZone = java.util.TimeZone.getDefault()
        return sdf.format(timestamp * 1000)
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}
