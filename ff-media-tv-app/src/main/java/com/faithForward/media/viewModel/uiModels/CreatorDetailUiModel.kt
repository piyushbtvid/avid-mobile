package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.detail.creator_detail.CreatorContentDto
import com.faithForward.media.ui.detail.creator_detail.CreatorDetailDto
import com.faithForward.media.ui.detail.creator_detail.content.ContentDto
import com.faithForward.media.util.formatDurationInReadableFormat
import com.faithForward.network.dto.ContentItem
import com.faithForward.network.dto.creator.CreatorResponse

sealed interface CreatorDetailItem {
    data class CreatorDetail(
        val creatorDetailDto: CreatorDetailDto,
        val contentList: List<ContentDto> = emptyList(),
    ) : CreatorDetailItem
}


fun CreatorResponse.toCreatorDetailDto(): CreatorDetailDto {
    val creatorContentDto = CreatorContentDto(
        about = data.bio,
        subscribersText = "${data.channel_subscribers} Subscribers",
        genre = data.channel_category,
        creatorChannelCategory = data.channel_category,
        channelName = data.channel_name,
        description = data.channel_description
    )

    return CreatorDetailDto(
        creatorName = data.name,
        creatorImageUrl = data.profile_img,
        creatorBackgroundImageUrl = data.channel_banner,
        creatorContentDto = creatorContentDto
    )
}

fun ContentItem.toContentDto(): ContentDto {
    val durationSeconds = duration?.toIntOrNull()
    val viewsCount = views?.toIntOrNull()
    return ContentDto(
        image = portrait ?: "",
        title = name ?: "",
        views = viewsCount?.let { "$it Views" } ?: "",
        duration = formatDurationInReadableFormat(durationSeconds),
        description = description ?: "",
        time = dateUploaded ?: "",
        slug = slug
    )
}