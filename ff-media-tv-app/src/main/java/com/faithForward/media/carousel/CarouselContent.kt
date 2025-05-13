package com.faithForward.media.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.components.ContentDescription

@Composable
fun CarouselContent(
    modifier: Modifier = Modifier,
    description: String? = null,
    releaseDate: String? = null,
    genre: String? = null,
    seasons: Int? = null,
    duration: String? = null,
    imdbRating: String? = null
) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        val metaList = listOfNotNull(
            releaseDate,
            genre,
            seasons?.let { "$it Season${if (it > 1) "s" else ""}" },
            duration,
            imdbRating?.let { "IMDB $it" }
        )


        // Show metadata row only if there's something to display
        if (metaList.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                metaList.forEachIndexed { index, item ->
                    ContentDescription(text = item)
                    if (index < metaList.lastIndex) {
                        Spacer(modifier = Modifier.width(8.dp))
                        ContentDescription(text = "|")
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Description
        description?.let {
            ContentDescription(
                modifier = Modifier
                    .width(289.dp)
                    .height(60.dp),
                text = it,
                lineHeight = 13,
                overflow = TextOverflow.Ellipsis,
                textSize = 12
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
        // Icon buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.plus_icon),
                contentDescription = null
            )    // Replace with actual drawable
            Image(
                painter = painterResource(R.drawable.fi_sr_thumbs_up),
                contentDescription = null
            )
            Image(
                painter = painterResource(R.drawable.fi_sr_thumbs_down),
                contentDescription = null
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarouselPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        CarouselContent()
    }
}
