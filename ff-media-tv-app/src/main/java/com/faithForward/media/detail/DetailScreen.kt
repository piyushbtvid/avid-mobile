package com.faithForward.media.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(Unit) {
        detailViewModel.getGivenCardDetail(itemId, relatedList)
    }

    val cardDetailResponse by detailViewModel.cardDetail.collectAsStateWithLifecycle()

    if (cardDetailResponse is Resource.Loading || cardDetailResponse is Resource.Error || cardDetailResponse is Resource.Unspecified) return

    val detailPageItem = cardDetailResponse.data ?: return

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (detailPageItem) {
            is DetailPageItem.CardWithRelated -> {
                // Main content (fills the screen)
                DetailContent(
                    detailDto = detailPageItem.detailDto,
                    modifier = Modifier
                        .fillMaxSize()
                )

                // Related row at the bottom
                RelatedContentRow(
                    relatedContentRowDto = RelatedContentRowDto(
                        heading = "Related",
                        relatedContentDto = detailPageItem.relatedList
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(bottom = 20.dp) // adjust padding as needed
                )
            }
        }
    }

}