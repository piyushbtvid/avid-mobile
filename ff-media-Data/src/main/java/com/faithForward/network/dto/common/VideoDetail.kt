package com.faithForward.network.dto.common

import com.google.gson.annotations.SerializedName

data class VideoDetail(
    @SerializedName("video_thumbnail_image")
    val videoThumbnailImage: String,
    @SerializedName("hls_playlist_url")
    val hlsPlaylistUrl: String,
    @SerializedName("preview_animation_url")
    val previewAnimationUrl: String,
    @SerializedName("video_title")
    val videoTitle: String,
    @SerializedName("play_link")
    val playLink: String,
    @SerializedName("play_url")
    val playUrl: String
)