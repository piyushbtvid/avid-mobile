package com.faithForward.media.carousel

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.faithForward.media.R

data class CarouselItemDto(
    val imgSrc: String? = null,
    val description: String? = null,
    val releaseDate: String? = null,
    val genre: String? = null,
    val seasons: Int? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
)

@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    carouselItemDto: CarouselItemDto,
    @DrawableRes placeholderRes: Int = R.drawable.test_poster
) {

    with(carouselItemDto) {
        Box(
            modifier = modifier
                .size(width = 945.dp, height = 358.dp)

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imgSrc?.ifBlank { null }) // fallback if blank
                    .placeholder(placeholderRes)
                    .error(placeholderRes)
                    .crossfade(true)
                    .build(),
                contentDescription = "banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(945.dp)
                    .height(358.dp)
//                    .offset(y = (-183).dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 10.dp
                        )
                    ),
                alignment = Alignment.TopCenter

            )
            CarouselContent(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart),
                description = description,
                genre = genre,
                seasons = seasons,
                duration = duration,
                imdbRating = imdbRating
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarouseItemPreview(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CarouselItem(
            carouselItemDto = CarouselItemDto(
                description = "The crafty Bad Guys crew embarks on a high-stakes Halloween heist to swipe a priceless amulet from a spooky mansion. What could go wrong?",
                genre = "jgvsdvc",
                seasons = 5,
                duration = "2.5h",
                imdbRating = "4.4"
            )
        )

    }

}