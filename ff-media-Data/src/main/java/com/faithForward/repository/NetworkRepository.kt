package com.faithForward.repository

import android.util.Log
import com.faithForward.network.ApiServiceInterface
import com.faithForward.network.dto.login.LoginData
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.dto.request.ContinueWatchingRequest
import com.faithForward.network.dto.request.LikeRequest
import com.faithForward.network.dto.request.LoginRequest
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
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getHomeSectionData(token)
    }

    suspend fun getCategories() = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getCategories(token)
    }

    suspend fun getCreatorsList() = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getCreatorsList(token)
    }

    suspend fun loginUser(
        email: String,
        password: String,
    ): Response<LoginResponse> {
        val loginRequest = LoginRequest(
            password = password, email = email
        )
        return apiServiceInterface.loginUser(loginRequest)
    }

    suspend fun saveUserSession(session: LoginData) {
        Log.e("USER_SESSION", "Saving user session in repo with $session")
        userPreferences.saveUserSession(session)
    }

    fun getCurrentSession(): LoginData? {
        Log.e("USER_PREF", "get Current Season called in Repo")
        val session = userPreferences.getUserSession()
        Log.e("USER_PREF", "Current session in repo called: $session")
        return session
    }

    suspend fun clearSession() {
        Log.e("USER_SESSION", "Clearing user session in repo")
        userPreferences.clearSession()
    }

    suspend fun getGivenSectionData(
        sectionName: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getGivenSectionData(sectionName, token)
    }

    suspend fun getMyListSectionData(
        sectionName: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getMyListSectionData(sectionName, token)
    }

    suspend fun getGivenGenreData(
        itemId: String,
    ) = apiServiceInterface.getGivenGenreData(itemId)

    suspend fun getGivenCardDetail(
        slug: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getGivenCardDetail(slug, token)
    }

    suspend fun getSingleSeriesDetail(
        itemId: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getSingleSeriesDetail(itemId)
    }

    suspend fun addToMyWatchList(
        slug: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.addToMyList(slug = slug, token = token)
    }

    suspend fun removeFromMyWatchList(
        slug: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.removeFromMyList(slug = slug, token = token)
    }

    suspend fun likeDisLikeContent(
        slug: String,
        type: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val body = LikeRequest(type = type)
        apiServiceInterface.likeOrDisLikeContent(
            slug = slug, token = token, body = body
        )
    }

    suspend fun getLikedList(
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.getLikedList(token)
    }

    suspend fun getDisLikedList(
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.getDisLikedList(token)
    }

    suspend fun getCreatorDetail(
        id: Int,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.getCreatorDetail(
            id = id, token = token
        )
    }

    suspend fun getCreatorContentList(
        id: Int,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.getCreatorContentList(
            id = id, token = token
        )
    }

    suspend fun saveContinueWatching(
        request: ContinueWatchingRequest,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.saveContinueWatching(
            token = token, request = request
        )
    }

    suspend fun searchContent(
        query: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token = userSession?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        apiServiceInterface.searchContent(
            token = token, query = query
        )
    }
}