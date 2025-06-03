package com.faithForward.media.detail

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.related.RelatedContent
import com.faithForward.media.detail.related.RelatedContentRowDto
import com.faithForward.media.viewModel.DetailPageItem
import com.faithForward.media.viewModel.DetailScreenEvent
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.media.viewModel.RelatedContentData
import com.faithForward.util.Resource

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    itemId: String,
    relatedList: List<PosterCardDto> = emptyList(),
    onRelatedItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
    detailViewModel: DetailViewModel,
) {
    val cardDetail by detailViewModel.cardDetail.collectAsStateWithLifecycle()
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val relatedContentData by detailViewModel.relatedContentData.collectAsStateWithLifecycle()
    val btnFocusRequester = remember { FocusRequester() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val animatedHeight by animateDpAsState(
        targetValue = if (uiState.targetHeight == Int.MAX_VALUE) screenHeight.dp else uiState.targetHeight.dp,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(Unit) {
        Log.e(
            "DETAIL_SCREEN",
            "detail screen is opened with $itemId and related List size ${relatedList.size}"
        )
        detailViewModel.handleEvent(DetailScreenEvent.LoadCardDetail(itemId, relatedList))
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
                DetailContent(
                    detailDto = detailPageItem.detailDto,
                    btnFocusRequester = btnFocusRequester,
                    isContentVisible = uiState.isContentVisible,
                    modifier = Modifier.fillMaxSize(),
                )

                // Assign to local variable to enable smart casting
                when (val contentData = relatedContentData) {
                    is RelatedContentData.None -> {
                        // Optionally show a placeholder or nothing
                    }

                    is RelatedContentData.RelatedMovies -> {
                        if (contentData.movies.isNotEmpty()) {
                            RelatedContent(
                                relatedContentRowDto = RelatedContentRowDto(
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
                                        detailViewModel.handleEvent(
                                            DetailScreenEvent.RelatedRowFocusChanged(it.hasFocus)
                                        )
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
                                onItemClick = onRelatedItemClick,
                                isRelatedContentMetaDataVisible = !uiState.isContentVisible
                            )
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
                                    detailViewModel.handleEvent(
                                        DetailScreenEvent.RelatedRowFocusChanged(it.hasFocus)
                                    )
                                },
                            contentRowModifier = Modifier.fillMaxWidth(),
                            onRelatedUpClick = {
                                detailViewModel.handleEvent(DetailScreenEvent.RelatedRowUpClick)
                                try {
                                    btnFocusRequester.requestFocus()
                                } catch (ex: Exception) {
                                    Log.e("LOG", "exception is ${ex.message}")
                                }
                                true
                            },
                            isRelatedContentMetaDataVisible = !uiState.isContentVisible,
                            onItemClick = { item, ls ->

                            },
                            seasonsNumberRow = {
                                SeasonsNumberRow(
                                    seasonsNumberDtoList = contentData.seasonNumberList,
                                    onSeasonUpClick = {
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
                                )
                            })
                    }
                }
            }
        }
    }
}