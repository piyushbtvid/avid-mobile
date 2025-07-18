package com.faithForward.media.ui.player.seriesNextUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun EpisodeNextUpItemContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    seasonEpisode: String,
) {


    Column(
        modifier = modifier.wrapContentSize()
    ) {

        TitleText(
            text = title,
            textSize = 18,
            lineHeight = 18,
            fontWeight = FontWeight.Bold,
            color = whiteMain,
        )

        Spacer(modifier = Modifier.height(8.dp))

        TitleText(
            text = seasonEpisode,
            textSize = 14,
            lineHeight = 14,
            fontWeight = FontWeight.W400,
            color = whiteMain.copy(alpha = .9f),
        )

        Spacer(modifier = Modifier.height(10.dp))

        TitleText(
            modifier = Modifier.widthIn(max = 450.dp),
            text = description,
            maxLine = 2,
            textSize = 14,
            lineHeight = 14,
            fontWeight = FontWeight.Normal,
            color = whiteMain.copy(alpha = .7f),
        )


    }

}


@Preview
@Composable
private fun ContentPreview() {

    EpisodeNextUpItemContent(
        title = "The Last Ride",
        description = "The Last Ride The Last Ride The Last Ride The Last Ride The Last RideThe Last Ride The Last Ride",
        seasonEpisode = "Season 2, Episode 8"
    )

}