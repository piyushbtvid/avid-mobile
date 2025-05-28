package com.faithForward.media.detail

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.util.FocusState

data class RelatedContentRowDto(
    val heading: String,
    val relatedContentDto: List<PosterCardDto>,
)

@Composable
fun RelatedContentRow(
    modifier: Modifier = Modifier,
    relatedContentRowDto: RelatedContentRowDto,
) {

    var relatedRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TitleText(
            text = relatedContentRowDto.heading, modifier = Modifier.padding(start = 25.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 25.dp, end = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        )
        {
            itemsIndexed(relatedContentRowDto.relatedContentDto) { index, relatedContentItem ->

                val uiState = when (index) {
                    relatedRowFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                RelatedContent(
                    modifier = Modifier
                        .onFocusChanged {
                            if (it.hasFocus) {
                                relatedRowFocusedIndex = index
                            } else {
                                if (relatedRowFocusedIndex == index) {
                                    relatedRowFocusedIndex = -1
                                }
                            }
                        }
                        .focusable(),
                    relatedContentDto = relatedContentItem,
                    uiState = uiState
                )
            }
        }
    }

}