package com.faithForward.media.home.carousel

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.faithForward.media.R
import com.faithForward.media.commanComponents.ContentDescription
import com.faithForward.media.commanComponents.RoundedIconButton
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.playButtonBackgroundColor
import com.faithForward.media.theme.textFocusedMainColor
import com.faithForward.media.util.FocusState

data class CarouselItemDto(
    val id: String? = null,
    val slug: String? = null,
    val imgSrc: String? = null,
    val description: String? = null,
    val releaseDate: String? = null,
    val genre: String? = null,
    val seasons: Int? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
    val title: String? = null,
    val subscribers: String? = null,
    var isCreator: Boolean = false,
    val isLiked: Boolean? = null,
    val isDisliked: Boolean? = null,
    var isFavourite: Boolean? = null,
)

@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    carouselItemDto: CarouselItemDto,
    micModifier: Modifier = Modifier,
    searchIcModifier: Modifier = Modifier,
    addToWatchListModifier: Modifier = Modifier,
    likeModifier: Modifier = Modifier,
    disLikeModifier: Modifier = Modifier,
    addToWatchListUiState: FocusState,
    likeUiState: FocusState,
    dislikeUiState: FocusState,
    micUiState: FocusState,
    searchUiSate: FocusState,
    focusState: FocusState,
    @DrawableRes placeholderRes: Int = R.drawable.preload_placeholder,
) {

    LaunchedEffect(
        carouselItemDto
    ) {
        Log.e(
            "CARSOUEL",
            "carousel item isFav is ${carouselItemDto.isFavourite} and ${carouselItemDto.isLiked}  ${carouselItemDto.isDisliked}"
        )
    }

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
                    width = 1.dp, color = textFocusedMainColor, shape = RoundedCornerShape(40.dp)
                )
        } else {
            Modifier.padding(start = 20.dp, bottom = 20.dp)
        }

    val micButtonModifier =
        if (micUiState == FocusState.FOCUSED || micUiState == FocusState.SELECTED) {
            micModifier
                .shadow(
                    color = Color.White.copy(alpha = .11f),
                    borderRadius = 40.dp,
                    blurRadius = 7.dp,
                    spread = 5.dp,
                )
                .border(
                    width = 1.dp, color = textFocusedMainColor, shape = RoundedCornerShape(40.dp)
                )
        } else {
            micModifier
        }

    val searchButtonModifier =
        if (searchUiSate == FocusState.FOCUSED || searchUiSate == FocusState.SELECTED) {
            searchIcModifier
                .shadow(
                    color = Color.White.copy(alpha = .11f),
                    borderRadius = 40.dp,
                    blurRadius = 7.dp,
                    spread = 5.dp,
                )
                .border(
                    width = 1.dp, color = textFocusedMainColor, shape = RoundedCornerShape(40.dp)
                )
        } else {
            searchIcModifier
        }

    with(carouselItemDto) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(358.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(imgSrc) // fallback if blank
                    .placeholder(placeholderRes).error(placeholderRes).crossfade(true).build(),
                contentDescription = "banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
//                    .offset(y = (-183).dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 10.dp
                        )
                    ),
                alignment = Alignment.TopCenter
            )
            ContentMetaBlock(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, top = 20.dp)
                    .align(alignment = Alignment.TopStart),
                description = description,
                releaseDate = releaseDate,
                genre = genre,
                seasons = seasons,
                duration = duration,
                subscribers = subscribers,
                imdbRating = imdbRating,
                title = title,
                buttonModifier = modifier,
                addToWatchListModifier = addToWatchListModifier,
                likeModifier = likeModifier,
                disLikeModifier = disLikeModifier,
                addToWatchListUiState = addToWatchListUiState,
                likeUiState = likeUiState,
                dislikeUiState = dislikeUiState,
                isLiked = isLiked ?: false,
                isUnLiked = isDisliked ?: false,
                isFavourite = isFavourite ?: false
            )

            title?.let {
                ContentDescription(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(top = 15.dp, end = if (isCreator) 80.dp else 100.dp)
                        .align(Alignment.TopEnd),
                    text = title,
                    textSize = 28,
                    lineHeight = 29,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            RoundedIconButton(
                modifier = buttonModifier.align(alignment = Alignment.BottomStart),
//
                imageId = R.drawable.play_ic,
                iconHeight = 30,
                boxSize = 62,
                iconWidth = 21,
                backgroundColor = playButtonBackgroundColor,
            )

            if (!isCreator) {
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 10.dp, end = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RoundedIconButton(
                        modifier = micButtonModifier,
                        imageId = R.drawable.microphone_ic,
                        iconHeight = 15,
                        boxSize = 43,
                        iconWidth = 15,
                        backgroundColor = Color.White.copy(alpha = .75f)
                    )
                    RoundedIconButton(
                        modifier = searchButtonModifier,
                        imageId = R.drawable.search_ic,
                        iconHeight = 15,
                        boxSize = 43,
                        iconWidth = 15,
                        backgroundColor = Color.White.copy(alpha = .75f)
                    )
                }
            }
        }
    }
}

@PreviewScreenSizes()
@Preview()
@Composable
fun CarouseItemPreview(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
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
            focusState = FocusState.FOCUSED,
            micUiState = FocusState.FOCUSED,
            searchUiSate = FocusState.FOCUSED,
            addToWatchListUiState = FocusState.FOCUSED,
            likeUiState = FocusState.FOCUSED,
            dislikeUiState = FocusState.FOCUSED,
        )

    }

}