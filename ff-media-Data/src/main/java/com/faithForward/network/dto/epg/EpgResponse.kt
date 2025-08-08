package com.faithForward.network.dto.epg

import com.google.gson.annotations.SerializedName


data class EpgResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("response")
    val response: ApiResponse
)

/**
 * A data class containing the main content of the API response,
 * including categories and advertisements.
 */
data class ApiResponse(
    @SerializedName("categories")
    val categories: List<Category>,

    @SerializedName("advertisements")
    val advertisements: List<Any> // Type is 'Any' as the list is empty in the example
)

/**
 * Represents a single category which contains various channels.
 */
data class Category(
    @SerializedName("name")
    val name: String,

    @SerializedName("banners")
    val banners: List<Any>, // Type is 'Any' as the list is empty in the example

    @SerializedName("channels")
    val channels: List<Any>, // Type is 'Any' as the list is empty in the example

    @SerializedName("stream_channels")
    val streamChannels: List<StreamChannel>
)

/**
 * Represents a single streamable channel with its details and list of broadcasts.
 */
data class StreamChannel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("hls")
    val hls: Boolean,

    @SerializedName("name")
    val name: String,

    @SerializedName("number")
    val number: Int,

    @SerializedName("private")
    val private: Boolean,

    @SerializedName("broadcasts")
    val broadcasts: List<Broadcast>,

    @SerializedName("banner_url")
    val bannerUrl: String?, // Nullable as it can be null

    @SerializedName("thumbnail")
    val thumbnail: String,

    @SerializedName("is_favorites")
    val isFavorites: Boolean,

    @SerializedName("is_youtube")
    val isYoutube: Boolean,

    @SerializedName("source_url")
    val sourceUrl: String
)

/**
 * Represents a single broadcast (e.g., a TV show episode or movie) within a channel.
 */
data class Broadcast(
    @SerializedName("id")
    val id: Long, // Using Long for larger numbers is safer

    @SerializedName("title")
    val title: String,

    @SerializedName("ads")
    val ads: List<Any>, // Type is 'Any' as the list is empty in the example

    @SerializedName("blockAds")
    val blockAds: List<Any>, // Type is 'Any' as the list is empty in the example

    @SerializedName("orderedAds")
    val orderedAds: List<Any>, // Type is 'Any' as the list is empty in the example

    @SerializedName("description")
    val description: String,

    @SerializedName("view_duration")
    val viewDuration: Int,

    @SerializedName("stream_duration")
    val streamDuration: Int,

    @SerializedName("view_start_at_iso")
    val viewStartAtIso: String,

    @SerializedName("stream_start_at_iso")
    val streamStartAtIso: String
)