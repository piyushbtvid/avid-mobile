package com.faithForward.media.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.login.qr.LoginQrScreenDto
import com.faithForward.media.viewModel.uiModels.QrLoginEvent
import com.faithForward.media.viewModel.uiModels.QrLoginState
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

    fun onEvent(event: QrLoginEvent) {
        when (event) {
            is QrLoginEvent.StartLogin -> {
                storedDeviceId = event.deviceId
                storedDeviceType = event.deviceType
                generateLoginQrCode(event.deviceId, event.deviceType)
            }

            QrLoginEvent.RetryQrCode -> retryQrCode()
            QrLoginEvent.StopPolling -> stopPolling()
        }
    }

    private fun generateLoginQrCode(deviceId: String, deviceType: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = networkRepository.generateLoginQrCode(deviceId, deviceType)
                if (response.isSuccessful) {
                    val data = response.body()

                    expireTimestamp = data?.data?.expires_in
                    if (data?.data?.expires_in != null) {
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
                    }
                    startCountdown()
                    startPolling(deviceId, deviceType)
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
                checkLoginStatus(deviceId, deviceType)
            }
        }
    }

    private suspend fun checkLoginStatus(deviceId: String, deviceType: String) {
        try {
            val response = networkRepository.checkLoginStatus(deviceId, deviceType)
            if (response.isSuccessful) {
                val loginData = response.body()
                if (!loginData?.data?.token.isNullOrEmpty() &&
                    !loginData?.data?.refreshToken.isNullOrEmpty() &&
                    !loginData?.data?.tokenType.isNullOrEmpty()
                ) {
                    _state.update { it.copy(isLoggedIn = true) }
                    stopPolling()
                }
            }
        } catch (e: Exception) {
            // Silent fail
        }
    }

    private fun retryQrCode() {
        expireTimestamp = null
        _state.update {
            it.copy(
                qrScreenDto = null,
                timeLeftSeconds = 0L,
                isLoggedIn = false,
                errorMessage = null
            )
        }

        val id = storedDeviceId ?: return
        val type = storedDeviceType ?: return

        onEvent(
            QrLoginEvent.StartLogin(
                deviceId = id,
                deviceType = type
            )
        )
    }

    private fun stopPolling() {
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
