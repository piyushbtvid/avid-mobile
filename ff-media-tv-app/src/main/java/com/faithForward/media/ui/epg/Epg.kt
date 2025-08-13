package com.faithForward.media.ui.epg

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.epg.timeline.TimeLine
import com.faithForward.media.ui.epg.util.generateTimeSlots
import kotlinx.coroutines.delay

data class EpgUiModel(
    val channelWithProgramsUiModels: List<ChannelWithProgramsUiModel>,
    val currentTime: Long = System.currentTimeMillis(),
)

@Composable
fun Epg(
    modifier: Modifier = Modifier,
    epgUiModel: EpgUiModel,
) {
    val sharedScrollState = rememberScrollState()
    val allPrograms = epgUiModel.channelWithProgramsUiModels.flatMap { it.programs }

    // Compute EPG start and end time
    val epgStartTime = allPrograms.minOfOrNull { it.programStartTime }
        ?: System.currentTimeMillis()
    val epgEndTime = allPrograms.maxOfOrNull { it.programEndTime }
        ?: (System.currentTimeMillis() + 1 * 60 * 60 * 1000)

    val timeSlots = remember(epgStartTime, epgEndTime) {
        generateTimeSlots(epgStartTime, epgEndTime)
    }

    val programWidthPerMinute = 2.dp

    // State for red line position
    val now = remember { mutableStateOf(System.currentTimeMillis()) }
    val currentTimeOffsetDp by remember(epgStartTime, now.value) {
        derivedStateOf {
            val diffInMinutes = ((now.value - epgStartTime).coerceAtLeast(0)) / (60 * 1000)
            programWidthPerMinute * diffInMinutes.toInt()
        }
    }

    // Update current time every minute
    LaunchedEffect(Unit) {
        while (true) {
            now.value = System.currentTimeMillis()
            delay(60 * 1000)
        }
    }

    // Timeline row
    Box(
        modifier = modifier
    ) {
        TimeLine(
            modifier = Modifier
                .padding(start = 151.5.dp)
                .horizontalScroll(sharedScrollState),
            timeSlots = timeSlots,
            startTimeMillis = epgStartTime,
            dpPerMinute = 2.dp,
        )

        LazyColumn(
            modifier = Modifier.padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(7.5.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            itemsIndexed(epgUiModel.channelWithProgramsUiModels) { index, channelWithProgram ->
                ChannelWithPrograms(
                    channelWithProgramsUiModel = channelWithProgram,
                    isFirstRow = index == 0,
                    horizontalScrollState = sharedScrollState
                )
            }
        }
    }
}


@Preview(device = "id:tv_1080p")
@Composable
private fun EpgPreview() {
//    Epg(
//        epgUiModel = generateSampleEpgUiModel()
//    )
}