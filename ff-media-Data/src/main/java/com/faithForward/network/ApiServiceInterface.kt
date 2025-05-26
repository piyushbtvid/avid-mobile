package com.faithForward.network

import com.faithForward.network.dto.CategoryDetailResponse
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.HomeSectionApiResponse
import com.faithForward.network.dto.ItemDetailResponse
import com.faithForward.network.dto.SectionContentResponse
import com.faithForward.network.dto.creator.CreatorsListApiResponse
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.request.LoginRequest
import com.faithForward.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceInterface {

    @GET(Constants.HOME_SECTION_END_POINT)
    suspend fun getHomeSectionData(

    ): Response<HomeSectionApiResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getCategories(): Response<CategoryResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getGivenCategoryDetail(
        @Path("id") id: Int
    ): Response<CategoryDetailResponse>

    @GET(Constants.CREATOR_END_POINT)
    suspend fun getCreatorsList(): Response<CreatorsListApiResponse>

    @POST(Constants.LOGIN_END_POINT)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET(Constants.GIVEN_SECTION_END_POINT)
    suspend fun getGivenSectionData(
        @Path("id") id: String
    ): Response<SectionContentResponse>

    @GET(Constants.GIVEN_ITEM_DETAIL_END_POINT)
    suspend fun getGivenItemIdDetail(
        @Path("id") id: String
    ): Response<ItemDetailResponse>

}