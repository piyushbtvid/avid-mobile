package com.faithForward.repository

import android.util.Log
import com.faithForward.network.ApiServiceInterface
import com.faithForward.network.dto.genre.GenreResponse
import com.faithForward.network.dto.login.ActivationCodeResponse
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.dto.request.ContinueWatchingRequest
import com.faithForward.network.dto.request.DeviceIdRequest
import com.faithForward.network.dto.request.LikeRequest
import com.faithForward.network.dto.request.LoginRequest
import com.faithForward.preferences.UserPrefData
import com.faithForward.preferences.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val userPreferences: UserPreferences,
    private val apiServiceInterface: ApiServiceInterface,
) {

    suspend fun getHomeSectionData() = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getHomeSectionData(
            token = token,
            deviceType = deviceType,
            deviceId = deviceId
        )
    }

    suspend fun getCategories() = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getCategories(
            deviceId = deviceId,
            deviceType = deviceType,
            token = token
        )
    }

    suspend fun getCreatorsList() = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getCreatorsList(
            deviceId = deviceId,
            deviceType = deviceType,
            token = token
        )
    }

    suspend fun loginUser(
        email: String,
        password: String,
        deviceType: String,
        deviceId: String,
    ): Response<LoginResponse> {
        val loginRequest = LoginRequest(
            password = password, email = email
        )
        return apiServiceInterface.loginUser(
            loginRequest = loginRequest,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun saveUserSession(prefData: UserPrefData) {
        Log.e("USER_SESSION", "Saving user session in repo with $prefData")
        userPreferences.saveUserSession(prefData)
    }

    fun getCurrentSession(): UserPrefData? {
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
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getGivenSectionData(
            deviceId = deviceId,
            deviceType = deviceType,
            id = sectionName,
            token = token
        )
    }

    suspend fun getMyListSectionData(
        sectionName: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getMyListSectionData(
            deviceId = deviceId,
            deviceType = deviceType,
            id = sectionName,
            token = token
        )
    }

    suspend fun getGivenGenreData(
        itemId: String,
    ): Response<GenreResponse> {
        val userSession = userPreferences.getUserSession()
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        return apiServiceInterface.getGivenGenreData(
            deviceId = deviceId,
            deviceType = deviceType,
            id = itemId
        )
    }

    suspend fun getGivenCardDetail(
        slug: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getGivenCardDetail(
            deviceId = deviceId,
            deviceType = deviceType,
            slug = slug,
            token = token
        )
    }

    suspend fun getSingleSeriesDetail(
        itemId: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getSingleSeriesDetail(
            deviceId = deviceId,
            deviceType = deviceType,
            id = itemId
        )
    }

    suspend fun addToMyWatchList(
        slug: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.addToMyList(
            slug = slug, token = token,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun removeFromMyWatchList(
        slug: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.removeFromMyList(
            slug = slug, token = token,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun likeDisLikeContent(
        slug: String,
        type: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val body = LikeRequest(type = type)
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.likeOrDisLikeContent(
            slug = slug, token = token, body = body,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun getLikedList(
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.getLikedList(
            deviceType = deviceType,
            token = token,
            deviceId = deviceId
        )
    }

    suspend fun getDisLikedList(
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.getDisLikedList(
            deviceId = deviceId,
            deviceType = deviceType,
            token = token
        )
    }

    suspend fun getCreatorDetail(
        id: Int,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.getCreatorDetail(
            id = id, token = token,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun getCreatorContentList(
        id: Int,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.getCreatorContentList(
            id = id, token = token,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun saveContinueWatching(
        request: ContinueWatchingRequest,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.saveContinueWatching(
            token = token, request = request,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun searchContent(
        query: String,
    ) = withContext(Dispatchers.IO) {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        apiServiceInterface.searchContent(
            token = token, query = query,
            deviceId = deviceId,
            deviceType = deviceType
        )
    }

    suspend fun generateLoginQrCode(
        deviceId: String,
        deviceType: String,
    ): Response<ActivationCodeResponse> {
        Log.e("CHECK_LOGIN", "Genrate QR called in repo")
        return apiServiceInterface.generateLoginQrCode(
            deviceId = deviceId,
            deviceType = deviceType
        )
    }


    suspend fun checkLoginStatus(
        deviceId: String,
        deviceType: String,
    ): Response<LoginResponse> {
        Log.e("CHECK_LOGIN", "check login called in repo")
        val request = DeviceIdRequest(deviceId)
        return apiServiceInterface.checkLoginStatus(
            deviceId = deviceId,
            deviceType = deviceType,
            request = request
        )
    }
}