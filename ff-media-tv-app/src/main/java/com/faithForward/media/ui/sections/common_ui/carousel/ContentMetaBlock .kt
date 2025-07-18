package com.faithForward.media.ui.sections.common_ui.carousel

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.ContentDescription
import com.faithForward.media.ui.theme.descriptionTextStyle
import com.faithForward.media.ui.theme.metaDataTextStyle
import com.faithForward.media.ui.theme.selectedMainColor
import com.faithForward.media.ui.theme.textFocusedMainColor
import com.faithForward.media.ui.theme.titleTextStyle
import com.faithForward.media.util.FocusState

@Composable
fun ContentMetaBlock(
    modifier: Modifier = Modifier,
    description: String? = null,
    releaseDate: String? = null,
    genre: String? = null,
    seasons: Int? = null,
    duration: String? = null,
    imdbRating: String? = null,
    subscribers: String? = null,
    title: String?,
    isFavourite: Boolean = false,
    isLiked: Boolean = false,
    isUnLiked: Boolean = false,
    isCreator: Boolean = false,
    textColor: Color = Color.White,
    addToWatchListModifier: Modifier = Modifier,
    likeModifier: Modifier = Modifier,
    disLikeModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    addToWatchListUiState: FocusState = FocusState.UNDEFINED,
    likeUiState: FocusState = FocusState.UNDEFINED,
    dislikeUiState: FocusState = FocusState.UNDEFINED,
    contentRowTint: Color = Color.White,
) {
    LaunchedEffect(addToWatchListUiState) {
        Log.e(
            "UTIL",
            "util list in carouselContent is $releaseDate $genre  $seasons $duration $subscribers $imdbRating $addToWatchListUiState"
        )
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            val metaList = listOfNotNull(releaseDate?.takeIf { it.isNotBlank() },
                genre?.takeIf { it.isNotBlank() },
                seasons?.let { "$it Season${if (it > 1) "s" else ""}" }?.takeIf { it.isNotBlank() },
                duration?.takeIf { it.isNotBlank() },
                subscribers?.takeIf { it.isNotBlank() },
                imdbRating?.let { "IMDB $it" }?.takeIf { it.isNotBlank() })

            if (metaList.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    metaList.forEachIndexed { index, item ->
                        if (index > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                            ContentDescription(
                                text = "|", textStyle = metaDataTextStyle
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        ContentDescription(text = item, textStyle = metaDataTextStyle)
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))
            }

            title?.let {
                ContentDescription(
                    modifier = Modifier
                        .width(400.dp),
                    text = title,
                    textStyle = titleTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(9.dp))
            }

            description?.let {
                ContentDescription(
                    modifier = Modifier
                        .width(289.dp)
                        .height(60.dp),
                    text = it,
                    textStyle = descriptionTextStyle,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if (addToWatchListUiState != FocusState.UNDEFINED && likeUiState != FocusState.UNDEFINED && dislikeUiState != FocusState.UNDEFINED && !isCreator) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Image(
                            modifier = addToWatchListModifier,
                            painter = painterResource(if (isFavourite) R.drawable.subtract else R.drawable.vector),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                if (addToWatchListUiState == FocusState.FOCUSED || addToWatchListUiState == FocusState.SELECTED) textFocusedMainColor
                                else contentRowTint
                            )
                        )
                    }
                    Image(
                        modifier = likeModifier,
                        painter = painterResource(R.drawable.fi_sr_thumbs_up),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            when {
                                isLiked -> selectedMainColor
                                likeUiState == FocusState.FOCUSED || likeUiState == FocusState.SELECTED -> textFocusedMainColor
                                else -> contentRowTint
                            }
                        )
                    )
                    Image(
                        modifier = disLikeModifier,
                        painter = painterResource(R.drawable.fi_sr_thumbs_down),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            when {
                                isUnLiked -> selectedMainColor
                                dislikeUiState == FocusState.FOCUSED || dislikeUiState == FocusState.SELECTED -> textFocusedMainColor
                                else -> contentRowTint
                            }
                        )
                    )
                }
            }


            Box(
                modifier = buttonModifier
                    .size(10.dp)
                    .background(color = Color.Transparent)
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
        ContentMetaBlock(
            title = "",
            addToWatchListUiState = FocusState.FOCUSED,
            likeUiState = FocusState.FOCUSED,
            dislikeUiState = FocusState.SELECTED,
        )
    }
}
