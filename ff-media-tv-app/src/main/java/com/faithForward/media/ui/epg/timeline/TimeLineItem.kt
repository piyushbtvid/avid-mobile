package com.faithForward.media.ui.epg.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.theme.programTitleStyle

data class TimeSlotUiModel(
    val timeLabel: String,
    val width: Dp,
)


@Composable
fun TimeLineItem(
    modifier: Modifier = Modifier,
    slot: TimeSlotUiModel,
) {
    Box(
        modifier = modifier
            .width(slot.width),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = slot.timeLabel,
            style = programTitleStyle.copy(fontSize = 12.sp)
        )
    }
}