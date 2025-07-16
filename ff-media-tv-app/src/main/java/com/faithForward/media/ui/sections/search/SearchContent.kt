package com.faithForward.media.ui.sections.search

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.search.item.SearchItemDto
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.util.Resource


data class SearchContentDto(
    val searchItemList: List<SearchItemDto>,
)




@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchItemClick: (SearchItemDto) -> Unit,
    searchResults: Resource<SearchContentDto>,
) {

    var isSearchBoxFocused by remember { mutableStateOf(false) }
    val searchBoxFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var lastFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    val isSearchBoxVisible = remember(lastFocusedIndex) {
        isSearchBoxFocused || lastFocusedIndex < 4
    }

    val targetHeight = if (isSearchBoxVisible) 60.dp else 0.dp

    val searchBarAnimatedHeight by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )

    val focusRequesters = remember(searchResults.data?.searchItemList?.size ?: 0) {
        List(searchResults.data?.searchItemList?.size ?: 0) { FocusRequester() }
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor)
            .padding(start = 63.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (searchBarAnimatedHeight > 0.dp) {
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .height(searchBarAnimatedHeight)
                    .clipToBounds(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(searchBoxFocusRequester),
                    isSearchBarFocused = isSearchBoxFocused,
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange,
                    onSearchBarFocusChange = { isSearchBoxFocused = it },
                    keyboardController = keyboardController,
                    searchBarFocusRequester = searchBoxFocusRequester,
                    onSearchClick = {
                        Log.e(
                            "SEARCH",
                            "onSearchClick called, results size: ${searchResults.data?.searchItemList?.size ?: 0}"
                        )
                        // Optional: Trigger focus manually if needed
                        if (searchResults is Resource.Success && searchResults.data?.searchItemList?.isNotEmpty() == true) {
                            try {
                                Log.e("SEARCH", "Requesting focus on grid")
                                if (focusRequesters.isNotEmpty()) {
                                    focusRequesters[0].requestFocus()
                                }
                            } catch (ex: Exception) {
                                Log.e("SEARCH", "Focus exception: ${ex.message}")
                            }
                        }
                    }
                )
            }
        }



        SearchResultsContent(
            searchResults = searchResults,
            modifier = Modifier.fillMaxSize(),
            focusRequesterList = focusRequesters,
            lastFocusedIndex = lastFocusedIndex,
            onLastFocusIndexChange = { index ->
                lastFocusedIndex = index
            },
            onSearchItemClick = onSearchItemClick
        )
    }

    // Auto-focus search bar on screen load
    LaunchedEffect(Unit) {
        try {
            if (lastFocusedIndex == -1 && isSearchBoxVisible) {
                searchBoxFocusRequester.requestFocus()
            }
        } catch (ex: Exception) {
            Log.e("SEARCH", "Search bar focus exception: ${ex.message}")
        }
    }


    // Restore focus to the last focused item when returning to the screen of search grid
    LaunchedEffect(Unit) {
        try {
            if (lastFocusedIndex >= 0 && lastFocusedIndex < focusRequesters.size) {
                Log.e(
                    "SEARCH_LAST",
                    "last focused index request focus called with last index is $lastFocusedIndex"
                )
                focusRequesters[lastFocusedIndex].requestFocus()
            } else if (focusRequesters.isNotEmpty()) {
                //  focusRequesterList[0].requestFocus() // Fallback to first item if no last focused
            }
        } catch (ex: Exception) {
            Log.e("GenreCardGrid", "Error requesting focus: ${ex.message}")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchContentPreview() {

//    SearchContent(
//        searchQuery = "Lord Jesus",
//        onSearchQueryChange = {
//
//        },
//        searchScreenState = SearchState(
//            query = "",
//
//            ),
//    )

}
