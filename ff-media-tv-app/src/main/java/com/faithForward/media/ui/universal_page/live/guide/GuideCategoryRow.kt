package com.faithForward.media.ui.universal_page.live.guide

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.CategoryCompose
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GuideCategoryRow(
    modifier: Modifier = Modifier,
    list: List<CategoryComposeDto>,
) {
    var categoryRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }


    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .focusRestorer(),
        contentPadding = PaddingValues(
            top = 17.5.dp,
            end = 20.dp,
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(list) { index, categoryComposeDto ->


            val uiState = when (index) {
                categoryRowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            CategoryCompose(
                modifier = Modifier
                    .onFocusChanged {
                        if (it.hasFocus) {
                            categoryRowFocusedIndex = index
                        } else {
                            if (categoryRowFocusedIndex == index) {
                                categoryRowFocusedIndex = -1
                            }
                        }
                    }
                    .focusable(),
                categoryComposeDto = categoryComposeDto,
                onCategoryItemClick = { id ->

                },
                focusState = uiState
            )
        }
    }

}