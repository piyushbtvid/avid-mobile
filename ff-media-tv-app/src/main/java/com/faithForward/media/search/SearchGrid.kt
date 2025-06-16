package com.faithForward.media.search

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.search.item.SearchItem
import com.faithForward.media.search.item.SearchItemDto
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchGrid(
    modifier: Modifier = Modifier,
    gridFocusRequester: FocusRequester,
    searchResultList: List<SearchItemDto>,
    lastFocusedIndex: Int,
    onLastFocusIndexChange: (Int) -> Unit,
) {
    val scrollState = rememberLazyGridState()


    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Fixed(5),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            bottom = 80.dp, start = 16.dp, end = 16.dp, top = 16.dp
        ),
        modifier = modifier
            .fillMaxSize()
            .focusRestorer() // Keep this, but ensure it doesn't override initial focus
    ) {
        itemsIndexed(searchResultList) { index, item ->
            val uiState = when (index) {
                lastFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }
            SearchItem(
                modifier = Modifier
                    .focusRequester(if (index == 0) gridFocusRequester else FocusRequester())
                    .onFocusChanged {
                        if (it.isFocused) {
                            Log.e("SEARCH_GRID", "Item $index gained focus")
                            onLastFocusIndexChange.invoke(index)
                            // Ensure the item is visible
//                            coroutineScope.launch {
//                                scrollState.scrollToItem(index)
//                            }
                        }
                    }
                    .focusable(),
                searchItemDto = item,
                onItemClick = {
                    Log.e("SEARCH_GRID", "Item $index clicked")
                },
                focusState = uiState,
            )
        }
    }

}