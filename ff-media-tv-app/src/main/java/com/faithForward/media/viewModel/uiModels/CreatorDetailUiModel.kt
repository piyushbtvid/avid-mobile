package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.home.creator.detail.CreatorContentDto
import com.faithForward.media.home.creator.detail.CreatorDetailDto
import com.faithForward.network.dto.creator.CreatorResponse

sealed interface CreatorDetailItem {
    data class CreatorDetail(val creatorDetailDto: CreatorDetailDto) : CreatorDetailItem
}

fun CreatorResponse.toCreatorDetailDto(): CreatorDetailDto {
    val creatorContentDto = CreatorContentDto(
        about = data.bio,
        subscribersText = "${data.channel_subscribers} Subscribers",
        genre = data.channel_category,
        creatorChannelCategory = data.channel_category,
    )

    return CreatorDetailDto(
        creatorName = data.name,
        creatorImageUrl = data.profile_img,
        creatorBackgroundImageUrl = data.channel_banner,
        creatorContentDto = creatorContentDto
    )
}