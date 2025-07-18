package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.login.qr.LoginQrScreenDto

sealed class QrLoginEvent {
    data class StartLogin(val deviceId: String, val deviceType: String) : QrLoginEvent()
    data object RetryQrCode : QrLoginEvent()
    data object StopPolling : QrLoginEvent()
}


data class QrLoginState(
    val isLoading: Boolean = false,
    val qrScreenDto: LoginQrScreenDto? = null,
    val timeLeftSeconds: Long = 0L,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
)
