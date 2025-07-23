package com.faithForward.media.ui.epg.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimeLine(
    modifier: Modifier = Modifier,
    timeSlots: List<TimeSlotUiModel>,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        timeSlots.forEach { slot ->
            TimeLineItem(slot = slot)
        }
    }
}