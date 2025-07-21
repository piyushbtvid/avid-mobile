package com.faithForward.media.ui.epg.util.extensions

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@ExperimentalFoundationApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PositionFocusedItemInLazyLayout(
    parentFraction: Float = 0f,
    childFraction: Float = 0f,
    content: @Composable () -> Unit
) {
    Log.d("Logging", "PositionFocusedItem:INit")
    // This bring-into-view spec pivots around the center of the scrollable container
    val bringIntoViewSpec = remember(parentFraction, childFraction) {
        object : BringIntoViewSpec {
            override fun calculateScrollDistance(
                offset: Float,
                size: Float,
                containerSize: Float
            ): Float {


                val childSmallerThanParent = size <= containerSize
                val initialTargetForLeadingEdge =
                    parentFraction * containerSize - (childFraction * size)
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

    // LocalBringIntoViewSpec will apply to all scrollables in the hierarchy
    CompositionLocalProvider(
        LocalBringIntoViewSpec provides bringIntoViewSpec,
        content = content,
    )
}