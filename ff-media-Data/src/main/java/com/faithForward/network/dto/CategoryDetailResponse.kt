package com.faithForward.network.dto

import com.faithForward.network.dto.common.VideoDetail
import com.google.gson.annotations.SerializedName

data class CategoryDetailResponse(
    @SerializedName("status") val status: String,
    @SerializedName("category") val category: String,
    @SerializedName("movies") val movies: List<Movie>,
    @SerializedName("series") val series: List<Any> // Empty array, using Any for flexibility
)

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("description") val description: String,
    @SerializedName("poster_image") val posterImage: String,
    @SerializedName("thumbnail_image") val thumbnailImage: String,
    @SerializedName("imdb_rating") val imdbRating: String,
    @SerializedName("order_index") val orderIndex: Int?,
    @SerializedName("monetization_type") val monetizationType: String,
    @SerializedName("plan_amount") val planAmount: Int?,
    @SerializedName("plan_period") val planPeriod: String?,
    @SerializedName("plan_frequency") val planFrequency: String,
    @SerializedName("release_date") val releaseDate: Long,
    @SerializedName("duration") val duration: String,
    @SerializedName("video_type") val videoType: String,
    @SerializedName("video_url") val videoUrl: String,
    @SerializedName("show_on_tv") val showOnTv: Int,
    @SerializedName("download_enable") val downloadEnable: Int?,
    @SerializedName("download_url") val downloadUrl: String?,
    @SerializedName("subtitle_on_off") val subtitleOnOff: Int,
    @SerializedName("subtitle_language1") val subtitleLanguage1: String?,
    @SerializedName("subtitle_url1") val subtitleUrl1: String?,
    @SerializedName("subtitle_language2") val subtitleLanguage2: String?,
    @SerializedName("subtitle_url2") val subtitleUrl2: String?,
    @SerializedName("subtitle_language3") val subtitleLanguage3: String?,
    @SerializedName("subtitle_url3") val subtitleUrl3: String?,
    @SerializedName("video_detail") val videoDetail: VideoDetail
)
