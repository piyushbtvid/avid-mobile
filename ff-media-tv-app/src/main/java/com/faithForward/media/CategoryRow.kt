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
import com.faithForward.media.components.PillButton
import com.faithForward.media.util.FocusState
import com.faithForward.network.dto.Category

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryRow(
    modifier: Modifier = Modifier, list: List<Category>
) {

    var categoryRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val itemFocusRequesters = remember { List(list.size) { FocusRequester() } }


    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .focusRestorer {
                itemFocusRequesters[0]
            },
        contentPadding = PaddingValues(start = 88.dp, end = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(list) { index, contentItem ->

            val uiState = when (index) {
                categoryRowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            PillButton(modifier = Modifier
                .focusRequester(itemFocusRequesters[index])
                .onFocusChanged {
                    if (it.hasFocus) {
                        //    onItemFocused(Pair(rowIndex, index))
                        categoryRowFocusedIndex = index
                        //  onChangeContentRowFocusedIndex.invoke(index)
                        //  playListItemFocusedIndex = index
                        //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                    } else {
                        if (categoryRowFocusedIndex == index) {
                            categoryRowFocusedIndex = -1
                            //  onChangeContentRowFocusedIndex.invoke(index)
                            // playListItemFocusedIndex = -1
                        }
                    }
                }
                .focusable(), btnText = contentItem.name, focusState = uiState)
        }
    }

}