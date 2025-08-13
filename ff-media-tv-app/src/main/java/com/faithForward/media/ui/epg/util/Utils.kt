package com.faithForward.media.ui.epg.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.faithForward.media.ui.epg.ChannelWithProgramsUiModel
import com.faithForward.media.ui.epg.EpgUiModel
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramUiModel
import com.faithForward.media.ui.epg.timeline.TimeSlotUiModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random
fun calculateProgramWidth(
    startTime: Long,
    endTime: Long
): Dp {
    val durationMinutes = ((endTime - startTime) / 60_000).coerceAtLeast(1)
    return durationMinutes.toFloat() * DpPerMinute
}


private val DpPerMinute = 2.dp

fun generateTimeSlots(
    startTime: Long,
    endTime: Long,
    stepMinutes: Int = 2 * 60
): List<TimeSlotUiModel> {
    val slots = mutableListOf<TimeSlotUiModel>()
    var current = startTime

    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    while (current < endTime) {
        val next = current + stepMinutes * 60_000
        val width = ((next - current) / 60_000).toFloat() * DpPerMinute
        val label = formatter.format(Date(current))
        slots.add(TimeSlotUiModel(label, width))
        current = next
    }
    return slots
}


fun generateSampleEpgUiModel(): EpgUiModel {
    val channelCount = 10
    val programsPerChannel = 100 // Increased for full coverage
    val random = Random(System.currentTimeMillis())

    val startTime = System.currentTimeMillis() - (3 * 60 * 60 * 1000) // 3 hours ago
    val epgWindowMillis = 20 * 60 * 60 * 1000 // 20 hours
    val epgEndTime = startTime + epgWindowMillis

    val channelWithPrograms = (1..channelCount).map { channelIndex ->
        var currentStartTime = startTime
        val programs = mutableListOf<ProgramUiModel>()

        repeat(programsPerChannel) { i ->
            if (currentStartTime >= epgEndTime) return@repeat

            val durationMinutes = random.nextInt(20, 120) // between 20 and 120 minutes
            val durationMillis = durationMinutes * 60 * 1000
            val programEndTime = (currentStartTime + durationMillis).coerceAtMost(epgEndTime)

            if (programEndTime > currentStartTime) {
                programs.add(
                    ProgramUiModel(
                        programName = "Program ${i + 1}",
                        programTimeString = formatProgramTime(currentStartTime, programEndTime),
                        programStartTime = currentStartTime,
                        programEndTime = programEndTime
                    )
                )
                currentStartTime = programEndTime
            }
        }

        // ✅ Force-fill the gap at the end if needed
        if (currentStartTime < epgEndTime) {
            programs.add(
                ProgramUiModel(
                    programName = "NO PROGRAM",
                    programTimeString = formatProgramTime(currentStartTime, epgEndTime),
                    programStartTime = currentStartTime,
                    programEndTime = epgEndTime
                )
            )
        }

        ChannelWithProgramsUiModel(
            channelUiModel = ChannelUiModel(
                channelName = "Channel $channelIndex",
                channelImage = ""
            ),
            programs = programs
        )
    }

    return EpgUiModel(
        channelWithProgramsUiModels = channelWithPrograms,
        currentTime = System.currentTimeMillis()
    )
}


fun formatProgramTime(start: Long, end: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return "${sdf.format(Date(start))} - ${sdf.format(Date(end))}"
}

@Composable
fun Int.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@toDp.toDp() }
}

fun currentTimeOffsetDp(
    epgStartTimeMillis: Long,
    currentTimeMillis: Long,
    dpPerMinute: Float
): Dp {
    val minutesSinceStart = (currentTimeMillis - epgStartTimeMillis).coerceAtLeast(0) / 60000f
    return (minutesSinceStart * dpPerMinute).dp
}


