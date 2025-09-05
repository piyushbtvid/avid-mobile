package com.faithForward.media.ui.sections.search.custom_keyboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState

@Composable
fun InputKeyList(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    onFocusedIndexChanged: (Int) -> Unit,
    onItemClick: (String) -> Unit,
    list: List<String>,
) {


    LazyRow(
        modifier = modifier.wrapContentWidth(),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
    ) {

        itemsIndexed(list) { index, item ->

            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }


            KeyBoardKeyItem(
                modifier = Modifier
                    .onFocusChanged {
                        if (it.hasFocus) {
                            onFocusedIndexChanged.invoke(index)
                        } else {
                            onFocusedIndexChanged.invoke(-1)

                        }
                    }
                    .clickable(interactionSource = null, indication = null, onClick = {
                        onItemClick.invoke(item)
                    })
                    .focusable(),
                displayText = item,
                focusState = uiState,
            )


        }

    }


}