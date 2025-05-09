package com.faithForward.media.sidebar

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState
import com.faithForward.media.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SideBarColumn(
    modifier: Modifier = Modifier,
    columnItems: List<SideBarItem>,
    focusedIndex: Int,
    selectedPosition: Int,
    onSelectedPositionChange: (Int) -> Unit,
    onFocusChange: (index: Int) -> Unit
) {
    val itemFocusRequesters = remember { List(columnItems.size) { FocusRequester() } }

    LazyColumn(
        modifier = modifier
        ,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(columnItems) { index, item ->
            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                selectedPosition -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }

            SideBarUiItem(
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
        ),
        SideBarItem(
            name = "Search",
            img = R.drawable.search_ic,
            tag = "search",
        ),
        SideBarItem(
            name = "MyList",
            img = R.drawable.plus_ic,
            tag = "myList",
        ),
        SideBarItem(
            name = "Creators",
            img = R.drawable.group_person_ic,
            tag = "creators",
        ),
        SideBarItem(
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
