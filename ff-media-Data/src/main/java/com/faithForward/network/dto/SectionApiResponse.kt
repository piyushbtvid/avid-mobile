package com.faithForward.network.dto

import com.faithForward.network.dto.common.VideoDetail
import com.google.gson.annotations.SerializedName


data class SectionApiResponse(
    val status: Boolean,
    val message: String,
    val data: List<Section>
)


data class Section(
    @SerializedName("menu_id")
    val menuId: Int,
    val title: String,
    val type: String, // "Gallery", "Carousel"
    val items: List<Item>,
    @SerializedName("totalItems")
    val totalItems: Int
)


data class Item(
    val type: String, // "movie"
    val id: Int,
    val title: String,
    val slug: String? = null,
    val description: String? = null,
    @SerializedName("thumbnail_image")
    val thumbnailImage: String,
    @SerializedName("poster_image")
    val posterImage: String,
    @SerializedName("imdb_rating")
    val imdbRating: String? = null,
    @SerializedName("order_index")
    val orderIndex: Int? = null,
    @SerializedName("release_date")
    val releaseDate: Long,
    val duration: String? = null,
    @SerializedName("video_type")
    val videoType: String? = null,
    @SerializedName("video_url")
    val videoUrl: String? = null,
    @SerializedName("show_on_tv")
    val showOnTv: Int, // 0 or 1, could be Boolean with custom handling if needed
    @SerializedName("download_enable")
    val downloadEnable: String? = null, // Type uncertain due to only null values, String? is safe
    @SerializedName("download_url")
    val downloadUrl: String? = null,
    @SerializedName("subtitle_on_off")
    val subtitleOnOff: Int, // 0 or 1
    @SerializedName("subtitle_language1")
    val subtitleLanguage1: String? = null,
    @SerializedName("subtitle_url1")
    val subtitleUrl1: String? = null,
    @SerializedName("subtitle_language2")
    val subtitleLanguage2: String? = null,
    @SerializedName("subtitle_url2")
    val subtitleUrl2: String? = null,
    @SerializedName("subtitle_language3")
    val subtitleLanguage3: String? = null,
    @SerializedName("subtitle_url3")
    val subtitleUrl3: String? = null,
    @SerializedName("video_detail")
    val videoDetail: VideoDetail,
    val monetization: Monetization
)


data class Monetization(
    @SerializedName("monetization_type")
    val monetizationType: String, // "free", "one_time_pay", "subscription_based"
    @SerializedName("plan_amount")
    val planAmount: String? = null, // e.g., "80.00"
    @SerializedName("plan_period")
    val planPeriod: String? = null, // In the provided JSON, this is always null
    @SerializedName("plan_frequency")
    val planFrequency: String // "minutes"
)