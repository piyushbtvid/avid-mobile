package com.faithForward.media

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import com.faithForward.media.util.FocusState

@Composable
fun ContentRow(
    modifier: Modifier = Modifier,
    contentList: List<ContentTestItem>,
    contentRowFocusedIndex: Int = -1
) {

    LazyRow(
        modifier = modifier.fillMaxWidth()
    )
    {
        itemsIndexed(contentList) { index, contentItem ->

            val uiState = when (index) {
                contentRowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

//            PosterCard(
//                modifier = Modifier
//                    .focusRequester(itemFocusRequesters[index])
//                    .onFocusChanged {
//                        if (it.hasFocus) {
//                            onItemFocused(Pair(rowIndex, index))
//                            playListItemFocusedIndex = index
//                            onBackgroundChange.invoke(playlistItem.landscape ?: "")
//                        } else {
//                            if (playListItemFocusedIndex == index) {
//                                playListItemFocusedIndex = -1
//                            }
//                        }
//                    }
//                    .focusable(),
//                posterImageSrc = "",
//                focusState = uiState,
//                placeholderRes = contentItem.poster,
//            )
        }
    }
}