package com.faithForward.media.search

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.faithForward.media.search.item.SearchItemDto
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.theme.cardShadowColor
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.viewModel.SearchViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.SearchEvent

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onSearchItemClick: (SearchItemDto) -> Unit,
    sideBarViewModel: SideBarViewModel,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf(uiState.query) }
    val sideBarState by sideBarViewModel.sideBarState

    BackHandler {
        Log.e("ON_BACK", "on back in search called")
        if (sideBarState.sideBarFocusedIndex != -1) {
            Log.e("ON_BACK", "on back in home called with side Bar focused index ${sideBarState.sideBarFocusedIndex}")
            onBackClick.invoke()
        } else {
            sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(0))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor)
    ) {
        SearchContent(
            modifier = modifier,
            searchQuery = searchQuery,
            onSearchQueryChange = { query ->
                searchQuery = query
                if (query.length >= 3) {
                    viewModel.onEvent(SearchEvent.SubmitQuery(query))
                }
            },
            searchResults = uiState.searchResults,
            onSearchItemClick = onSearchItemClick
        )
        if (sideBarState.sideBarFocusedIndex != -1) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                cardShadowColor.copy(alpha = 0.9f),
                                cardShadowColor.copy(alpha = 0.6f),
                                cardShadowColor.copy(alpha = 0.5f),
                                cardShadowColor.copy(alpha = 0.4f),
                                whiteMain.copy(alpha = 0f),
                            )
                        )
                    )
            )
        }
    }
}