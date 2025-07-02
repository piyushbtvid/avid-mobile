package com.faithForward.media.player.seriesNextUi

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.theme.whiteMain
import kotlinx.coroutines.delay


@Composable
fun UpNextProgress(
    initialTime: Int = 20,
    modifier: Modifier = Modifier
) {
    val timeLeft = remember { Animatable(initialTime.toFloat()) }

    LaunchedEffect(Unit) {
        for (second in initialTime downTo 0) {
            timeLeft.snapTo(second.toFloat()) // Instant visual update
            delay(1000) // Wait for 1 second
        }
    }

    val displayText = if (timeLeft.value.toInt() == 0) {
        "Playing now"
    } else {
        "Playing next in ${timeLeft.value.toInt()} seconds"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Up Next",
            color = whiteMain,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        CircularProgressbar(
            name = timeLeft.value.toInt().toString(),
            dataUsage = (timeLeft.value / initialTime) * 100,
            foregroundIndicatorColor = Color(0xFF1DA1F2),
            shadowColor = Color.Gray
        )

        Text(
            text = displayText,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.7f)
            )
        )
    }
}


@Preview
@Composable
private fun UpNextPreview() {

    UpNextProgress()

}