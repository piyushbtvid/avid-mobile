package com.faithForward.media.ui.sections.my_account.comman

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WatchSectionGrid(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    focusRequesterList : List<FocusRequester>,
    onFocusedIndexChange: (Int) -> Unit,
    watchSectionItemDtoList: List<WatchSectionItemDto>,
) {

    val scrollState = rememberLazyGridState()


    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(
            bottom = 80.dp, start = 16.dp, end = 16.dp, top = 16.dp
        ),
        modifier = modifier
            .fillMaxSize()
            .focusRestorer() // Keep this, but ensure it doesn't override initial focus
    ) {
        itemsIndexed(watchSectionItemDtoList) { index, item ->
            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            WatchSectionItem(
                modifier = Modifier
                    .focusRequester(focusRequesterList[index])
                    .onFocusChanged {
                        if (it.hasFocus) {
                            onFocusedIndexChange.invoke(index)
                        } else {
                            onFocusedIndexChange.invoke(-1)
                        }
                    }
                    .focusable(),
                focusState = uiState,
                watchSectionItemDto = item,
            )

        }
    }

}