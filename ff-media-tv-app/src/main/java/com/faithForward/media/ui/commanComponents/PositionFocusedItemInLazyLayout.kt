package com.faithForward.media.ui.commanComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PositionFocusedItemInLazyLayout(
    parentOffset: Dp = 0.dp,
    childOffset: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current

    val bringIntoViewSpec = remember(parentOffset, childOffset, density) {
        object : BringIntoViewSpec {
            override fun calculateScrollDistance(
                offset: Float,
                size: Float,
                containerSize: Float,
            ): Float {

                // Convert Dp to px
                val parentPx = with(density) { parentOffset.toPx() }
                val childPx = with(density) { childOffset.toPx() }

                val childSmallerThanParent = size <= containerSize
                val initialTargetForLeadingEdge = parentPx - childPx
                val spaceAvailableToShowItem = containerSize - initialTargetForLeadingEdge

                val targetForLeadingEdge =
                    if (childSmallerThanParent && spaceAvailableToShowItem < size) {
                        containerSize - size
                    } else {
                        initialTargetForLeadingEdge
                    }

                return (offset - targetForLeadingEdge).coerceIn(-containerSize, containerSize)
            }
        }
    }

    CompositionLocalProvider(
        LocalBringIntoViewSpec provides bringIntoViewSpec,
        content = content,
    )
}