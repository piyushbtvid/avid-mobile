package com.faithForward.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.faithForward.network.dto.login.LoginData
import com.faithForward.network.dto.login.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_PROFILE_IMG = stringPreferencesKey("user_profile_img")
        val USER_STATUS = stringPreferencesKey("user_status")
        val USER_ROLE = stringPreferencesKey("user_role")
        val TOKEN = stringPreferencesKey("token")
        val TOKEN_TYPE = stringPreferencesKey("token_type")
    }

    suspend fun saveUserSession(session: LoginData) {
        dataStore.edit { prefs ->
            prefs[USER_NAME] = session.user.name
            prefs[USER_EMAIL] = session.user.email
            session.user.phone?.let { prefs[USER_PHONE] = it }
            session.user.profileImg?.let { prefs[USER_PROFILE_IMG] = it }
            prefs[USER_STATUS] = session.user.status
            prefs[USER_ROLE] = session.user.role
            prefs[TOKEN] = session.token
            prefs[TOKEN_TYPE] = session.tokenType
        }
    }

    val userSessionFlow: Flow<LoginData?> = dataStore.data
        .map { prefs ->
            val name = prefs[USER_NAME] ?: return@map null
            val email = prefs[USER_EMAIL] ?: return@map null
            val phone = prefs[USER_PHONE]
            val profileImg = prefs[USER_PROFILE_IMG]
            val status = prefs[USER_STATUS] ?: return@map null
            val role = prefs[USER_ROLE] ?: return@map null
            val token = prefs[TOKEN] ?: return@map null
            val tokenType = prefs[TOKEN_TYPE] ?: return@map null

            LoginData(
                user = User(name, email, phone, profileImg, status, role),
                token = token,
                tokenType = tokenType
            )
        }

    fun getCurrentSession(): LoginData? = runBlocking {
        dataStore.data.firstOrNull()?.let { prefs ->
            val name = prefs[USER_NAME] ?: return@let null
            val email = prefs[USER_EMAIL] ?: return@let null
            val phone = prefs[USER_PHONE]
            val profileImg = prefs[USER_PROFILE_IMG]
            val status = prefs[USER_STATUS] ?: return@let null
            val role = prefs[USER_ROLE] ?: return@let null
            val token = prefs[TOKEN] ?: return@let null
            val tokenType = prefs[TOKEN_TYPE] ?: return@let null

            LoginData(
                user = User(name, email, phone, profileImg, status, role),
                token = token,
                tokenType = tokenType
            )
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}