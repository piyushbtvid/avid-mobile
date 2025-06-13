package com.faithForward.media.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.related.RelatedContent
import com.faithForward.media.detail.related.RelatedContentRowDto
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.media.viewModel.uiModels.DetailPageItem
import com.faithForward.media.viewModel.uiModels.DetailScreenEvent
import com.faithForward.media.viewModel.uiModels.RelatedContentData
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.util.Resource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    onWatchNowClick: (PosterCardDto?, List<PosterCardDto>?) -> Unit,
    onRelatedItemClick: (PosterCardDto) -> Unit,
    detailViewModel: DetailViewModel,
) {
    val cardDetail by detailViewModel.cardDetail.collectAsStateWithLifecycle()
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val relatedContentData by detailViewModel.relatedContentData.collectAsStateWithLifecycle()
    val btnFocusRequester = remember { FocusRequester() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val uiEvent by detailViewModel.uiEvent.collectAsStateWithLifecycle(null)
    val context = LocalContext.current

    var lastFocusedItem by rememberSaveable { mutableStateOf(-1) }
    var seasonNumberSelectedItem by rememberSaveable { mutableStateOf(-1) }

    val animatedHeight by animateDpAsState(
        targetValue = if (uiState.targetHeight == Int.MAX_VALUE) screenHeight.dp else uiState.targetHeight.dp,
        animationSpec = tween(durationMillis = 500)
    )

    // Showing Toast when uiEvent changes
    LaunchedEffect(uiEvent) {
        uiEvent?.let { event ->
            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            // Reset uiEvent to prevent repeated toasts (optional, depending on ViewModel reset)
            // detailViewModel.resetUiEvent()
        }
    }

    when (cardDetail) {
        is Resource.Loading, is Resource.Unspecified -> {
            // Add loading indicator here if required
            return
        }

        is Resource.Error -> {
            // Error UI here if required
            return
        }

        is Resource.Success -> {
            val detailPageItem = cardDetail.data as? DetailPageItem.Card ?: return
            Box(modifier = modifier.fillMaxSize()) {
                DetailContent(detailDto = detailPageItem.detailDto,
                    btnFocusRequester = btnFocusRequester,
                    isContentVisible = uiState.isContentVisible,
                    modifier = Modifier.fillMaxSize(),
                    onWatchNowClick = {
                        if (detailPageItem.detailDto.isSeries == true) {
                            if (relatedContentData is RelatedContentData.SeriesSeasons) {
                                onWatchNowClick.invoke(
                                    null,
                                    (relatedContentData as RelatedContentData.SeriesSeasons).selectedSeasonEpisodes
                                )
                            }
                        } else {
                            onWatchNowClick.invoke(detailPageItem.detailDto.toPosterCardDto(), null)
                        }
                    },
                    onWatchNowFocusChange = { hasFocus ->
                        if (hasFocus) {
                            lastFocusedItem = -1
                        }
                    },
                    onToggleFavorite = {
                        if (detailPageItem.detailDto.slug != null) {
                            detailViewModel.handleEvent(
                                DetailScreenEvent.ToggleFavorite(detailPageItem.detailDto.slug!!)
                            )
                        }
                    },
                    onToggleDisLike = {
                        if (detailPageItem.detailDto.slug != null) {
                            detailViewModel.handleEvent(
                                DetailScreenEvent.ToggleDisLike(detailPageItem.detailDto.slug)
                            )
                        }
                    },
                    onToggleLike = {
                        if (detailPageItem.detailDto.slug != null) {
                            detailViewModel.handleEvent(
                                DetailScreenEvent.ToggleLike(detailPageItem.detailDto.slug)
                            )
                        }
                    })

                when (val contentData = relatedContentData) {
                    is RelatedContentData.None -> {
                        // Optionally show a placeholder or nothing
                    }

                    is RelatedContentData.RelatedMovies -> {
                        if (contentData.movies.isNotEmpty()) {
                            RelatedContent(relatedContentRowDto = RelatedContentRowDto(
                                heading = "Related Movies",
                                relatedContentDto = contentData.movies
                            ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 30.dp)
                                    .height(animatedHeight)
                                    .align(Alignment.BottomStart)
                                    .padding(bottom = 10.dp)
                                    .onFocusChanged {
                                        if (it.hasFocus) {
                                            detailViewModel.handleEvent(
                                                DetailScreenEvent.RelatedRowFocusChanged(it.hasFocus)
                                            )
                                        }
                                    },
                                contentRowModifier = Modifier
                                    .fillMaxWidth()
                                    .focusRestorer(),
                                onRelatedUpClick = {
                                    detailViewModel.handleEvent(DetailScreenEvent.RelatedRowUpClick)
                                    try {
                                        btnFocusRequester.requestFocus()
                                    } catch (ex: Exception) {
                                        Log.e("LOG", "exception is ${ex.message}")
                                    }
                                    true
                                },
                                onItemClick = { item, list, index ->
                                    onRelatedItemClick.invoke(item)
                                },
                                isRelatedContentMetaDataVisible = !uiState.isContentVisible,
                                lastFocusedItemIndex = lastFocusedItem,
                                onLastFocusedIndexChange = { item ->
                                    lastFocusedItem = item
                                })
                        }
                    }

                    is RelatedContentData.SeriesSeasons -> {
                        RelatedContent(relatedContentRowDto = RelatedContentRowDto(
                            heading = "Seasons:",
                            relatedContentDto = contentData.selectedSeasonEpisodes
                        ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                                .height(animatedHeight)
                                .align(Alignment.BottomStart)
                                .padding(bottom = 10.dp)
                                .onFocusChanged {
                                    if (it.hasFocus) {
                                        detailViewModel.handleEvent(
                                            DetailScreenEvent.RelatedRowFocusChanged(it.hasFocus)
                                        )
                                    }
                                },
                            contentRowModifier = Modifier.fillMaxWidth(),
                            onRelatedUpClick = {
                                detailViewModel.handleEvent(DetailScreenEvent.RelatedRowUpClick)
                                try {
                                    btnFocusRequester.requestFocus()
                                } catch (ex: Exception) {
                                    Log.e("LOG", "exception is ${ex.message}")
                                }
                                false
                            },
                            isRelatedContentMetaDataVisible = !uiState.isContentVisible,
                            onItemClick = { item, ls, index ->
                                Log.e("RELATED_SERIES", "on item is $item")
                                if (item.isRelatedSeries == true) {
                                    onRelatedItemClick.invoke(item)
                                } else {
                                    val newLs = ls.subList(
                                        index,
                                        ls.size
                                    )  // This creates a new list from index to end
                                    onWatchNowClick.invoke(null, newLs)
                                }

                            },
                            lastFocusedItemIndex = lastFocusedItem,
                            onLastFocusedIndexChange = { int ->
                                lastFocusedItem = int
                            },
                            seasonsNumberRow = {
                                SeasonsNumberRow(
                                    seasonsNumberDtoList = contentData.seasonNumberList,
                                    onSeasonUpClick = {
                                        detailViewModel.handleEvent(DetailScreenEvent.RelatedRowUpClick)
                                        try {
                                            btnFocusRequester.requestFocus()
                                        } catch (ex: Exception) {
                                            Log.e("LOG", "exception is ${ex.message}")
                                        }
                                        true
                                    },
                                    onSeasonNumberChanged = { seasonNumber ->
                                        detailViewModel.handleEvent(
                                            DetailScreenEvent.SeasonSelected(seasonNumber)
                                        )
                                    },
                                    modifier = Modifier.focusRestorer(),
                                    lastSelectedItemIndex = seasonNumberSelectedItem,
                                    onLastSelectedIndexChange = { index ->
                                        seasonNumberSelectedItem = index
                                    },
                                )
                            })
                    }
                }
            }

            LaunchedEffect(Unit) {
                Log.e(
                    "LAST_FOCUSED_INDEX",
                    "current selected series in detail screen is $seasonNumberSelectedItem"
                )
                if (lastFocusedItem == -1) {
                    try {
                        Log.e("LAST_FOCUSED", "last focused is $lastFocusedItem")
                        btnFocusRequester.requestFocus()
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }
}