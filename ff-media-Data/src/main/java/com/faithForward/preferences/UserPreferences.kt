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
    }

    suspend fun saveUserSession(session: LoginData) = withContext(Dispatchers.IO) {
        with(sharedPreferences.edit()) {
            putString(USER_NAME, session.user?.name)
            putString(USER_EMAIL, session.user?.email)
            putString(USER_ROLE, session.user?.role)
            putString(USER_TYPE, session.user?.user_type)
            putString(TOKEN, session.token)
            putString(TOKEN_TYPE, session.tokenType)
            putString(REFRESH_TOKEN, session.refreshToken)
            putLong(EXPIRE_DATE, session.expire_date)
            putString(ACTIVATION_STATUS, session.activation_status)
            commit() // Use commit() for synchronous save to ensure data is written
        }
    }

    fun getUserSession(): LoginData? {
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

        Log.e("USER_PREF", "token is $token after getUserSeason")

        return LoginData(
            user = User(name, email, role, userType),
            token = token,
            tokenType = tokenType,
            refreshToken = refreshToken,
            expire_date = expireDate,
            activation_status = activationStatus
        )
    }

    suspend fun clearSession() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().clear().commit()
    }
}