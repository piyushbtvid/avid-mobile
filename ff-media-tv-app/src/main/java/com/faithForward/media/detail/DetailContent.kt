package com.faithForward.media.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.commanComponents.CategoryCompose
import com.faithForward.media.commanComponents.CategoryComposeDto
import com.faithForward.media.commanComponents.ContentDescription
import com.faithForward.media.home.carousel.ContentMetaBlock
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.textFocusedMainColor
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.media.util.FocusState

data class DetailDto(
    val imgSrc: String? = null,
    val description: String? = null,
    val releaseDate: String? = null,
    val genre: String? = null,
    val seasons: Int? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
    val title: String? = null,
    val subscribers: String? = null,
)

@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    btnFocusRequester: FocusRequester = FocusRequester(),
    contentColor: Color = Color.Black,
    buttonUnfocusedColor: Color = Color.White,
    textUnfocusedColor: Color = textUnFocusColor,
    detailDto: DetailDto,
    contentRowTint: Color = Color.White,
) {

    var addToWatchListUiState by remember { mutableStateOf(FocusState.UNFOCUSED) }
    var likeUiState by remember { mutableStateOf(FocusState.UNFOCUSED) }
    var dislikeUiState by remember { mutableStateOf(FocusState.UNFOCUSED) }

    val isFocused = remember { mutableStateOf(false) }


    val addToWatchListModifier = Modifier
        .onFocusChanged {
            addToWatchListUiState = when {
                it.isFocused -> FocusState.FOCUSED
                it.hasFocus -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }
        }
        .focusable()

    val likeModifier = Modifier
        .onFocusChanged {
            likeUiState = when {
                it.isFocused -> FocusState.FOCUSED
                it.hasFocus -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }
        }
        .focusable()

    val disLikeModifier = Modifier
        .onFocusChanged {
            dislikeUiState = when {
                it.isFocused -> FocusState.FOCUSED
                it.hasFocus -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }
        }
        .focusable()

    with(detailDto) {
        Box(modifier = modifier.fillMaxSize()) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(imgSrc)
                    .error(R.drawable.banner_test_img).crossfade(true).build(),
                contentDescription = "detail Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                textFocusedMainColor,
                                textFocusedMainColor.copy(alpha = 0f),
                                textFocusedMainColor.copy(alpha = 0f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .verticalScroll(rememberScrollState()) // allow scrolling
                    .padding(bottom = 100.dp), // ensure the button has space
                verticalArrangement = Arrangement.spacedBy(34.dp)
            ) {
                ContentMetaBlock(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp)
                        .wrapContentHeight(),
                    description = description,
                    releaseDate = releaseDate,
                    genre = genre,
                    seasons = seasons,
                    duration = duration,
                    subscribers = subscribers,
                    imdbRating = imdbRating,
                    title = title,
                    textColor = contentColor,
                    buttonModifier = modifier,
                    addToWatchListModifier = addToWatchListModifier,
                    likeModifier = likeModifier,
                    disLikeModifier = disLikeModifier,
                    addToWatchListUiState = addToWatchListUiState,
                    likeUiState = likeUiState,
                    dislikeUiState = dislikeUiState,
                    contentRowTint = contentRowTint
                )
                CategoryCompose(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .focusRequester(btnFocusRequester)
                        .onFocusChanged {
                            isFocused.value = it.hasFocus
                        }
                        .focusable(),
                    backgroundUnFocusedColor = buttonUnfocusedColor,
                    textUnFocusedColor = textUnfocusedColor,
                    categoryComposeDto = CategoryComposeDto(btnText = "Watch Now", id = ""),
                    onCategoryItemClick = { id ->
                        // onCategoryItemClick.invoke(id)
                    },
                    focusState = if (isFocused.value) FocusState.FOCUSED else FocusState.UNFOCUSED
                )

                Spacer(modifier = Modifier.height(40.dp)) // padding space at the bottom
            }


            title?.let {
                ContentDescription(
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .padding(top = 15.dp, end = 100.dp)
                        .align(Alignment.TopEnd),
                    text = title,
                    textSize = 28,
                    lineHeight = 29,
                    maxLines = 2,
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        btnFocusRequester.requestFocus()
    }
}


@Preview(
    name = "Landscape Preview", widthDp = 640, heightDp = 360, showBackground = true
)
@Composable
private fun DetailPagePreview() {
    DetailContent(
        detailDto = DetailDto(),
    )
}