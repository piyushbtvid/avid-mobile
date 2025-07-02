package com.faithForward.network

import com.faithForward.network.dto.CategoryDetailResponse
import com.faithForward.network.dto.CategoryResponse
import com.faithForward.network.dto.HomeSectionApiResponse
import com.faithForward.network.dto.SectionContentResponse
import com.faithForward.network.dto.common.ApiMessageResponse
import com.faithForward.network.dto.creator.CreatorResponse
import com.faithForward.network.dto.creator.CreatorsListApiResponse
import com.faithForward.network.dto.detail.CardDetail
import com.faithForward.network.dto.genre.GenreResponse
import com.faithForward.network.dto.login.ActivationCodeResponse
import com.faithForward.network.dto.login.LoginResponse
import com.faithForward.network.dto.myList.MyListResponse
import com.faithForward.network.dto.request.ContinueWatchingRequest
import com.faithForward.network.dto.request.DeviceIdRequest
import com.faithForward.network.dto.request.LikeRequest
import com.faithForward.network.dto.request.LoginRequest
import com.faithForward.network.dto.search.SearchResponse
import com.faithForward.network.dto.series.SingleSeriesDetailResponse
import com.faithForward.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET(Constants.CREATOR_CONTENT_LIST_END_POINT)
    suspend fun getCreatorContentList(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<SectionContentResponse>

    @POST(Constants.LOGIN_END_POINT)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest,
        @Header("X-Device-Id") deviceId: String = "1234578",
        @Header("X-Device-Type") deviceType: String = "fire_tv",
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

    @POST(Constants.SAVE_CONTINUE_WATCHING_END_POINT)
    suspend fun saveContinueWatching(
        @Header("Authorization") token: String,
        @Body request: ContinueWatchingRequest,
    ): Response<ApiMessageResponse>


    @GET(Constants.SEARCH_END_POINT)
    suspend fun searchContent(
        @Header("Authorization") token: String,
        @Query("query") query: String,
    ): Response<SearchResponse>

    @POST(Constants.GENERATE_ACTIVATION_CODE_END_POINT)
    suspend fun generateLoginQrCode(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
    ): Response<ActivationCodeResponse>

    @POST(Constants.LOGIN_STATUS_CHECK_END_POINT)
    suspend fun checkLoginStatus(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Body request: DeviceIdRequest,
    ): Response<LoginResponse>

}