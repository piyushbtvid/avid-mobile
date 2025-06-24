package com.faithForward.media.commanComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.rowTitleColor

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Int = 15,
    fontWeight: FontWeight = FontWeight.Normal,
    lineHeight: Int = 15,
    color: Color = rowTitleColor,
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = fontWeight,
        color = color,
        maxLines = 1,
        lineHeight = lineHeight.sp,
        overflow = TextOverflow.Ellipsis,
        fontSize = textSize.sp
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