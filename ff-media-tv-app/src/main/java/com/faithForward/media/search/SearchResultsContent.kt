package com.faithForward.media.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.faithForward.media.search.item.SearchItemDto
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.util.Resource

@Composable
fun SearchResultsContent(
    searchResults: Resource<SearchContentDto>,
    modifier: Modifier = Modifier,
    lastFocusedIndex: Int,
    focusRequesterList: List<FocusRequester>,
    onSearchItemClick: (SearchItemDto) -> Unit,
    onLastFocusIndexChange: (Int) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        when (searchResults) {
            is Resource.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp), color = focusedMainColor
                )
            }

            is Resource.Success -> {
                if (searchResults.data?.searchItemList?.isNotEmpty() == true) {
                    SearchGrid(
                        searchResultList = searchResults.data?.searchItemList!!,
                        modifier = Modifier.padding(top = 16.dp),
                        lastFocusedIndex = lastFocusedIndex,
                        focusRequesterList = focusRequesterList,
                        onLastFocusIndexChange = onLastFocusIndexChange,
                        onSearchItemClick = onSearchItemClick
                    )
                } else {
                    Text(
                        text = "No results found",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier

                    )
                }
            }

            is Resource.Error -> {
                searchResults.message?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier

                    )
                }
            }

            is Resource.Unspecified -> {}
        }
    }

}