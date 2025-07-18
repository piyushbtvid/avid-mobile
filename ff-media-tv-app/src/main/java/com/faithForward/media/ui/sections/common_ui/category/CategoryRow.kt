package com.faithForward.media.ui.sections.common_ui.category

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.faithForward.media.ui.commanComponents.CategoryCompose
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.util.FocusState

data class CategoryRowDto(
    val categories: List<CategoryComposeDto>,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CategoryRow(
    modifier: Modifier = Modifier,
    categoryRowDto: CategoryRowDto,
    rowIndex: Int,
    focusRequesters: MutableMap<Pair<Int, Int>, FocusRequester>,
    lastFocusedItem: Pair<Int, Int>,
    onItemFocused: (Pair<Int, Int>) -> Unit,
    onCategoryItemClick: (String) -> Unit,
    listState: LazyListState,
    shouldFocusOnFirstItem: Boolean = false,
) {


    with(categoryRowDto) {

        var categoryRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
        val itemFocusRequesters = remember { List(categories.size) { FocusRequester() } }

//        LaunchedEffect(shouldFocusOnFirstItem) {
//            if (shouldFocusOnFirstItem) {
//                try {
//                    itemFocusRequesters[0].requestFocus()
//                } catch (ex: Exception) {
//                    Log.e("FOCUS_ISSUE", "${ex.message}")
//                }
//            }
//        }


        LazyRow(
            state = listState,
            modifier = modifier
                .fillMaxWidth()
                .focusRestorer(),
            contentPadding = PaddingValues(
                top = 17.5.dp,
                start = 25.dp,
                end = 20.dp,
                bottom = 15.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(categories) { index, categoryComposeDto ->

                focusRequesters[Pair(rowIndex, index)] = itemFocusRequesters[index]


                // Restore focus to the last focused item when returning to this row
                LaunchedEffect(lastFocusedItem) {
                    if (lastFocusedItem == Pair(rowIndex, index)) {
                        try {
                            itemFocusRequesters[index].requestFocus()
                        }catch (_:Exception){

                        }
                    }
                }

                val uiState = when (index) {
                    categoryRowFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                CategoryCompose(
                    modifier = Modifier
                        .focusRequester(itemFocusRequesters[index])
                        .onFocusChanged {
                            if (it.hasFocus) {
                                onItemFocused(Pair(rowIndex, index))
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
                        .focusable(),
                    categoryComposeDto = categoryComposeDto,
                    onCategoryItemClick = { id ->
                        onCategoryItemClick.invoke(id)
                    },
                    focusState = uiState
                )
            }
        }
    }

}