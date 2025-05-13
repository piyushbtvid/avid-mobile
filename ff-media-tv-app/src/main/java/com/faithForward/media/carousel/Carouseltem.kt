package com.faithForward.media.carousel

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.faithForward.media.R
import com.faithForward.media.components.ContentDescription
import com.faithForward.media.components.RoundedIconButton
import com.faithForward.media.extensions.shadow
import com.faithForward.media.ui.theme.btnShadowColor
import com.faithForward.media.ui.theme.playButtonBackgroundColor
import com.faithForward.media.ui.theme.textFocusedMainColor
import com.faithForward.media.util.FocusState

data class CarouselItemDto(
    val imgSrc: String? = null,
    val description: String? = null,
    val releaseDate: String? = null,
    val genre: String? = null,
    val seasons: Int? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
    val title: String? = null
)

@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    carouselItemDto: CarouselItemDto,
    focusState: FocusState,
    @DrawableRes placeholderRes: Int = R.drawable.banner_test_img
) {

    val buttonModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            Modifier
                .padding(start = 20.dp, bottom = 20.dp)
                .shadow(
                    color = Color.White.copy(alpha = .46f),
                    borderRadius = 40.dp,
                    blurRadius = 7.dp,
                    spread = 5.dp,
                )
                .border(
                    width = 1.dp,
                    color = textFocusedMainColor,
                    shape = RoundedCornerShape(40.dp)
                )
        } else {
            modifier.padding(start = 20.dp, bottom = 20.dp)
        }

    with(carouselItemDto) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(358.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(null) // fallback if blank
                    .placeholder(placeholderRes)
                    .error(placeholderRes)
                    .crossfade(true)
                    .build(),
                contentDescription = "banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
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
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp)
                    .align(alignment = Alignment.TopStart),
                description = description,
                releaseDate = releaseDate,
                genre = genre,
                seasons = seasons,
                duration = duration,
                imdbRating = imdbRating,
                title = title
            )

            title?.let {
                ContentDescription(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(top = 15.dp, end = 26.dp)
                        .align(Alignment.TopEnd),
                    text = title,
                    textSize = 28,
                    lineHeight = 29,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            RoundedIconButton(
                modifier = buttonModifier
                    .align(alignment = Alignment.BottomStart)
//
                ,
                imageId = R.drawable.play_ic,
                iconHeight = 30,
                boxSize = 62,
                iconWidth = 21,
                backgroundColor = playButtonBackgroundColor
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
                imdbRating = "4.4",
                title = "khbshbslihbfv"
            ),
            focusState = FocusState.FOCUSED
        )

    }

}