package com.faithForward.media.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.faithForward.media.theme.detailGradientEndColor
import com.faithForward.media.theme.detailGradientStartColor
import com.faithForward.media.theme.textFocusedMainColor

@Composable
fun DetailGradient(
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        detailGradientStartColor,
                        detailGradientStartColor.copy(alpha = 0.5f),
                        detailGradientEndColor
                    )
                )
            )
    )

}