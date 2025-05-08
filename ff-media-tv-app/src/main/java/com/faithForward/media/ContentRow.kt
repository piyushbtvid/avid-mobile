package com.faithForward.media

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import com.faithForward.media.util.FocusState
import com.faithForward.network.dto.Item

@Composable
fun ContentRow(
    modifier: Modifier = Modifier,
    contentList: List<Item>,
    contentRowFocusedIndex: Int = -1,
    onChangeContentRowFocusedIndex: (Int) -> Unit
) {

    val itemFocusRequesters = remember { List(contentList.size) { FocusRequester() } }

    LazyRow(
        modifier = modifier.fillMaxWidth()
    )
    {
        itemsIndexed(contentList) { index, contentItem ->

            val uiState = when (index) {
                contentRowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            PosterCard(
                modifier = Modifier
                    .focusRequester(itemFocusRequesters[index])
                    .onFocusChanged {
                        if (it.hasFocus) {
                            //    onItemFocused(Pair(rowIndex, index))
                            onChangeContentRowFocusedIndex.invoke(index)
                            //  playListItemFocusedIndex = index
                            //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                        } else {
                            if (contentRowFocusedIndex == index) {
                                onChangeContentRowFocusedIndex.invoke(index)
                                // playListItemFocusedIndex = -1
                            }
                        }
                    }
                    .focusable(),
                posterImageSrc = "",
                focusState = uiState
            )
        }
    }
}