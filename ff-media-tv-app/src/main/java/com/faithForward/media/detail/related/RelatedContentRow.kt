package com.faithForward.media.detail.related

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RelatedContentRow(
    modifier: Modifier = Modifier,
    relatedRowFocusedIndex: Int,
    relatedContentRowDto: RelatedContentRowDto,
    onRelatedUpClick: () -> Unit,
    onRelatedRowFocusedIndexChange: (Int) -> Unit,
) {

    LazyRow(
        modifier = modifier
           ,
        contentPadding = PaddingValues(start = 25.dp, end = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        itemsIndexed(relatedContentRowDto.relatedContentDto) { index, relatedContentItem ->

            val uiState = when (index) {
                relatedRowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            RelatedContentItem(modifier = Modifier
                .onFocusChanged {
                    if (it.hasFocus) {
                        onRelatedRowFocusedIndexChange.invoke(index)
                    } else {
                        if (relatedRowFocusedIndex == index) {
                            onRelatedRowFocusedIndexChange.invoke(-1)
                        }
                    }
                }
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.DirectionUp && keyEvent.type == KeyEventType.KeyUp) {
                        onRelatedUpClick.invoke()
                        true
                    } else {
                        false
                    }
                }, relatedContentDto = relatedContentItem, uiState = uiState
            )
        }
    }


}