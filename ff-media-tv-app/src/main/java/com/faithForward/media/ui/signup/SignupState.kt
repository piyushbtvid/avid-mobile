package com.faithForward.media.ui.signup

data class SignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignedUp: Boolean = false,
    val errorMessage: String? = null,
)

