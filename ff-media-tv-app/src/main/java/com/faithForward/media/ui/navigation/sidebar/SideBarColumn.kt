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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.navigation.Routes
import com.faithForward.media.util.FocusState

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
    val itemFocusRequesters = remember { List(columnItems.size) { FocusRequester() } }

    var targetValue = if (focusedIndex == -1) 38.dp else 114.dp
    val animatedWidth by animateDpAsState(
        targetValue = targetValue, animationSpec = tween(300), label = ""
    )


    // Ensure focus is requested when focusedIndex changes
    LaunchedEffect(focusedIndex, isSideBarFocusable) {
        Log.e("FOCUSED_INDEX", "focused index changed with $focusedIndex and $isSideBarFocusable")
        if (focusedIndex != -1 && isSideBarFocusable) {
            try {
                if (focusedIndex < itemFocusRequesters.size) {
                    Log.e("SIDE_BAR_ITEM", "on side bar item request called")
                    itemFocusRequesters[focusedIndex].requestFocus()
                }
            } catch (ex: Exception) {
                Log.e("SideBarColumn", "Error requesting focus: ${ex.message}")
            }
        }
    }

    LazyColumn(
        modifier = modifier.onFocusChanged {
            Log.d("onFocusChanged", "${it.hasFocus} ${it.isFocused} ${it.isCaptured}")
            if (it.hasFocus && isSideBarFocusable) {
                try {
                    val targetIndex = selectedPosition.coerceIn(0, itemFocusRequesters.size - 1)
                    itemFocusRequesters[targetIndex].requestFocus()
                } catch (ex: Exception) {
                    Log.e("LOG", "${ex.message}")
                }
            }
        },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(columnItems) { index, item ->
            // Check if user items (MyList, MyAccount, Logout) exist in the list
            val hasUserItems = columnItems.any {
                it.tag == Routes.MyList.route ||
                        it.tag == Routes.MyAccount.route ||
                        it.tag == "log_out"
            }

            // Skip rendering last 2 items when focusedIndex == -1 ONLY if user items exist
            // This prevents hiding Series and Movies when login is disabled
            val isLastTwoItem = index >= columnItems.size - 2
            if (isLastTwoItem && focusedIndex == -1 && hasUserItems) return@itemsIndexed

            // Add top space before second-last item ONLY if user items exist
            // This prevents gap between Creators and Series when login is disabled
            if (index == columnItems.size - 2 && focusedIndex != -1 && hasUserItems) {
                Spacer(modifier = Modifier.height(75.dp))
            }

            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                selectedPosition -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }

            //modifier is being applied to image inside Item
            SideBarUiItem(modifier = Modifier
                .focusRequester(itemFocusRequesters[index])
                .onFocusChanged {
                    if (it.hasFocus) {
                        Log.e("PROFILE", "side bar ui item has focus called")
                        onFocusChange.invoke(index)
                    } else if (focusedIndex == index) {
                        onFocusChange.invoke(-1)
                    }
                }
                .focusable(enabled = isSideBarFocusable)
                .focusProperties {
                    canFocus = isSideBarFocusable
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
                focusState = uiState
            )
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
