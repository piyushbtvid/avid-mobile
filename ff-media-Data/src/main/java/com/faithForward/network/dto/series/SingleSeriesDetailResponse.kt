package com.faithForward.network.dto.series

import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.Creator
import com.faithForward.network.dto.Genre

data class SingleSeriesDetailResponse(
    val status: String,
    val message: String,
    val data: ContentItem
)


data class Season(
    val id: String,
    val name: String,
    val season_number: Int,
    val description: String? = null,
    val portrait: String,
    val landscape: String,
    val episodes: List<Episode>
)


data class Episode(
    val id: String,
    val episode_number: Int,
    val name: String,
    val description: String,
    val portrait: String,
    val landscape: String,
    val views: Int,
    val rating: String,
    val genres: List<Genre>,
    val duration: Int,
    val upload_type: String,
    val dateUploaded: String,
    val uploadedYear: String,
    val access: String,
    val creator: Creator
)