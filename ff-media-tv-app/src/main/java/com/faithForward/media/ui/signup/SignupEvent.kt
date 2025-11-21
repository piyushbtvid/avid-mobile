package com.faithForward.media.ui.signup

sealed class SignupEvent {
    data class NameChanged(val name: String) : SignupEvent()
    data class EmailChanged(val email: String) : SignupEvent()
    data class PasswordChanged(val password: String) : SignupEvent()
    data class SubmitSignup(val deviceType: String, val deviceId: String) : SignupEvent()
    data class ResetIsSignedUp(val boolean: Boolean) : SignupEvent()
}

