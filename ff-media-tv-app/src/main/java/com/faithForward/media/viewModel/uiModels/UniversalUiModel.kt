package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.epg.ChannelWithProgramsUiModel
import com.faithForward.media.ui.epg.EpgUiModel
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramUiModel
import com.faithForward.network.dto.epg.Broadcast
import com.faithForward.network.dto.epg.Category
import com.faithForward.network.dto.epg.StreamChannel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun mapToEpgUiModel(categoryList: List<Category>?): EpgUiModel {
    val firstCategory = categoryList?.firstOrNull()

    val channelWithProgramsUiModels = firstCategory?.streamChannels?.map { streamChannel ->
        val channelUiModel = streamChannel.toChannelUiModel()
        val programs = streamChannel.broadcasts.map { it.toProgramUiModel() }
        ChannelWithProgramsUiModel(
            channelUiModel = channelUiModel,
            programs = programs
        )
    } ?: emptyList()

    return EpgUiModel(
        channelWithProgramsUiModels = channelWithProgramsUiModels,
        currentTime = System.currentTimeMillis()
    )
}

fun mapToEpgUiModelWithSingleBroadcast(categoryList: List<Category>?): EpgUiModel {
    //  val firstCategory = categoryList?.firstOrNull()

    val secondCategory = categoryList?.getOrNull(1)

    val channelWithProgramsUiModels = secondCategory?.streamChannels?.map { streamChannel ->
        val channelUiModel = streamChannel.toChannelUiModel()
        val firstBroadcast = streamChannel.broadcasts.firstOrNull()
        val programs = if (firstBroadcast != null) {
            listOf(firstBroadcast.toProgramUiModel())
        } else {
            emptyList()
        }
        ChannelWithProgramsUiModel(
            channelUiModel = channelUiModel,
            programs = programs
        )
    } ?: emptyList()

    return EpgUiModel(
        channelWithProgramsUiModels = channelWithProgramsUiModels,
        currentTime = System.currentTimeMillis()
    )
}


fun Category.toCategoryComposeDto(): CategoryComposeDto {
    return CategoryComposeDto(
        btnText = name,
        id = name
    )
}


fun Broadcast.toProgramUiModel(): ProgramUiModel {
    val startMillis = parseIsoTimeToMillis(streamStartAtIso)
    val endMillis = calculateEndTimeMillis(streamStartAtIso, streamDuration)

    return ProgramUiModel(
        programName = title,
        programTimeString = formatProgramTime(startMillis, endMillis),
        programStartTime = startMillis,
        programEndTime = endMillis,
    )
}


fun StreamChannel.toChannelUiModel(): ChannelUiModel {

    return ChannelUiModel(
        channelName = name,
        channelImage = bannerUrl
            ?: "https://covers.logoscdn.com/vod/covers/faithlife-tv-cover-landscape-hd-550x312.png"
    )

}


fun parseIsoTimeToMillis(isoTime: String): Long {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = sdf.parse(isoTime)
    return date?.time ?: 0L
}

fun calculateEndTimeMillis(isoStartTime: String, durationSeconds: Int): Long {
    val startMillis = parseIsoTimeToMillis(isoStartTime)
    return startMillis + durationSeconds * 1000L
}

fun formatProgramTime(startMillis: Long, endMillis: Long): String {
    val formatter = SimpleDateFormat("hh:mma", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault() // Use user's local time zone

    val startTime = formatter.format(Date(startMillis))
    val endTime = formatter.format(Date(endMillis))

    return "$startTime - $endTime".lowercase()
}
