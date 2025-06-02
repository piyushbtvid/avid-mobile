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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
import com.faithForward.util.Resource

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    itemId: String,
    relatedList: List<PosterCardDto> = emptyList(),
    detailViewModel: DetailViewModel,
) {
    val cardDetail by detailViewModel.cardDetail.collectAsStateWithLifecycle()
    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()
    val btnFocusRequester = remember { FocusRequester() }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    // Update target height with actual screen height when focused
    val animatedHeight by animateDpAsState(
        targetValue = if (uiState.targetHeight == Int.MAX_VALUE) screenHeight.dp else uiState.targetHeight.dp,
        animationSpec = tween(durationMillis = 500)
    )

    val list = listOf(
        SeasonsNumberDto(
            seasonNumber = 1
        ),
        SeasonsNumberDto(
            seasonNumber = 2
        ),
        SeasonsNumberDto(
            seasonNumber = 3
        ),
        SeasonsNumberDto(
            seasonNumber = 4
        ),
        SeasonsNumberDto(
            seasonNumber = 5
        ),
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
            //  add loading indicator here if required
            return
        }

        is Resource.Error -> {
            //  error UI here if required
            return
        }

        is Resource.Success -> {
            val detailPageItem = cardDetail.data as? DetailPageItem.CardWithRelated ?: return
            Box(modifier = modifier.fillMaxSize()) {
                DetailContent(
                    detailDto = detailPageItem.detailDto,
                    btnFocusRequester = btnFocusRequester,
                    contentColor = uiState.contentColor,
                    textUnfocusedColor = uiState.textUnfocusedColor,
                    contentRowTint = uiState.contentRowTint,
                    buttonUnfocusedColor = uiState.buttonUnfocusedColor,
                    modifier = Modifier.fillMaxSize(),
                )

                if (detailPageItem.relatedList.isNotEmpty()) {
                    RelatedContent(
                        relatedContentRowDto = RelatedContentRowDto(
                            heading = "Related Movies",
                            relatedContentDto = detailPageItem.relatedList,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(animatedHeight)
                            .align(Alignment.BottomStart)
                            .padding(
                                bottom = 10.dp,
                            )
                            .onFocusChanged {
                                detailViewModel.handleEvent(
                                    DetailScreenEvent.RelatedRowFocusChanged(
                                        it.hasFocus
                                    )
                                )
                            },
                        onRelatedUpClick = {
                            detailViewModel.handleEvent(DetailScreenEvent.RelatedRowUpClick)
                            try {
                                btnFocusRequester.requestFocus()
                            } catch (ex: Exception) {
                                Log.e("LOG", "exception is ${ex.message}")
                            }
                            true
                        },
                        relatedContentColor = uiState.relatedContentColor,
                    )
                } else {
                    if (detailPageItem.seasonNumberList != null && detailPageItem.seasonList != null) {
                        SeasonsContent(
                            onSeasonUpClick = {
                                Log.e("SEASON", "on Season Up Click is called on detail screen")
                                try {
                                    btnFocusRequester.requestFocus()
                                } catch (ex: Exception) {
                                    Log.e("LOG", "exception is ${ex.message}")
                                }
                                true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp)
                                .height(animatedHeight)
                                .align(Alignment.BottomStart)
                                .padding(
                                    bottom = 10.dp,
                                )
                                .onFocusChanged {
                                    detailViewModel.handleEvent(
                                        DetailScreenEvent.RelatedRowFocusChanged(
                                            it.hasFocus
                                        )
                                    )
                                },
                            seasonsContentDto = SeasonsContentDto(
                                seasonsNumberDtoList = detailPageItem.seasonNumberList,
                                relatedContentDto = detailPageItem.seasonList.get(1).episodesContentDto
                            ),
                            relatedContentColor = uiState.relatedContentColor
                        )
                    }
                }

            }
        }
    }
}