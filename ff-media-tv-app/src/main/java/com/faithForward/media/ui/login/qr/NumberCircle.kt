package com.faithForward.media.ui.login.qr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun NumberCircle(
    modifier: Modifier = Modifier,
    number: String,
) {

    Box(
        modifier = modifier
            .size(15.dp)
            .clip(
                shape = RoundedCornerShape(50.dp)
            )
            .background(focusedMainColor),
        contentAlignment = Alignment.Center
    ) {

        TitleText(
            text = number,
            color = whiteMain,
            textSize = 10,
            lineHeight = 10,
            fontWeight = FontWeight.Bold
        )

    }

}


@Preview
@Composable
private fun CircelPreview() {
    NumberCircle(
        number = "1"
    )
}

@Preview
@Composable
private fun CircelPrevie2() {
    NumberCircle(
        number = "2"
    )
}


@Preview
@Composable
private fun CircelPreview3() {
    NumberCircle(
        number = "3"
    )
}