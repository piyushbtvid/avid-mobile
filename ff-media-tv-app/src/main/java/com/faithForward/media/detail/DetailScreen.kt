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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.viewModel.DetailPageItem
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.util.Resource

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    itemId: String,
    relatedList: List<PosterCardDto> = emptyList(),
    detailViewModel: DetailViewModel,
) {
    // State to control the height of RelatedContentRow
    var targetHeight by remember { mutableStateOf(250) }
    // Animate the height change
    val animatedHeight by animateDpAsState(
        targetValue = targetHeight.dp,
        animationSpec = tween(durationMillis = 500) // 500ms animation duration
    )

    LaunchedEffect(Unit) {
        Log.e(
            "DETAIL_SCREEN",
            "detail screen is opened with $itemId and related List size  ${relatedList.size}"
        )
        detailViewModel.getGivenCardDetail(itemId, relatedList)
    }

    val cardDetailResponse by detailViewModel.cardDetail.collectAsStateWithLifecycle()

    if (cardDetailResponse is Resource.Loading || cardDetailResponse is Resource.Error || cardDetailResponse is Resource.Unspecified) return

    val detailPageItem = cardDetailResponse.data ?: return

    LaunchedEffect(detailPageItem) {
        Log.e(
            "DETAIL_SCREEN",
            "detail page item in detail screen is $detailPageItem"
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (detailPageItem) {
            is DetailPageItem.CardWithRelated -> {
                // Main content (fills the screen)
                DetailContent(
                    detailDto = detailPageItem.detailDto,
                    modifier = Modifier
                        .fillMaxSize(),
                    onPlayButtonKeyDown = {
                        Log.e("PLAYBUTTON", "playButton on down clicked")
                        // Trigger height animation to 500.dp
                        targetHeight = 500
                    }
                )

                // Related row at the bottom with animated height
                RelatedContentRow(
                    relatedContentRowDto = RelatedContentRowDto(
                        heading = "Related",
                        relatedContentDto = detailPageItem.relatedList
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeight) // Use animated height
                        .align(Alignment.BottomStart)
                        .padding(bottom = 10.dp) // adjust padding as needed
                        .onFocusChanged {
                            if (it.hasFocus) {
                                targetHeight = 500
                                Log.e("RELATED", "related content Row has Focused")
                            } else {
                                targetHeight = 250
                            }
                        }
                )
            }
        }
    }
}