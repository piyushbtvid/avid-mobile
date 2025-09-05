package com.faithForward.media.ui.commanComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.rowTitleColor

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Int = 15,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: Int = 15,
    maxLine: Int = 1,
    color: Color = rowTitleColor,
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = fontWeight,
        color = color,
        fontFamily = fontFamily,
        maxLines = maxLine,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight.sp,
        overflow = TextOverflow.Ellipsis,
        fontSize = textSize.sp,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TitlePreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TitleText(
            text = "My List"
        )
    }

}