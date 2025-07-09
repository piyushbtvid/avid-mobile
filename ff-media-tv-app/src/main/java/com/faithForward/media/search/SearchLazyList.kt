package com.faithForward.media.search

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.search.item.SearchItemDto
import com.faithForward.media.search.item.SearchUiItem
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchLazyList(
    modifier: Modifier = Modifier,
    lastFocusedIndex: Int,
    searchResultList: List<SearchItemDto>,
) {


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .focusRestorer(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        itemsIndexed(searchResultList) { index, item ->

            val uiState = when (index) {
                lastFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }
            SearchUiItem(
                modifier = Modifier
                    .onFocusChanged {
                        if (it.isFocused) {
                            Log.e("SEARCH_GRID", "Item $index gained focus")
                            //  onLastFocusIndexChange.invoke(index)
                            // Ensure the item is visible
//                            coroutineScope.launch {
//                                scrollState.scrollToItem(index)
//                            }
                        }
                    }
                    .focusable()
                    .border(
                        width = if (uiState == FocusState.FOCUSED) 2.dp else 0.dp,
                        color = if (uiState == FocusState.FOCUSED) focusedMainColor else Color.Transparent,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 3.dp),
                searchItemDto = item,
                onItemClick = {
                    Log.e("SEARCH_GRID", "Item $index clicked")
                    // onSearchItemClick.invoke(item)
                },
                focusState = uiState,
            )

        }

    }

}


@Preview
@Composable
private fun SearchListPreview() {
    val resultList = listOf(
        SearchItemDto(
            title = "The Saga of water lbsdlibsd lbaslbclblbs ljbslbf   ,bdclibsaf   aslbavslb",
            imdb = "PG",
            duration = "1h 48m",
            genre = "Drama,Comedy,Thriler"
        ),
        SearchItemDto(
            imdb = "Tv-14",
            title = "The Last Ride",
            creatorViews = "300k Views",
            creatorName = "Terrel Jones",
            creatorUploadDate = "5 month ago",
            creatorVideoNumber = "16 Videos"
        ),

        SearchItemDto(
            imdb = "PG",
            seasonNumber = "6 Seasons",
            title = "The Last Ride",
            genre = "Drama,Comedy,Thriler"
        ),

        SearchItemDto(
            imdb = "PG",
            seasonNumber = "6 Seasons",
            title = "The Last Ride",
            genre = "Drama,Comedy,Thriler"
        ),


        )

    SearchLazyList(
        lastFocusedIndex = 1,
        searchResultList = resultList
    )
}