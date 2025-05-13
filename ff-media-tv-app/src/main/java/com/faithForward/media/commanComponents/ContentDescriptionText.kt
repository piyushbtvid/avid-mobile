package com.faithForward.media.commanComponents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun ContentDescription(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Int = 10,
    lineHeight: Int = 10,
    color: Color = Color.White,
    maxLines: Int? = null,
    overflow: TextOverflow? = null,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = textSize.sp,
        lineHeight = lineHeight.sp,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = overflow ?: TextOverflow.Clip,
        textAlign = textAlign,
        fontWeight = fontWeight
    )
}
