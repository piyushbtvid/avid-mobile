package com.faithForward.media

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun ContentDescription(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Int = 10,
    color: Color = Color.White
) {

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = textSize.sp
    )

}