package com.faithForward.media.player.seriesNextUi

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.theme.whiteMain

@Composable
fun CircularProgressbar(
    name: String = "",
    size: Dp = 30.dp,
    foregroundIndicatorColor: Color = MaterialTheme.colors.primary,
    shadowColor: Color = Color.Gray,
    indicatorThickness: Dp = 4.dp,
    dataUsage: Float = 60f,
    animationDuration: Int = 12000,
    dataTextStyle: TextStyle = TextStyle(fontSize = 10.sp),
) {
    var dataUsageRemember by remember { mutableFloatStateOf(-1f) }

    val dataUsageAnimate = animateFloatAsState(
        targetValue = dataUsageRemember,
        animationSpec = tween(durationMillis = animationDuration),
        label = ""
    )

    LaunchedEffect(Unit) {
        dataUsageRemember = dataUsage
    }

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = this.size // <-- actual canvas size in pixels
            val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
            val radius = canvasSize.minDimension / 2

            // Shadow circle
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(shadowColor.copy(alpha = 0.4f), Color.Transparent),
                    center = center,
                    radius = radius
                ),
                radius = radius,
                center = center
            )

            // Background circle
            drawCircle(
                color = pageBlackBackgroundColor,
                radius = radius - indicatorThickness.toPx() / 2,
                center = center
            )

            // Foreground arc
            val sweepAngle = dataUsageAnimate.value * 3.6f
            val arcSize = Size(
                width = canvasSize.width - indicatorThickness.toPx(),
                height = canvasSize.height - indicatorThickness.toPx()
            )

            drawArc(
                color = foregroundIndicatorColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(
                    width = indicatorThickness.toPx(),
                    cap = StrokeCap.Round
                ),
                size = arcSize,
                topLeft = Offset(
                    x = indicatorThickness.toPx() / 2,
                    y = indicatorThickness.toPx() / 2
                )
            )
        }

        // Center text
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                color = whiteMain,
                style = dataTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
//            Text(
//                text = "${dataUsageAnimate.value.toInt()}%",
//                style = dataTextStyle,
//                color = whiteMain
//            )
        }
    }
}

