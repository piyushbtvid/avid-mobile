package com.faithForward.media.ui.player.seriesNextUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R

data class EpisodeNextUpItemDto(
    val episodeSlug: String,
    val seriesSlug: String,
    val image: String,
    val title: String,
    val description: String,
    val seasonEpisode: String,
)

@Composable
fun EpisodeNextUpItem(
    modifier: Modifier = Modifier,
    episodeNextUpItemDto: EpisodeNextUpItemDto,
) {

    with(episodeNextUpItemDto) {
        Row(
            modifier = modifier.wrapContentSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .error(R.drawable.banner_test_img)
                    .crossfade(true)
                    .build(),
                contentDescription = "Poster Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(170.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(interactionSource = null, indication = null, onClick = {
                        //onItemClick.invoke()
                    }
                    )
            )

            EpisodeNextUpItemContent(
                title = title,
                description = description,
                seasonEpisode = seasonEpisode,
            )

        }
    }


}

@Preview(
    name = "Episode Next Up - Landscape",
    widthDp = 1920,
    heightDp = 1080,
    showBackground = true
)
@Composable
private fun EpisodeNextUpItemPreview() {

    EpisodeNextUpItem(
        episodeNextUpItemDto = EpisodeNextUpItemDto(
            image = "",
            title = "The Last Ride",
            description = "The Last Ride The Last Ride The Last Ride The Last Ride The Last RideThe Last Ride ",
            seasonEpisode = "Season 2, Episode 8",
            episodeSlug = "",
            seriesSlug = ""
        )
    )

}