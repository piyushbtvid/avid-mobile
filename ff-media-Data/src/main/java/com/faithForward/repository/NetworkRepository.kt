package com.faithForward.repository

import com.faithForward.preferences.UserPreferences
import com.faithForward.network.ApiServiceInterface
import com.faithForward.network.dto.login.LoginData
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.request.LoginRequest
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val userPreferences: UserPreferences,
    private val apiServiceInterface: ApiServiceInterface
) {

    suspend fun getHomeSectionData(sectionId: Int) =
        apiServiceInterface.getHomeSectionData()

    suspend fun getCategories() = apiServiceInterface.getCategories()

//    suspend fun getGivenCategoryDetail(categoryId: Int) =
//        apiServiceInterface.getGivenSectionData(categoryId)

    suspend fun getCreatorsList() = apiServiceInterface.getCreatorsList()

    suspend fun loginUser(
        email: String,
        password: String
    ): Response<LoginResponse> {
        val loginRequest = LoginRequest(
            password = password,
            email = email
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
        sectionName: String
    ) = apiServiceInterface.getGivenSectionData(sectionName)

    suspend fun getGivenGenreData(
        itemId: String
    ) = apiServiceInterface.getGivenGenreData(itemId)

    suspend fun getGivenCardDetail(
        itemId: String
    ) = apiServiceInterface.getGivenCardDetail(itemId)

}