package com.faithForward.preferences

import com.faithForward.network.dto.login.LoginData

data class UserPrefData(
    val season : LoginData,
    val deviceType : String?,
    val deviceID : String?
)
