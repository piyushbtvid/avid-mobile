package com.faithForward.media.ui.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.Util.isTvDevice
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.faithForward.media.ui.commanComponents.CategoryCompose
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.sections.common_ui.carousel.ContentMetaBlock
import com.faithForward.media.ui.theme.blackColor
import com.faithForward.media.ui.theme.detailNowTextStyle
import com.faithForward.media.ui.theme.detailNowUnFocusTextStyle
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState
import com.faithForward.media.util.rememberIsTvDevice

data class DetailDto(
    val id: String? = null,
    val slug: String?,
    val imgSrc: String? = null,
    val landScape: String? = null,
    val description: String? = null,
    val releaseDate: String? = null,
    val genre: String? = null,
    val seasons: Int? = null,
    var isFavourite: Boolean? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
    val title: String? = null,
    val subscribers: String? = null,
    val videoLink: String? = null,
    val isLiked: Boolean? = null,
    val isDisliked: Boolean? = null,
    val isSeries: Boolean? = null,
    val contentType: String? = null,
    val progress: Long? = null,
    val relatedList: List<PosterCardDto>? = null,
    val itemTrailer: String? = null,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    playNowBtnFocusRequester: FocusRequester = FocusRequester(),
    resumeBtnFocusRequester: FocusRequester = FocusRequester(),
    playTrailerFocusRequester: FocusRequester = FocusRequester(),
    onWatchNowClick: (String) -> Unit,
    onResumeNowCLick: () -> Unit,
    onPlayTrailer: () -> Unit,
    onWatchNowFocusChange: (Boolean) -> Unit,
    onResumeNowFocusChange: (Boolean) -> Unit,
    onPlayTrailerFocusChange: (Boolean) -> Unit,
    isContentVisible: Boolean = true,
    isResumeVisible: Boolean = false,
    isPlayTrailerButtonShow: Boolean = false,
    detailDto: DetailDto,
    resumeNowTxt: String = "Resume Now",
    onToggleFavorite: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleDisLike: () -> Unit,
) {
    val isTv = rememberIsTvDevice()
    var addToWatchListUiState by remember { mutableStateOf(FocusState.UNFOCUSED) }
    var likeUiState by remember { mutableStateOf(FocusState.UNFOCUSED) }
    var dislikeUiState by remember { mutableStateOf(FocusState.UNFOCUSED) }
    var isPlayFocused by remember { mutableStateOf(false) }
    var isResumeFocused by remember { mutableStateOf(false) }
    var isPlayTrailerFocused by remember { mutableStateOf(false) }


    val targetAlpha by animateFloatAsState(
        targetValue = if (isContentVisible) 1f else 0f, animationSpec = tween(durationMillis = 500)
    )

    val addToWatchListModifier = if (isTv) {
        Modifier
            .onFocusChanged {
                addToWatchListUiState = when {
                    it.isFocused -> FocusState.FOCUSED
                    it.hasFocus -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }
            }
            .clickable(interactionSource = null, indication = null, onClick = {
                onToggleFavorite()
            })
            .focusable()
    } else {
        Modifier
            .clickable(interactionSource = null, indication = null, onClick = {
                onToggleFavorite()
            })
    }

    val likeModifier = if (isTv) {
        Modifier
            .onFocusChanged {
                likeUiState = when {
                    it.isFocused -> FocusState.FOCUSED
                    it.hasFocus -> FocusState.SELECTED
                    else -> FocusState.UNFOCUSED
                }
            }
            .clickable(interactionSource = null, indication = null, onClick = {
                onToggleLike()
            })
            .focusable()
    } else {
        Modifier
            .clickable(interactionSource = null, indication = null, onClick = {
                onToggleLike()
            })
    }

    val disLikeModifier = if (isTv) {
        Modifier
            .onFocusChanged {
                dislikeUiState = when {
                    it.isFocused -> FocusState.FOCUSED
                    it.hasFocus -> FocusState.SELECTED
                    else -> FocusState.UNFOCUSED
                }
            }
            .clickable(interactionSource = null, indication = null, onClick = {
                onToggleDisLike()
            })
            .focusable()
    } else {
        Modifier
            .clickable(interactionSource = null, indication = null, onClick = {
                onToggleDisLike()
            })
    }

    with(detailDto) {
        if (isTv) {
            // TV Layout - Keep existing layout
            Box(
                modifier = modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(imgSrc).crossfade(true)
                        .build(),
                    contentDescription = "detail Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                DetailGradient(
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .alpha(targetAlpha)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        textColor = whiteMain,
                        buttonModifier = Modifier,
                        isFavourite = isFavourite ?: false,
                        isLiked = isLiked ?: false,
                        isUnLiked = isDisliked ?: false,
                        addToWatchListModifier = addToWatchListModifier,
                        likeModifier = likeModifier,
                        disLikeModifier = disLikeModifier,
                        addToWatchListUiState = addToWatchListUiState,
                        likeUiState = likeUiState,
                        dislikeUiState = dislikeUiState,
                    )
                    FlowRow(
                        modifier = Modifier.padding(start = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Watch Now Button
                        CategoryCompose(modifier = Modifier
                            .focusRequester(playNowBtnFocusRequester)
                            .onFocusChanged {
                                isPlayFocused = it.hasFocus
                                onWatchNowFocusChange.invoke(it.hasFocus)
                            }
                            .focusable(),
                            categoryComposeDto = CategoryComposeDto(btnText = "Watch Now", id = ""),
                            backgroundFocusedColor = focusedMainColor,
                            textFocusedStyle = detailNowTextStyle,
                            backgroundUnFocusedColor = blackColor,
                            textUnFocusedStyle = detailNowUnFocusTextStyle,
                            onCategoryItemClick = { id ->
                                onWatchNowClick.invoke(id)
                            },
                            focusState = if (isPlayFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
                        )

                        // Resume Now Button
                        if (isResumeVisible) {
                            CategoryCompose(modifier = Modifier
                                .focusRequester(resumeBtnFocusRequester)
                                .onFocusChanged {
                                    isResumeFocused = it.hasFocus
                                    onResumeNowFocusChange.invoke(it.hasFocus)
                                }
                                .focusable(),
                                categoryComposeDto = CategoryComposeDto(
                                    btnText = resumeNowTxt,
                                    id = ""
                                ),
                                backgroundFocusedColor = focusedMainColor,
                                textFocusedStyle = detailNowTextStyle,
                                backgroundUnFocusedColor = blackColor,
                                textUnFocusedStyle = detailNowUnFocusTextStyle,
                                onCategoryItemClick = { id ->
                                    onResumeNowCLick.invoke()
                                },
                                focusState = if (isResumeFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
                            )
                        }

                        if (isPlayTrailerButtonShow) {
                            // Play Trailer
                            CategoryCompose(modifier = Modifier
                                .focusRequester(playTrailerFocusRequester)
                                .onFocusChanged {
                                    isPlayTrailerFocused = it.hasFocus
                                    onPlayTrailerFocusChange.invoke(it.hasFocus)
                                }
                                .focusable(),
                                categoryComposeDto = CategoryComposeDto(
                                    btnText = "Play Trailer",
                                    id = ""
                                ),
                                backgroundFocusedColor = focusedMainColor,
                                textFocusedStyle = detailNowTextStyle,
                                backgroundUnFocusedColor = blackColor,
                                textUnFocusedStyle = detailNowUnFocusTextStyle,
                                onCategoryItemClick = { id ->
                                    onPlayTrailer.invoke()
                                },
                                focusState = if (isPlayTrailerFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
                            )
                        }
                    }
                }
            }
        } else {
            // Mobile Layout - New vertical layout
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .alpha(targetAlpha)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Top Image with 16:9 aspect ratio
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(imgSrc).crossfade(true)
                        .build(),
                    contentDescription = "detail Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )

                // 2. Action Buttons (Watch Now, Resume, Play Trailer)
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Watch Now Button
                    CategoryCompose(modifier = Modifier
                        .focusRequester(playNowBtnFocusRequester)
                        .onFocusChanged {
                            isPlayFocused = it.hasFocus
                            onWatchNowFocusChange.invoke(it.hasFocus)
                        }
                        .focusable(),
                        categoryComposeDto = CategoryComposeDto(btnText = "Watch Now", id = ""),
                        backgroundFocusedColor = focusedMainColor,
                        textFocusedStyle = detailNowTextStyle,
                        backgroundUnFocusedColor = blackColor,
                        textUnFocusedStyle = detailNowUnFocusTextStyle,
                        onCategoryItemClick = { id ->
                            onWatchNowClick.invoke(id)
                        },
                        focusState = if (isPlayFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
                    )

                    // Resume Now Button
                    if (isResumeVisible) {
                        CategoryCompose(modifier = Modifier
                            .focusRequester(resumeBtnFocusRequester)
                            .onFocusChanged {
                                isResumeFocused = it.hasFocus
                                onResumeNowFocusChange.invoke(it.hasFocus)
                            }
                            .focusable(),
                            categoryComposeDto = CategoryComposeDto(
                                btnText = resumeNowTxt,
                                id = ""
                            ),
                            backgroundFocusedColor = focusedMainColor,
                            textFocusedStyle = detailNowTextStyle,
                            backgroundUnFocusedColor = blackColor,
                            textUnFocusedStyle = detailNowUnFocusTextStyle,
                            onCategoryItemClick = { id ->
                                onResumeNowCLick.invoke()
                            },
                            focusState = if (isResumeFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
                        )
                    }

                    if (isPlayTrailerButtonShow) {
                        // Play Trailer
                        CategoryCompose(modifier = Modifier
                            .focusRequester(playTrailerFocusRequester)
                            .onFocusChanged {
                                isPlayTrailerFocused = it.hasFocus
                                onPlayTrailerFocusChange.invoke(it.hasFocus)
                            }
                            .focusable(),
                            categoryComposeDto = CategoryComposeDto(
                                btnText = "Play Trailer",
                                id = ""
                            ),
                            backgroundFocusedColor = focusedMainColor,
                            textFocusedStyle = detailNowTextStyle,
                            backgroundUnFocusedColor = blackColor,
                            textUnFocusedStyle = detailNowUnFocusTextStyle,
                            onCategoryItemClick = { id ->
                                onPlayTrailer.invoke()
                            },
                            focusState = if (isPlayTrailerFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Title and Content Info
                ContentMetaBlock(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .wrapContentHeight(),
                    description = description,
                    releaseDate = releaseDate,
                    genre = genre,
                    seasons = seasons,
                    duration = duration,
                    subscribers = subscribers,
                    imdbRating = imdbRating,
                    title = title,
                    textColor = blackColor, // Use black text for mobile
                    buttonModifier = Modifier,
                    isFavourite = isFavourite ?: false,
                    isLiked = isLiked ?: false,
                    isUnLiked = isDisliked ?: false,
                    addToWatchListModifier = addToWatchListModifier,
                    likeModifier = likeModifier,
                    disLikeModifier = disLikeModifier,
                    addToWatchListUiState = addToWatchListUiState,
                    likeUiState = likeUiState,
                    dislikeUiState = dislikeUiState,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 5. Related Content (if available)
                relatedList?.let { related ->
                    if (related.isNotEmpty()) {
                        // Add related content section here
                        // This would typically be another component for showing related items
                    }
                }

                Spacer(modifier = Modifier.height(80.dp)) // Bottom padding
            }
        }
    }
}


@Preview(
    name = "Landscape Preview", widthDp = 640, heightDp = 360, showBackground = true
)
@Composable
private fun DetailPagePreview() {
    DetailContent(detailDto = DetailDto(slug = ""), onWatchNowClick = {

    }, onWatchNowFocusChange = {

    }, onToggleFavorite = {

    }, onToggleLike = {

    }, onToggleDisLike = {

    },
        onResumeNowFocusChange = {

        },
        onResumeNowCLick = {

        },
        onPlayTrailerFocusChange = {

        },
        onPlayTrailer = {

        }

    )
}