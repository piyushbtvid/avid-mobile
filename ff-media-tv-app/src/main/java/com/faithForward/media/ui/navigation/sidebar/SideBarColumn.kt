package com.faithForward.media.ui.navigation.sidebar

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.util.FocusState
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive

@Composable
fun SideBarColumn(
    modifier: Modifier = Modifier,
    columnItems: List<SideBarItem>,
    focusedIndex: Int,
    selectedPosition: Int,
    isSideBarFocusable: Boolean,
    onSelectedPositionChange: (Int) -> Unit,
    onItemClick: (SideBarItem) -> Unit,
    onFocusChange: (Int) -> Unit,
) {
    val itemFocusRequesters = remember(columnItems) {
        List(columnItems.size) { FocusRequester() }
    }
    val coroutineScope = rememberCoroutineScope()
    var isComposed by remember { mutableStateOf(true) }

    var targetValue = if (focusedIndex == -1) 38.dp else 114.dp
    val animatedWidth by animateDpAsState(
        targetValue = targetValue, animationSpec = tween(300), label = ""
    )

    // Track composition state
    DisposableEffect(Unit) {
        isComposed = true
        onDispose {
            isComposed = false
        }
    }

    // Ensure focus is requested when focusedIndex changes
    LaunchedEffect(focusedIndex, isSideBarFocusable, columnItems.size) {
        if (!isComposed || !isSideBarFocusable) return@LaunchedEffect
        Log.e("FOCUSED_INDEX", "focused index changed with $focusedIndex and $isSideBarFocusable")
        if (focusedIndex != -1 && isSideBarFocusable) {
            if (focusedIndex < itemFocusRequesters.size && focusedIndex >= 0) {
                Log.e("SIDE_BAR_ITEM", "on side bar item request called")
                itemFocusRequesters[focusedIndex].safeRequestFocus()
            }
        }
    }

    LazyColumn(
        modifier = modifier.onFocusChanged {
            Log.d("onFocusChanged", "${it.hasFocus} ${it.isFocused} ${it.isCaptured}")
            if (it.hasFocus && isSideBarFocusable && columnItems.isNotEmpty() && isComposed) {
                coroutineScope.launch {
                    // Wait a frame to ensure composition is stable
                    awaitFrame()
                    if (!isActive || !isComposed || !isSideBarFocusable) return@launch
                    val visibleLastIndex = if (focusedIndex == -1) {
                        (columnItems.size - 3).coerceAtLeast(0)
                    } else {
                        (columnItems.size - 1).coerceAtLeast(0)
                    }
                    val targetIndex = selectedPosition.coerceIn(0, visibleLastIndex)
                    if (targetIndex >= 0 && targetIndex < itemFocusRequesters.size) {
                        itemFocusRequesters[targetIndex].safeRequestFocus()
                    }
                }
            }
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(columnItems) { index, item ->

            // Skip rendering last 2 items when focusedIndex == -1
            val isLastTwoItem = index >= columnItems.size - 2
            val shouldHide = isLastTwoItem && focusedIndex == -1

            // Add top space before second-last item
            if (index == columnItems.size - 2 && focusedIndex != -1) {
                Spacer(modifier = Modifier.height(75.dp))
            }

            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                selectedPosition -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }

            //modifier is being applied to image inside Item
            val baseModifier = if (shouldHide) {
                Modifier.height(0.dp)
            } else {
                Modifier
            }

            SideBarUiItem(modifier = baseModifier
                .focusRequester(itemFocusRequesters[index])
                .onFocusChanged {
                    if (it.hasFocus) {
                        Log.e("PROFILE", "side bar ui item has focus called")
                        onFocusChange.invoke(index)
                    } else if (focusedIndex == index) {
                        onFocusChange.invoke(-1)
                    }
                }
                .focusable(enabled = isSideBarFocusable && !shouldHide)
                .focusProperties {
                    canFocus = isSideBarFocusable && !shouldHide
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    Log.e("SIDE_BAR_ITEM", "on side bar item click called with index $index")
                    if (index != columnItems.size - 1) {
                        Log.e("SIDE_BAR_ITEM", "on side bar item click called inside if block")
                        onSelectedPositionChange.invoke(index)
                        onFocusChange.invoke(-1)
                    }
                    onItemClick.invoke(item)
                }),
                focusedSideBarItem = focusedIndex,
                txt = item.name,
                img = item.img,
                focusState = if (shouldHide) FocusState.UNFOCUSED else uiState
            )
        }

    }
}

private suspend fun FocusRequester.safeRequestFocus(
    maxAttempts: Int = 3,
) {
    repeat(maxAttempts) { attempt ->
        try {
            requestFocus()
            return
        } catch (e: IllegalStateException) {
            // If LayoutNode is not attached, stop trying
            if (e.message?.contains("LayoutNode") == true && attempt == maxAttempts - 1) {
                Log.e("SideBarColumn", "Failed to request focus after $maxAttempts attempts: ${e.message}")
                return
            }
            awaitFrame()
        } catch (e: Exception) {
            // Catch any other exceptions and stop trying
            Log.e("SideBarColumn", "Error requesting focus: ${e.message}")
            return
        }
    }
}


@Preview
@Composable
fun SideBarRowPreview() {

    val sideBarTestList = listOf(
        SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        ), SideBarItem(
            name = "Search",
            img = R.drawable.search_ic,
            tag = "search",
        ), SideBarItem(
            name = "MyList",
            img = R.drawable.plus_ic,
            tag = "myList",
        ), SideBarItem(
            name = "Creators",
            img = R.drawable.group_person_ic,
            tag = "creators",
        ), SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        )
    )

//    SideBarColumn(
//        rowItems = sideBarTestList,
//        focusedIndex = -1
//
//    ) {
//
//    }

}
