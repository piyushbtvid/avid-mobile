package com.faithForward.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.theme.focusedMainColor

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Int = 15,
    color: Color = focusedMainColor
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
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