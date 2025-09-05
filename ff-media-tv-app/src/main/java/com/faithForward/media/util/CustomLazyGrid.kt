package com.faithForward.media.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.faithForward.media.util.Util.isTvDevice

sealed class CustomGridCells {
    data class Fixed(val count: Int) : CustomGridCells()
    data class Adaptive(val minSize: Dp) : CustomGridCells()
}

@Composable
fun <T> CustomLazyGrid(
    modifier: Modifier = Modifier,
    items: List<T>,
    columns: CustomGridCells,
    verticalSpacing: Dp = 16.dp,
    horizontalSpacing: Dp = 16.dp,
    rowModifier: Modifier = Modifier,
    columnContentPadding: PaddingValues = PaddingValues(vertical = 30.dp),
    rowContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    var containerWidth by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    Box(
        modifier = modifier.onGloballyPositioned { coords ->
            containerWidth = coords.size.width
        }
    ) {
        if (containerWidth > 0) {
            val columnCount = when (columns) {
                is CustomGridCells.Fixed -> columns.count
                is CustomGridCells.Adaptive -> {
                    with(density) {
                        val minPx = columns.minSize.roundToPx()
                        (containerWidth / minPx).coerceAtLeast(1)
                    }
                }
            }

            val rows = items.chunked(columnCount)

            LazyColumn(
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
                                val globalIndex = rowIndex * columnCount + colIndex
                                itemContent(globalIndex, item)
                            }
                        }
                    }
                }
            }
        }
    }
}
