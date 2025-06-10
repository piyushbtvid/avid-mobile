package com.faithForward.network

import com.faithForward.network.dto.CategoryDetailResponse
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.HomeSectionApiResponse
import com.faithForward.network.dto.SectionContentResponse
import com.faithForward.network.dto.common.ApiMessageResponse
import com.faithForward.network.dto.creator.CreatorDetailData
import com.faithForward.network.dto.creator.CreatorResponse
import com.faithForward.network.dto.creator.CreatorsListApiResponse
import com.faithForward.network.dto.detail.CardDetail
import com.faithForward.network.dto.genre.GenreResponse
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.dto.myList.MyListResponse
import com.faithForward.network.dto.series.SingleSeriesDetailResponse
import com.faithForward.network.dto.request.LikeRequest
import com.faithForward.network.dto.request.LoginRequest
import com.faithForward.util.Constants
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceInterface {

    @GET(Constants.HOME_SECTION_END_POINT)
    suspend fun getHomeSectionData(
        @Header("Authorization") token: String,
    ): Response<HomeSectionApiResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getCategories(
        @Header("Authorization") token: String,
    ): Response<CategoryResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getGivenCategoryDetail(
        @Path("id") id: Int,
    ): Response<CategoryDetailResponse>

    @GET(Constants.CREATOR_END_POINT)
    suspend fun getCreatorsList(
        @Header("Authorization") token: String,
    ): Response<CreatorsListApiResponse>

    @GET(Constants.CREATOR_DETAIL_END_POINT)
    suspend fun getCreatorDetail(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<CreatorResponse>

    @POST(Constants.LOGIN_END_POINT)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @GET(Constants.GIVEN_SECTION_END_POINT)
    suspend fun getGivenSectionData(
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Response<SectionContentResponse>


    @GET(Constants.GIVEN_SECTION_END_POINT)
    suspend fun getMyListSectionData(
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Response<MyListResponse>


    @GET(Constants.SINGLE_GENRE_DETAIL_END_POINT)
    suspend fun getGivenGenreData(
        @Path("id") id: String,
    ): Response<GenreResponse>


    @GET(Constants.GIVEN_ITEM_DETAIL_END_POINT)
    suspend fun getGivenCardDetail(
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
    ): Response<CardDetail>


    @GET(Constants.SINGLE_SERIES_DETAIL_API)
    suspend fun getSingleSeriesDetail(
        @Path("id") id: String,
    ): Response<SingleSeriesDetailResponse>

    @POST(Constants.MY_LIST_END_POINT)
    suspend fun addToMyList(
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
    ): Response<ApiMessageResponse>

    @DELETE(Constants.MY_LIST_END_POINT)
    suspend fun removeFromMyList(
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
    ): Response<ApiMessageResponse>

    @POST(Constants.LIKE_DISLIKE_END_POINT)
    suspend fun likeOrDisLikeContent(
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
        @Body body: LikeRequest,
    ): Response<ApiMessageResponse>

    @GET(Constants.LIKED_LIST_END_POINT)
    suspend fun getLikedList(
        @Header("Authorization") token: String,
    ): Response<MyListResponse>

    @GET(Constants.DIS_lIKED_LIST_END_POINT)
    suspend fun getDisLikedList(
        @Header("Authorization") token: String,
    ): Response<MyListResponse>

}