package com.faithForward.network.dto

import com.faithForward.network.dto.detail.ResumeInfo
import com.faithForward.network.dto.series.Season
import com.google.gson.annotations.SerializedName


data class HomeSectionApiResponse(
    val status: String?,
    val message: String?,
    val data: ContentData?,
)

data class ContentData(
    val genres: List<Genre>?,
    @SerializedName("pill_categories") val pillCategories: List<PillCategory>?,
    val sections: List<Section>?,
)

data class Genre(
    val name: String?,
    val id: Int?,
)

data class Section(
    val title: String?,
    val id: String?,
    val content: List<ContentItem>?,
)


data class Creator(
    val id: Int,
    val name: String,
)


data class ContentItem(
    @SerializedName("type") val type: String? = null,
    @SerializedName("content_type") val contentType: String?,
    val id: Int?,
    val slug: String?,
    val name: String?,
    @SerializedName("title") val title: String? = null,
    val description: String?,
    val portrait: String?,
    val landscape: String?,
    val genres: List<Genre>?,
    @SerializedName("pill_categories") val pillCategories: List<PillCategory>? = null,
    val rating: String?,
    @SerializedName("dateUploaded") val dateUploaded: String?,
    @SerializedName("uploadedYear") val uploadedYear: String?,
    var access: String? = null,
    @SerializedName("video_link") var videoLink: String? = null,
    var views: String? = null,
    var creator: Creator? = null,
    var duration: String? = null,
    val likeDislike: String? = null,
    @SerializedName("myList") val myList: String? = null,
    val seasons: List<Season>? = null,
    @SerializedName("episode_number") val episodeNumber: Int? = null,
    @SerializedName("series_name") val seriesName: String? = null,
    @SerializedName("series_slug") val seriesSlug: String? = null,
    @SerializedName("season_number") val seasonNumber: Int? = null,
    @SerializedName("season_name") val seasonName: String? = null,
    @SerializedName("season_slug") val seasonSlug: String? = null,
    @SerializedName("progress_seconds") val progressSeconds: Long? = null,
    @SerializedName("related_content")
    val relatedContent: List<ContentItem>? = null,
    val resumeInfo: ResumeInfo? = null,
    val email: String? = null,
    val phone: String? = null,
    @SerializedName("trailer") val trailer: Trailer? = null,
    @SerializedName("channel_name") val channelName: String? = null,
    @SerializedName("channel_category") val channelCategory: String? = null,
    @SerializedName("channel_banner") val channelBanner: String? = null,
    @SerializedName("channel_description") val channelDescription: String? = null,
    @SerializedName("channel_subscribers") val channelSubscribers: String? = null,
    @SerializedName("profile_img") val creatorProfileImage: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("poster") val poster: String? = null,
    @SerializedName("release_year") val releaseYear: String? = null,
    @SerializedName("order_index") val orderIndex: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("video_type") val videoType: String? = null,
    @SerializedName("download_enable") val downloadEnable: String? = null,
    @SerializedName("subtitle_on_off") val subtitleOnOff: String? = null,
    @SerializedName("subtitle_language1") val subtitleLanguage1: String? = null,
    @SerializedName("subtitle_url1") val subtitleUrl1: String? = null,
    @SerializedName("subtitle_language2") val subtitleLanguage2: String? = null,
    @SerializedName("subtitle_url2") val subtitleUrl2: String? = null,
    val casts: Any? = null,
    val crews: Any? = null,
    @SerializedName("last_watched_at") val lastWatchedAt: String? = null,
    @SerializedName("seasons_count") val seasonsCount: Int? = null,
    @SerializedName("episodes_count") val episodesCount: Int? = null,
    @SerializedName("created_at") val createdAt: String? = null,
)

data class Trailer(
    val id: Int?,
    @SerializedName("play_link") val playLink: String?,
    val thumbnail: String?,
)

data class PillCategory(
    val id: Int?,
    val name: String?,
)
