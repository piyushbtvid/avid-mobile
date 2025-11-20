package com.faithForward.media.ui.epg.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TimeLine(
    modifier: Modifier = Modifier,
    timeSlots: List<TimeSlotUiModel>,
    startTimeMillis: Long,
    dpPerMinute: Dp,
) {
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Update current time every 1 minute
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000) // 1 minute
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    val nowOffset = calculateNowOffset(startTimeMillis, currentTimeMillis, dpPerMinute)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight() // same height as timeline items
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            timeSlots.forEach { slot ->
                TimeLineItem(slot = slot)
            }
        }

        // Now line inside the timeline
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 30.dp)
                .width(1.5.dp)
                .offset(x = nowOffset)
                .background(Color.Gray)
        )
    }
}
fun calculateNowOffset(
    startTimeMillis: Long,
    nowMillis: Long,
    dpPerMinute: Dp,
): Dp {
    val minutesFromStart = (nowMillis - startTimeMillis) / 60000 // ms to minutes
    return (minutesFromStart * dpPerMinute.value).dp
}
