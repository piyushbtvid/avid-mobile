package com.faithForward.media.sidebar

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SideBarColumn(
    modifier: Modifier = Modifier,
    columnItems: List<SideBarItem>,
    focusedIndex: Int,
    selectedPosition: Int,
    isSideBarFocusable: Boolean,
    onSelectedPositionChange: (Int) -> Unit,
    onFocusChange: (index: Int) -> Unit
) {
    val itemFocusRequesters = remember { List(columnItems.size) { FocusRequester() } }

    var targetValue = if (focusedIndex == -1) 38.dp else 114.dp
    val animatedWidth by animateDpAsState(
        targetValue = targetValue,
        animationSpec = tween(300),
        label = ""
    )

    LazyColumn(
        modifier = modifier
            .focusRestorer {
                itemFocusRequesters[1]
            }
            .onFocusChanged {
                Log.d("onFocusChanged", "${it.hasFocus} ${it.isFocused} ${it.isCaptured}")
                if (it.hasFocus) {
                    itemFocusRequesters[selectedPosition].requestFocus()
                }
            },
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(columnItems) { index, item ->
            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                selectedPosition -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }

            SideBarUiItem(modifier = Modifier
                .focusRequester(itemFocusRequesters[index])
                .onFocusChanged {
                    if (it.hasFocus) {
                        onFocusChange.invoke(index)
                    } else {
                        if (focusedIndex == index) {
                            onFocusChange.invoke(-1)
                        }
                    }
                }
                .focusable(enabled = isSideBarFocusable)
                .focusProperties {
                    canFocus = isSideBarFocusable
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    onSelectedPositionChange.invoke(index)
                    onFocusChange.invoke(-1)
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
