package com.faithForward.media.ui.sections.my_account.comman

import androidx.compose.foundation.clickable
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
import com.faithForward.media.util.CustomGridCells
import com.faithForward.media.util.CustomLazyGrid
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WatchSectionGrid(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    focusRequesterList: List<FocusRequester>,
    onLastFocusedIndexChange: (Int) -> Unit,
    onItemClick: (WatchSectionItemDto) -> Unit,
    onFocusedIndexChange: (Int) -> Unit,
    watchSectionItemDtoList: List<WatchSectionItemDto>,
) {


    CustomLazyGrid(
        modifier = modifier
            .fillMaxSize(),
        items = watchSectionItemDtoList,
        columns = CustomGridCells.Fixed(2),
//        verticalSpacing = 45.dp,
//        horizontalSpacing = 30.dp,
//        rowContentPadding = PaddingValues(horizontal = 16.dp),
//        columnContentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
    ) { index, item ->
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
                        onLastFocusedIndexChange.invoke(index)
                    } else {
                        onFocusedIndexChange.invoke(-1)
                    }
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    onItemClick.invoke(item)
                })
                .focusable(),
            focusState = uiState,
            watchSectionItemDto = item,
        )
    }
}