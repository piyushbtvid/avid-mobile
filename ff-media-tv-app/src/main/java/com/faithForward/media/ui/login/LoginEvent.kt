package com.faithForward.media.ui.login

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class SubmitLogin(val deviceType: String, val deviceId: String) : LoginEvent()
    data class ResetIsLogin(val boolean: Boolean) : LoginEvent()
}
