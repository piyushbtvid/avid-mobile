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
        const val USER_PHONE = "user_phone"
        const val USER_PROFILE_IMG = "user_profile_img"
        const val USER_STATUS = "user_status"
        const val USER_ROLE = "user_role"
        const val TOKEN = "token"
        const val TOKEN_TYPE = "token_type"
    }

    suspend fun saveUserSession(session: LoginData) = withContext(Dispatchers.IO) {
        with(sharedPreferences.edit()) {
            putString(USER_NAME, session.user?.name)
            putString(USER_EMAIL, session.user?.email)
            session.user?.phone?.let { putString(USER_PHONE, it) }
            session.user?.profileImg?.let { putString(USER_PROFILE_IMG, it) }
            putString(USER_STATUS, session.user?.status)
            putString(USER_ROLE, session.user?.role)
            putString(TOKEN, session.token)
            putString(TOKEN_TYPE, session.tokenType)
            commit() // Use commit() for synchronous save to ensure data is written
        }
    }

    fun getUserSession(): LoginData? {
        Log.e("USER_PREF", "get Current Season called in UserPref")
        val name = sharedPreferences.getString(USER_NAME, null)
        val email = sharedPreferences.getString(USER_EMAIL, null)
        val phone = sharedPreferences.getString(USER_PHONE, null)
        val profileImg = sharedPreferences.getString(USER_PROFILE_IMG, null)
        val status = sharedPreferences.getString(USER_STATUS, null)
        val role = sharedPreferences.getString(USER_ROLE, null)
        val token = sharedPreferences.getString(TOKEN, null)
        val tokenType = sharedPreferences.getString(TOKEN_TYPE, null)

        Log.e("USER_PREF", "token is $token after getUserSeason")

        return LoginData(
            user = User(name, email, phone, profileImg, status, role),
            token = token,
            tokenType = tokenType
        )
    }

    suspend fun clearSession() = withContext(Dispatchers.IO) {
        sharedPreferences.edit().clear().commit()
    }
}