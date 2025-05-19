package com.faithForward.repository

import com.faithForward.network.ApiServiceInterface
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.request.LoginRequest
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val apiServiceInterface: ApiServiceInterface
) {

    suspend fun getGivenSectionData(sectionId: Int) =
        apiServiceInterface.getGivenSectionData(sectionId)

    suspend fun getCategories() = apiServiceInterface.getCategories()

    suspend fun getGivenCategoryDetail(categoryId: Int) =
        apiServiceInterface.getGivenSectionData(categoryId)

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

}