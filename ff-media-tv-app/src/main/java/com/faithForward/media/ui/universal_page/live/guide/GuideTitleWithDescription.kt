package com.faithForward.media.ui.universal_page.live.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun GuideTitleWithDescription(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
) {

    Column(
        modifier = modifier.width(289.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        TitleText(
            text = title,
            textSize = 20,
            lineHeight = 20,
            fontWeight = FontWeight.Medium,
            color = whiteMain
        )

        TitleText(
            text = description,
            textSize = 10,
            lineHeight = 10,
            fontWeight = FontWeight.Normal,
            color = whiteMain,
            maxLine = 3
        )

    }

}