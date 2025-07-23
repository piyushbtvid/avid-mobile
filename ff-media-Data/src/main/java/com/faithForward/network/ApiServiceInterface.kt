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
import com.faithForward.network.dto.login.refresh_token.RefreshTokenResponse
import com.faithForward.network.dto.myList.MyListResponse
import com.faithForward.network.dto.request.ContinueWatchingRequest
import com.faithForward.network.dto.request.DeviceIdRequest
import com.faithForward.network.dto.request.LikeRequest
import com.faithForward.network.dto.request.LoginRequest
import com.faithForward.network.dto.request.RecentSearchRequest
import com.faithForward.network.dto.search.SearchResponse
import com.faithForward.network.dto.search.recent_search.RecentSearchResponse
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
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<HomeSectionApiResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getCategories(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<CategoryResponse>

    @GET(Constants.CATEGORY_END_POINT)
    suspend fun getGivenCategoryDetail(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: Int,
    ): Response<CategoryDetailResponse>

    @GET(Constants.CREATOR_END_POINT)
    suspend fun getCreatorsList(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<CreatorsListApiResponse>

    @GET(Constants.CREATOR_DETAIL_END_POINT)
    suspend fun getCreatorDetail(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<CreatorResponse>

    @GET(Constants.CREATOR_CONTENT_LIST_END_POINT)
    suspend fun getCreatorContentList(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: Int,
        @Header("Authorization") token: String,
    ): Response<SectionContentResponse>

    @POST(Constants.LOGIN_END_POINT)
    suspend fun loginUser(
        @Body loginRequest: LoginRequest,
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
    ): Response<LoginResponse>

    @GET(Constants.GIVEN_SECTION_END_POINT)
    suspend fun getGivenSectionData(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Response<SectionContentResponse>


    @GET(Constants.GIVEN_SECTION_END_POINT)
    suspend fun getMyListSectionData(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): Response<MyListResponse>


    @GET(Constants.SINGLE_GENRE_DETAIL_END_POINT)
    suspend fun getGivenGenreData(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: String,
    ): Response<GenreResponse>


    @GET(Constants.GIVEN_ITEM_DETAIL_END_POINT)
    suspend fun getGivenCardDetail(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
    ): Response<CardDetail>


    @GET(Constants.SINGLE_SERIES_DETAIL_API)
    suspend fun getSingleSeriesDetail(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("id") id: String,
    ): Response<SingleSeriesDetailResponse>

    @POST(Constants.MY_LIST_END_POINT)
    suspend fun addToMyList(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
    ): Response<ApiMessageResponse>

    @DELETE(Constants.MY_LIST_END_POINT)
    suspend fun removeFromMyList(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
    ): Response<ApiMessageResponse>

    @POST(Constants.LIKE_DISLIKE_END_POINT)
    suspend fun likeOrDisLikeContent(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Path("slug") slug: String,
        @Header("Authorization") token: String,
        @Body body: LikeRequest,
    ): Response<ApiMessageResponse>

    @GET(Constants.LIKED_LIST_END_POINT)
    suspend fun getLikedList(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<MyListResponse>

    @GET(Constants.DIS_lIKED_LIST_END_POINT)
    suspend fun getDisLikedList(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<MyListResponse>

    @POST(Constants.SAVE_CONTINUE_WATCHING_END_POINT)
    suspend fun saveContinueWatching(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
        @Body request: ContinueWatchingRequest,
    ): Response<ApiMessageResponse>


    @GET(Constants.SEARCH_END_POINT)
    suspend fun searchContent(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
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


    @POST(Constants.LOGOUT_END_POINT)
    suspend fun logoutUser(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<ApiMessageResponse>

    @POST(Constants.REFRESH_TOKEN_POINT)
    suspend fun refreshToken(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
        @Query("refresh_token") refreshToken: String,
    ): Response<RefreshTokenResponse>

    @GET(Constants.RECENT_SEARCH_END_POINT)
    suspend fun getRecentSearch(
        @Header("X-Device-Id") deviceId: String,
        @Header("X-Device-Type") deviceType: String,
        @Header("Authorization") token: String,
    ): Response<RecentSearchResponse>


    @POST(Constants.RECENT_SEARCH_END_POINT)
    suspend fun saveRecentSearch(
        @Body recentSearchRequest: RecentSearchRequest,
        @Header("Authorization") token: String,
    ): Response<ApiMessageResponse>

}