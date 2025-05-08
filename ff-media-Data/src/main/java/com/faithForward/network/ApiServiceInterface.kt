package com.faithForward.network

import com.faithForward.util.Constants
import com.faithForward.network.dto.CategoryDetailResponse
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.SectionApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceInterface {

    @GET(Constants.SECTION_END_POINT)
    suspend fun getGivenSectionData(
        @Path("id") id: Int
    ): Response<SectionApiResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getCategories(): Response<CategoryResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getGivenCategoryDetail(
        @Path("id") id: Int
    ): Response<CategoryDetailResponse>

}