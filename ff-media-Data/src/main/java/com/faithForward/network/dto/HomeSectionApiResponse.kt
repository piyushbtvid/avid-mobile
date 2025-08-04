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
    val content_type: String?,
    val id: Int?,
    val slug: String?,
    val name: String?,
    val description: String?,
    val portrait: String?,
    val landscape: String?,
    val genres: List<Genre>?,
    val rating: String?,
    val dateUploaded: String?,
    val uploadedYear: String?,
    var access: String? = null,
    var video_link: String? = null,
    var views: Int? = null,
    var creator: Creator? = null,
    var duration: Int? = null,
    val likeDislike: String? = null,
    val myList: Boolean? = null,
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
)

data class Trailer(
    val id: Int,
    @SerializedName("play_link") val playLink: String,
    val thumbnail: String,
)
