package com.faithForward.media.ui.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramUiModel
import com.faithForward.media.ui.epg.timeline.TimeLine
import com.faithForward.media.ui.epg.util.currentTimeOffsetDp
import com.faithForward.media.ui.epg.util.generateSampleEpgUiModel
import com.faithForward.media.ui.epg.util.generateTimelineSlots
import com.faithForward.media.ui.epg.util.toDp
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

    // Dynamically compute min and max time range from all programs
    val epgStartTime = allPrograms.minOfOrNull { it.programStartTime } ?: System.currentTimeMillis()
    val epgEndTime = allPrograms.maxOfOrNull { it.programEndTime }
        ?: (System.currentTimeMillis() + 1 * 60 * 60 * 1000)

    val timeSlots = remember(epgStartTime, epgEndTime) {
        generateTimelineSlots(epgStartTime, epgEndTime)
    }

    val programWidthPerMinute = 2.dp // Keep same as used for ProgramBox

    // For drawing the red line
    val now = rememberUpdatedState(System.currentTimeMillis())
    val currentTimeOffsetDp by remember(now, epgStartTime) {
        derivedStateOf {
            val diffInMinutes = ((now.value - epgStartTime).coerceAtLeast(0)) / (60 * 1000)
            programWidthPerMinute * diffInMinutes.toInt()
        }
    }


    // Infinite recomposition for updating the line
    LaunchedEffect(Unit) {
        while (true) {
            delay(60 * 1000) // update every minute
        }
    }

    Box {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimeLine(
                modifier = Modifier
                    .padding(start = 151.5.dp)
                    .horizontalScroll(sharedScrollState),
                timeSlots = timeSlots
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(7.5.dp)
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
        // Red vertical line showing current time
//        Box(
//            modifier = Modifier
//                .padding(start = 151.5.dp)
//                .horizontalScroll(sharedScrollState)
//        ) {
//            Box(
//                modifier = Modifier
//                    .offset(x = currentTimeOffsetDp)
//                    .fillMaxHeight()
//                    .width(2.dp)
//                    .background(Color.Red)
//            )
//        }

    }
}


@Preview(device = "id:tv_1080p")
@Composable
private fun EpgPreview() {
    Epg(
        epgUiModel = generateSampleEpgUiModel()
    )
}