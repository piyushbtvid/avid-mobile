package com.faithForward.media

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SideBarColumn(
    modifier: Modifier = Modifier,
    rowItems: List<SideBarItem>,
    focusedIndex: Int,
) {

    val itemFocusRequesters = remember { List(rowItems.size) { FocusRequester() } }


    LazyColumn(
        modifier = modifier
            .focusRestorer {
                itemFocusRequesters[0]
            }
            .onFocusChanged {
                Log.e(
                    "SIDE_BAR", "is Focused is ${it.isFocused}  and has focused is ${it.hasFocus}"
                )
//                if (it.hasFocus) {
//                    itemFocusRequesters[selectedPosition].requestFocus()
//                }
            },
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(rowItems) { index, item ->

            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                //    selectedPosition -> SideBarItemUiState.SELECTED
                else -> FocusState.UNFOCUSED
            }

            SideBarUiItem(
                modifier = Modifier
                    .focusRequester(itemFocusRequesters[index])
                    .onFocusChanged {
                        if (it.hasFocus) {
                            //  onFocusChange.invoke(index)
                        } else {
                            if (focusedIndex == index) {
                                //    onFocusChange.invoke(-1)
                            }
                        }
                    }
                    .focusable()
                    .clickable(interactionSource = null, indication = null, onClick = {
                        //    onSideBarItemClick.invoke(item)
                        //  onSelectedPositionChange.invoke(item)
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

    SideBarColumn(
        rowItems = sideBarTestList,
        focusedIndex = 1
    )

}
