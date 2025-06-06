package com.faithForward.repository

import android.util.Log
import com.faithForward.network.ApiServiceInterface
import com.faithForward.network.dto.login.LoginData
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.request.LoginRequest
import com.faithForward.preferences.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val userPreferences: UserPreferences,
    private val apiServiceInterface: ApiServiceInterface,
) {

    suspend fun getHomeSectionData() = withContext(Dispatchers.IO) {
        // Get the user token from userSessionFlow, default to empty string if null/empty
        val userSession = userPreferences.userSessionFlow.firstOrNull()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getHomeSectionData(token)
    }

    suspend fun getCategories() = withContext(Dispatchers.IO) {
        // Get the user token from userSessionFlow, default to empty string if null/empty
        val userSession = userPreferences.userSessionFlow.firstOrNull()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getCategories(token)
    }

//    suspend fun getGivenCategoryDetail(categoryId: Int) =
//        apiServiceInterface.getGivenSectionData(categoryId)

    suspend fun getCreatorsList() = apiServiceInterface.getCreatorsList()

    suspend fun loginUser(
        email: String,
        password: String,
    ): Response<LoginResponse> {
        val loginRequest = LoginRequest(
            password = password, email = email
        )

        return apiServiceInterface.loginUser(loginRequest)
    }

    val userSession: Flow<LoginData?> = userPreferences.userSessionFlow

    suspend fun saveUserSession(session: LoginData) {
        userPreferences.saveUserSession(session)
    }

    suspend fun clearSession() {
        userPreferences.clearSession()
    }

    fun getCurrentSession(): LoginData? {
        val userData = userPreferences.getCurrentSession()
        return userData
    }

    suspend fun getGivenSectionData(
        sectionName: String,
    ) = withContext(Dispatchers.IO) {
        // Get the user token from userSessionFlow, default to empty string if null/empty
        val userSession = userPreferences.userSessionFlow.firstOrNull()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getGivenSectionData(sectionName, token)
    }


    suspend fun getMyListSectionData(
        sectionName: String,
    ) = withContext(Dispatchers.IO) {
        // Get the user token from userSessionFlow, default to empty string if null/empty
        val userSession = userPreferences.userSessionFlow.firstOrNull()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getMyListSectionData(sectionName, token)
    }


    suspend fun getGivenGenreData(
        itemId: String,
    ) = apiServiceInterface.getGivenGenreData(itemId)

    suspend fun getGivenCardDetail(
        itemId: String,
    ) = apiServiceInterface.getGivenCardDetail(itemId)

    suspend fun getSingleSeriesDetail(
        itemId: String,
    ) = apiServiceInterface.getSingleSeriesDetail(itemId)

    suspend fun addToMyWatchList(
        itemId: String,
    ) = withContext(Dispatchers.IO) {
        // Get the user token from userSessionFlow, default to empty string if null/empty
        val userSession = userPreferences.userSessionFlow.firstOrNull()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.addToMyList(id = itemId, token = token)
    }

    suspend fun removeFromMyWatchList(
        itemId: String,
    ) = withContext(Dispatchers.IO) {
        // Get the user token from userSessionFlow, default to empty string if null/empty
        val userSession = userPreferences.userSessionFlow.firstOrNull()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.removeFromMyList(id = itemId, token = token)
    }


}

