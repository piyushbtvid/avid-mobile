package com.faithForward.repository

import android.util.Log
import com.faithForward.network.ApiServiceInterface
import com.faithForward.network.dto.common.ApiMessageResponse
import com.faithForward.network.dto.common.CommanListResponse
import com.faithForward.network.dto.genre.GenreResponse
import com.faithForward.network.dto.login.ActivationCodeResponse
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.dto.login.SignupResponse
import com.faithForward.network.dto.login.User
import com.faithForward.network.dto.login.refresh_token.RefreshTokenResponse
import com.faithForward.network.dto.profile.AllAvatarListResponse
import com.faithForward.network.dto.profile.AllProfileResponse
import com.faithForward.network.dto.profile.ProfileCommonResponse
import com.faithForward.network.dto.request.ContinueWatchingRequest
import com.faithForward.network.dto.request.CreateProfileRequest
import com.faithForward.network.dto.request.DeviceIdRequest
import com.faithForward.network.dto.request.LikeRequest
import com.faithForward.network.dto.request.LoginRequest
import com.faithForward.network.dto.request.SignupRequest
import com.faithForward.network.dto.request.PurchaseRequest
import com.faithForward.network.dto.request.RecentSearchRequest
import com.faithForward.network.dto.search.SearchResponse
import com.faithForward.network.dto.search.recent_search.RecentSearchResponse
import com.faithForward.network.dto.subscription.SubscriptionResponse
import com.faithForward.preferences.ConfigManager
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
        val configData = ConfigManager.getConfigData()
        val shouldSendToken = configData?.let {
            it.enable_login || it.enable_qrlogin
        } ?: false
        val token =
            if (shouldSendToken) {
                userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
            } else {
                ""
            }
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        Log.e("HOME_DATA", "TOKEN IN REPO IS $token")
        apiServiceInterface.getHomeSectionData(
            token = token, deviceType = deviceType, deviceId = deviceId
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
            deviceId = deviceId, deviceType = deviceType, token = token
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
            deviceId = deviceId, deviceType = deviceType, token = token
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
            loginRequest = loginRequest, deviceId = deviceId, deviceType = deviceType
        )
    }

    suspend fun signupUser(
        name: String,
        email: String,
        password: String,
        deviceType: String,
        deviceId: String,
    ): Response<SignupResponse> {
        val signupRequest = SignupRequest(
            name = name,
            email = email,
            password = password
        )
        return apiServiceInterface.signupUser(
            signupRequest = signupRequest, deviceId = deviceId, deviceType = deviceType
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
            deviceId = deviceId, deviceType = deviceType, id = sectionName, token = token
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
            deviceId = deviceId, deviceType = deviceType, id = sectionName, token = token
        )
    }

    suspend fun getGivenGenreData(
        itemId: String,
    ): Response<GenreResponse> {
        val userSession = userPreferences.getUserSession()
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        return apiServiceInterface.getGivenGenreData(
            deviceId = deviceId, deviceType = deviceType, id = itemId
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
            deviceId = deviceId, deviceType = deviceType, slug = slug, token = token
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
            deviceId = deviceId, deviceType = deviceType, id = itemId
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
            slug = slug, token = token, deviceId = deviceId, deviceType = deviceType
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
            slug = slug, token = token, deviceId = deviceId, deviceType = deviceType
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
            slug = slug, token = token, body = body, deviceId = deviceId, deviceType = deviceType
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
            deviceType = deviceType, token = token, deviceId = deviceId
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
            deviceId = deviceId, deviceType = deviceType, token = token
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
            id = id, token = token, deviceId = deviceId, deviceType = deviceType
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
            id = id, token = token, deviceId = deviceId, deviceType = deviceType
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
            token = token, request = request, deviceId = deviceId, deviceType = deviceType
        )
    }

    suspend fun searchContent(
        query: String,
    ): Response<SearchResponse> {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        val result = apiServiceInterface.searchContent(
            token = token, query = query, deviceId = deviceId, deviceType = deviceType
        )

        Log.e("SEARCH_RESULT", "search response in repo is $result")
        return result
    }

    suspend fun getRecentSearchContent(): Response<RecentSearchResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        val result = apiServiceInterface.getRecentSearch(
            token = token, deviceId = deviceId, deviceType = deviceType
        )

        return result
    }

    suspend fun updateRecentSearch(
        contentType: String,
        contentId: String,
    ): Response<ApiMessageResponse> {

        val recentSearchRequest = RecentSearchRequest(
            content_type = contentType,
            content_id = contentId
        )

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""

        return apiServiceInterface.saveRecentSearch(
            recentSearchRequest,
            token
        )

    }

    suspend fun generateLoginQrCode(
        deviceId: String,
        deviceType: String,
    ): Response<ActivationCodeResponse> {
        Log.e("CHECK_LOGIN", "Genrate QR called in repo")
        return apiServiceInterface.generateLoginQrCode(
            deviceId = deviceId, deviceType = deviceType
        )
    }


    suspend fun checkLoginStatus(
        deviceId: String,
        deviceType: String,
    ): Response<LoginResponse> {
        Log.e("CHECK_LOGIN", "check login called in repo")
        val request = DeviceIdRequest(deviceId)
        return apiServiceInterface.checkLoginStatus(
            deviceId = deviceId, deviceType = deviceType, request = request
        )
    }

    suspend fun logoutUser(): Response<ApiMessageResponse> {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""
        return apiServiceInterface.logoutUser(
            token = token, deviceId = deviceId, deviceType = deviceType
        )
    }

    suspend fun refreshToken(refreshToken: String): Response<RefreshTokenResponse> {
        Log.e("REFRESH_TOKEN", "REFRESH token called in network Repo")
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        return apiServiceInterface.refreshToken(
            deviceId = deviceId, deviceType = deviceType, token = token, refreshToken = refreshToken
        )
    }

    suspend fun getAllProfiles(): Response<AllProfileResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        return apiServiceInterface.getUserAllProfiles(
            deviceId = deviceId,
            deviceType = deviceType,
            token = token
        )
    }

    suspend fun createUserProfile(
        userName: String,
        avatarId: Int,
    ): Response<ProfileCommonResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        val createProfileRequest = CreateProfileRequest(
            name = userName,
            avatar = avatarId,
            language = "english",
            preferences = "",
        )

        return apiServiceInterface.createUserProfile(
            deviceId = deviceId,
            deviceType = deviceType,
            token = token,
            createProfileRequest = createProfileRequest
        )

    }


    suspend fun updateProfile(
        profileId: Int,
        userName: String,
        avatarId: Int,
    ): Response<ProfileCommonResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        val createProfileRequest = CreateProfileRequest(
            name = userName,
            avatar = avatarId,
            language = "english",
            preferences = "",
        )

        return apiServiceInterface.updateProfile(
            token = token,
            deviceId = deviceId,
            deviceType = deviceType,
            createProfileRequest = createProfileRequest,
            profileId = profileId
        )

    }

    suspend fun deleteProfile(
        profileId: Int,
        userName: String,
        avatarId: Int,
    ): Response<ApiMessageResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        val createProfileRequest = CreateProfileRequest(
            name = userName,
            avatar = avatarId,
            language = "english",
            preferences = "",
        )


        return apiServiceInterface.deleteProfile(
            token = token,
            deviceId = deviceId,
            deviceType = deviceType,
            //createProfileRequest = createProfileRequest,
            profileId = profileId
        )

    }

    suspend fun setProfile(
        profileId: Int,
    ): Response<ProfileCommonResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        return apiServiceInterface.setProfile(
            token = token,
            deviceId = deviceId,
            deviceType = deviceType,
            profileId = profileId
        )
    }

    suspend fun setPurchase(
        receipt_id: String,
        product_id: String,
    ): Response<LoginResponse> {

        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        return apiServiceInterface.setPurchase(
            deviceId = deviceId,
            token = token,
            deviceType = deviceType,
            purchaseRequest = PurchaseRequest(
                receipt_id = receipt_id,
                product_id = product_id
            )
        )
    }


    suspend fun getAllAvatars(): Response<AllAvatarListResponse> {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""

        return apiServiceInterface.getAllAvatars(
            token = token
        )
    }


    suspend fun updateTokenSeason(
        newToken: String?,
        newRefreshToken: String?,
        newExpireTime: Long,
        newTokenType: String?,
    ): Boolean {
        Log.e(
            "REFRESH_TOKEN",
            "update Token Season for shared pref Repo called with  $newExpireTime $newToken  $newRefreshToken    $newTokenType"
        )
        return userPreferences.updateTokenSession(
            token = newToken,
            refreshToken = newRefreshToken,
            expireDate = newExpireTime,
            tokenType = newTokenType,
        )
    }

    suspend fun updateUserInfo(user: User): Boolean {

        return userPreferences.updateUserInfo(
            name = user.name,
            email = user.email,
            userType = user.user_type,
            role = user.role,
        )
    }

    suspend fun getContinueWatchingList(): Response<CommanListResponse> {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""

        return apiServiceInterface.getContinueWatchingList(
            token = token
        )

    }

    suspend fun getEpgData() = apiServiceInterface.getEpgData()

    suspend fun getUserSubscriptionDetail(): Response<SubscriptionResponse> {
        val userSession = userPreferences.getUserSession()
        val token =
            userSession?.season?.token?.takeIf { it.isNotEmpty() }?.let { "Bearer $it" } ?: ""
        val deviceId = userSession?.deviceID ?: ""
        val deviceType = userSession?.deviceType ?: ""

        return apiServiceInterface.getUserSubscriptionDetail(
            deviceId = deviceId,
            deviceType = deviceType,
            token = token,
        )
    }

    suspend fun getConfigData() = apiServiceInterface.getConfigData()
}