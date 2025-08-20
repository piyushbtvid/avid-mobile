package com.faithForward.media.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> CustomLazyGrid(
    modifier: Modifier = Modifier,
    items: List<T>,
    columns: Int,
    verticalSpacing: Dp = 16.dp,
    horizontalSpacing: Dp = 16.dp,
    rowModifier: Modifier = Modifier,
    columnContentPadding: PaddingValues = PaddingValues(vertical = 30.dp),
    rowContentPadding: PaddingValues = PaddingValues(horizontal = 50.dp),
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    // Split items into rows based on fixed column count
    val rows = remember(items, columns) { items.chunked(columns) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        contentPadding = columnContentPadding
    ) {
        rows.forEachIndexed { rowIndex, rowItems ->
            item(key = rowIndex) {
                LazyRow(
                    modifier = rowModifier.fillMaxWidth(),
                    contentPadding = rowContentPadding,
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)
                ) {
                    itemsIndexed(rowItems) { colIndex, item ->
                        val globalIndex = rowIndex * columns + colIndex
                        itemContent(globalIndex, item)
                    }
                }
            }
        }
    }
}
