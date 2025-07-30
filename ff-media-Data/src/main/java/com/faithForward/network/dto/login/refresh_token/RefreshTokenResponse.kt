package com.faithForward.network.dto.login.refresh_token

data class RefreshTokenResponse(
    val status: String,
    val message: String,
    val data: TokenData,
)

data class TokenData(
    val token: String,
    val refresh_token: String,
    val expire_date: Long,
    val token_type: String,
)