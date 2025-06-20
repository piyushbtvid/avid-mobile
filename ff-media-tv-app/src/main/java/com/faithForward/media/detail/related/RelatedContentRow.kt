package com.faithForward.media.detail.related

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.util.FocusState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RelatedContentRow(
    modifier: Modifier = Modifier,
    relatedRowFocusedIndex: Int,
    relatedContentRowDto: List<PosterCardDto>,
    focusRequesters: MutableMap<Int, FocusRequester>,
    lastFocusedItem: Int,
    onItemFocused: (Int) -> Unit,
    lazyListState: LazyListState,
    onRelatedUpClick: () -> Boolean,
    onItemClick: (PosterCardDto, Int) -> Unit,
    onRelatedRowFocusedIndexChange: (Int) -> Unit,
) {

    // val itemFocusRequesters = remember { List(relatedContentRowDto.size) { FocusRequester() } }
    val itemFocusRequesters =
        remember { mutableListOf<FocusRequester>().apply { addAll(List(relatedContentRowDto.size) { FocusRequester() }) } }

    LaunchedEffect(Unit) {
        Log.e("RELATED_CONTENT_ROW", "relatedContentRowDto size is ${relatedContentRowDto.size}")
    }

    LazyRow(
        state = lazyListState,
        modifier = modifier,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        itemsIndexed(relatedContentRowDto) { index, relatedContentItem ->
            val safeFocusRequester = itemFocusRequesters.getOrNull(index) ?: FocusRequester()

            // Optional: Only include if focusRequesters is needed for a specific purpose
            if (index < focusRequesters.size) {
                focusRequesters[index] = itemFocusRequesters[index]
            }

            LaunchedEffect(lastFocusedItem) {
                if (lastFocusedItem in 0 until itemFocusRequesters.size && lastFocusedItem == index) {
                    try {
//                        if (lastFocusedItem > 0) {
                        //  lazyListState.scrollToItem(lastFocusedItem)
                        itemFocusRequesters[index].requestFocus()
                        //  }
                    } catch (ex: Exception) {
                        Log.e("RELATED_CONTENT_ROW", "Error requesting focus: ${ex.message}")
                    }
                }
            }

            val uiState = when (index) {
                relatedRowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            RelatedContentItem(modifier = Modifier
                .focusRequester(safeFocusRequester)
                .onFocusChanged {
                    if (it.hasFocus) {
                        onItemFocused(index)
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
                        return@onKeyEvent onRelatedUpClick()
                    } else {
                        false
                    }
                }, relatedContentDto = relatedContentItem, uiState = uiState, onItemClick = {
                onItemClick.invoke(relatedContentItem, index)
            })
        }
    }


}