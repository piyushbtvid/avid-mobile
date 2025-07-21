package com.faithForward.media.ui.detail.related

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faithForward.media.ui.commanComponents.PosterCard
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.epg.util.FocusState


@Composable
fun RelatedContentItem(
    modifier: Modifier = Modifier,
    uiState: FocusState = FocusState.UNFOCUSED,
    relatedContentDto: PosterCardDto,
    onItemClick: (PosterCardDto) -> Unit,
) {

    PosterCard(
        modifier = modifier,
        posterCardDto = relatedContentDto,
        focusState = uiState,
        showContent = false,
        onItemClick = onItemClick
    )

}