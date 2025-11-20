package com.faithForward.media.ui.player.seriesNextUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.pageBlackBackgroundColor


@Composable
fun EpisodeNextUpDialog(
    modifier: Modifier = Modifier,
    time: String,
    onCancelClick: () -> Unit,
    onPlayNowClick: () -> Unit,
    episodeNextUpItemDto: EpisodeNextUpItemDto,
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(pageBlackBackgroundColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        UpNextProgress()


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EpisodeNextUpItem(
                episodeNextUpItemDto = episodeNextUpItemDto
            )

            EpisodeNextButtonsRow(
                onCancelClick = onCancelClick,
                onPlayNowClick = onPlayNowClick
            )
        }

    }


}

@Preview(
    name = "Episode Next Up",
    widthDp = 1920,
    heightDp = 250,
    showBackground = true
)
@Composable
private fun NextUpPreview() {

    EpisodeNextUpDialog(
        time = "",
        episodeNextUpItemDto = EpisodeNextUpItemDto(
            image = "",
            title = "The Last Ride",
            description = "The Last Ride The Last Ride The Last Ride The Last Ride The Last RideThe Last Ride ",
            seasonEpisode = "Season 2, Episode 8",
            episodeSlug = "",
            seriesSlug = ""
        ),
        onCancelClick = {

        },
        onPlayNowClick = {

        }
    )

}