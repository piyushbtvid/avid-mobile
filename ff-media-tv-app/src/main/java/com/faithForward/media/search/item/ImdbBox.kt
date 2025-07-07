package com.faithForward.media.search.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.theme.whiteMain

@Composable
fun ImdbBox(
    modifier: Modifier = Modifier,
    text: String,
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .background(pageBlackBackgroundColor)
            .border(width = 1.dp, color = whiteMain.copy(alpha = 0.7f))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ) {

        TitleText(
            text = text,
            textSize = 15,
            color = whiteMain
        )

    }

}


@Preview
@Composable
private fun ImdbBoxPreview() {

    ImdbBox(
        text = "PG"
    )

}