package com.faithForward.media.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.faithForward.media.viewModel.SearchViewModel
import com.faithForward.media.viewModel.uiModels.SearchEvent

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf(uiState.query) }

    SearchContent(
        modifier = modifier,
        searchQuery = searchQuery,
        onSearchQueryChange = { query ->
            searchQuery = query
            if (query.length >= 3) {
                viewModel.onEvent(SearchEvent.SubmitQuery(query))
            }
        },
        searchResults = uiState.searchResults
    )
}