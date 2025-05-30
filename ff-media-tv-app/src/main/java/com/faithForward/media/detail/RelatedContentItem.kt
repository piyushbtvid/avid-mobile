package com.faithForward.media.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faithForward.media.commanComponents.PosterCard
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.util.FocusState



@Composable
fun RelatedContentItem(
    modifier: Modifier = Modifier,
    uiState: FocusState = FocusState.UNFOCUSED,
    relatedContentDto: PosterCardDto,
) {

    PosterCard(
        modifier = modifier,
        posterCardDto = relatedContentDto,
        focusState = uiState,
        onItemClick = {

        }
    )

}