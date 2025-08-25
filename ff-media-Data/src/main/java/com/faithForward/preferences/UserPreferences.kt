package com.faithForward.preferences

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.faithForward.network.dto.login.LoginData
import com.faithForward.network.dto.login.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val USER_ROLE = "user_role"
        const val USER_TYPE = "user_type"
        const val TOKEN = "token"
        const val REFRESH_TOKEN = "refresh_token"
        const val TOKEN_TYPE = "token_type"
        const val EXPIRE_DATE = "expire_date"
        const val ACTIVATION_STATUS = "activation_status"
        const val DEVICE_ID = "device_id"
        const val DEVICE_TYPE = "device_type"
    }

    suspend fun saveUserSession(prefData: UserPrefData) = withContext(Dispatchers.IO) {
        with(sharedPreferences.edit()) {
            putString(USER_NAME, prefData.season.user?.name)
            putString(USER_EMAIL, prefData.season.user?.email)
            putString(USER_ROLE, prefData.season.user?.role)
            putString(USER_TYPE, prefData.season.user?.user_type)
            putString(TOKEN, prefData.season.token)
            putString(TOKEN_TYPE, prefData.season.tokenType)
            putString(REFRESH_TOKEN, prefData.season.refreshToken)
            putLong(EXPIRE_DATE, prefData.season.expire_date)
            putString(ACTIVATION_STATUS, prefData.season.activation_status)
            putString(DEVICE_TYPE, prefData.deviceType)
            putString(DEVICE_ID, prefData.deviceID)
            commit() // Use commit() for synchronous save to ensure data is written
        }
    }

    suspend fun updateTokenSession(
        token: String?,
        refreshToken: String?,
        expireDate: Long,
        tokenType: String?,
    ): Boolean = withContext(Dispatchers.IO) {
        with(sharedPreferences.edit()) {
            putString(TOKEN, token)
            putString(REFRESH_TOKEN, refreshToken)
            putLong(EXPIRE_DATE, expireDate)
            putString(TOKEN_TYPE, tokenType)
            commit() // this returns true if save is successful
        }
    }


    suspend fun updateUserInfo(
        name: String?,
        email: String?,
        userType: String?,
        role: String?
    ): Boolean = withContext(Dispatchers.IO) {
        with(sharedPreferences.edit()) {
            putString(USER_NAME, name)
            putString(USER_EMAIL, email)
            putString(USER_TYPE, userType)
            putString(USER_ROLE, role)
            commit() // returns true if successful
        }
    }



    fun getUserSession(): UserPrefData? {
        Log.e("USER_PREF", "get Current Season called in UserPref")
        val name = sharedPreferences.getString(USER_NAME, null)
        val email = sharedPreferences.getString(USER_EMAIL, null)
        val role = sharedPreferences.getString(USER_ROLE, null)
        val userType = sharedPreferences.getString(USER_TYPE, null)
        val token = sharedPreferences.getString(TOKEN, null)
        val tokenType = sharedPreferences.getString(TOKEN_TYPE, null)
        val refreshToken = sharedPreferences.getString(REFRESH_TOKEN, null)
        val activationStatus = sharedPreferences.getString(ACTIVATION_STATUS, null)
        val expireDate = sharedPreferences.getLong(EXPIRE_DATE, 0)
        val deviceType = sharedPreferences.getString(DEVICE_TYPE, null)
        val deviceId = sharedPreferences.getString(DEVICE_ID, null)

        Log.e("USER_PREF", "token is $token after getUserSeason")

        return UserPrefData(
            season = LoginData(
                user = User(
                    name = name,
                    email = email,
                    user_type = userType,
                    role = role,
                ),
                token = token,
                tokenType = tokenType,
                refreshToken = refreshToken,
                expire_date = expireDate,
                activation_status = activationStatus
            ),
            deviceType = deviceType,
            deviceID = deviceId
        )
    }

    suspend fun clearSession() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().clear().commit()
    }
}