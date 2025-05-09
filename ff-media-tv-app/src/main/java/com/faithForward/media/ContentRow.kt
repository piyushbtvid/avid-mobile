package com.faithForward.media

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState
import com.faithForward.network.dto.Item

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContentRow(
    modifier: Modifier = Modifier,
    contentList: List<Item>,
    onChangeContentRowFocusedIndex: (Int) -> Unit
) {

    var contentRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    val itemFocusRequesters = remember { List(contentList.size) { FocusRequester() } }

    LazyRow(
        modifier =
        modifier
            .fillMaxWidth()
            .focusRestorer {
                itemFocusRequesters[0]
            },
        contentPadding = PaddingValues(start = 88.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
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
                            contentRowFocusedIndex = index
                            //  onChangeContentRowFocusedIndex.invoke(index)
                            //  playListItemFocusedIndex = index
                            //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                        } else {
                            if (contentRowFocusedIndex == index) {
                                contentRowFocusedIndex = -1
                                //  onChangeContentRowFocusedIndex.invoke(index)
                                // playListItemFocusedIndex = -1
                            }
                        }
                    }
                    .focusable(),
                posterImageSrc = contentItem.posterImage,
                focusState = uiState
            )
        }
    }
}