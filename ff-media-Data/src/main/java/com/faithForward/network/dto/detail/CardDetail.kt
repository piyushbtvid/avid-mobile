package com.faithForward.network.dto.detail

import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.Creator
import com.faithForward.network.dto.Genre

data class CardDetail(
    val status: String,

    val message: String,

    val data: ContentItem,
)

data class ResumeInfo(
    val content_type: String,
    val id: Int,
    val slug: String,
    val name: String,
    val episode_number: Int?,
    val series_name: String?,
    val series_slug: String?,
    val season_number: Int?,
    val season_name: String?,
    val season_slug: String?,
    val description: String?,
    val portrait: String?,
    val landscape: String?,
    val views: Int?,
    val rating: String?,
    val genres: List<Genre>?,
    val duration: Long,
    val dateUploaded: String?,
    val uploadedYear: String?,
    val access: String?,
    val creator: Creator?,
    val video_link: String?,
    val likeDislike: String?,
    val progress_seconds: Long?,
    val last_watched_at: String?,
)
