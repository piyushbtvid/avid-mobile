package com.faithForward.media.ui.sections.search

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyBoardUi
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyboardMode
import com.faithForward.media.ui.sections.search.custom_keyboard.NewKeyboardActionState
import com.faithForward.media.ui.sections.search.item.SearchItemDto
import com.faithForward.media.ui.sections.search.recent.RecentSearch
import com.faithForward.media.ui.theme.cardShadowColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.viewModel.SearchViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.SearchEvent

data class SearchUiScreenDto(
    val searchItemDtoList: List<SearchItemDto>?,
)

//Old Search Screen
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
            Log.e(
                "ON_BACK",
                "on back in home called with side Bar focused index ${sideBarState.sideBarFocusedIndex}"
            )
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


// New Search Screen

@Composable
fun SearchScreenUi(
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel,
    sideBarViewModel: SideBarViewModel,
    onBackClick: () -> Unit,
    onSearchItemClick: (SearchItemDto) -> Unit,
) {

    var recentSearchFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var searchResultFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var searchResultLastFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    var searchInputText by rememberSaveable { mutableStateOf("") }
    var currentKeyboardMode by rememberSaveable { mutableStateOf(KeyboardMode.ALPHABET) }

    val uiState by searchViewModel.searchUiState.collectAsState()
    val sideBarState by sideBarViewModel.sideBarState

    val searchResultFocusRequesterList =
        remember(uiState.result?.searchItemDtoList?.size ?: 0) {
            List(uiState.result?.searchItemDtoList?.size ?: 0) { FocusRequester() }
        }

    BackHandler {
        Log.e("ON_BACK", "on back in search called")
        if (sideBarState.sideBarFocusedIndex != -1) {
            Log.e(
                "ON_BACK",
                "on back in home called with side Bar focused index ${sideBarState.sideBarFocusedIndex}"
            )
            onBackClick.invoke()
        } else {
            sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(0))
        }
    }

    LaunchedEffect(searchInputText) {
        Log.e("INPUT_TEXT", "on input text change called with $searchInputText")
        if (searchInputText.length >= 3) {
            searchViewModel.onEvent(SearchEvent.SubmitQuery(searchInputText))
        }
        if (searchInputText.length <= 2) {
            searchViewModel.onEvent(SearchEvent.EmptySearchResult)
        }
    }

    LaunchedEffect(Unit) {
        if (searchResultLastFocusedIndex > -1 && searchResultFocusRequesterList.isNotEmpty()) {
            try {
                Log.e("SEARCH_RESULT", "on Search Last Request Focus called")
                searchResultFocusRequesterList[searchResultLastFocusedIndex].requestFocus()
            } catch (_: Exception) {

            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .padding(
                    top = 34.dp,
                    end = 21.8.dp
                )
                .size(15.dp),
            painter = painterResource(R.drawable.fi_sr_user),
            contentDescription = "profile image"
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 73.dp, start = 100.dp, end = 0.dp),
            verticalArrangement = Arrangement.spacedBy(23.5.dp)
        ) {

            KeyBoardUi(
                searchResultLastFocusedIndex = searchResultLastFocusedIndex,
                searchInputText = searchInputText,
                currentKeyboardMode = currentKeyboardMode,
                onInputTextChange = { string ->
                    searchInputText += string
                },
                onKeyBoardActionButtonClick = { state ->

                    when (state) {
                        NewKeyboardActionState.space -> {
                            searchInputText += " "
                        }

                        NewKeyboardActionState.clear -> {
                            if (searchInputText.isNotEmpty()) {
                                searchInputText = searchInputText.dropLast(1)
                            }
                        }

                        NewKeyboardActionState.number -> {
                            // switch to number keyboard layout
                            currentKeyboardMode = KeyboardMode.NUMBER
                        }

                        NewKeyboardActionState.alphabet -> {
                            // switch to alphabet keyboard layout
                            currentKeyboardMode = KeyboardMode.ALPHABET
                        }

                        NewKeyboardActionState.clearAll -> {}
                        NewKeyboardActionState.small -> {}
                        NewKeyboardActionState.large -> {}
                    }
                }
            )


            Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {


                if (uiState.recentSearch != null) {

                    RecentSearch(
                        list = uiState.recentSearch!!,
                        lastFocusedIndex = recentSearchFocusedIndex,
                        onFocusedIndexChange = { int ->
                            recentSearchFocusedIndex = int
                        },
                        onItemClick = { value ->
                            searchInputText = value
                        }
                    )

                }



                when {
                    uiState.result?.searchItemDtoList?.isNotEmpty() == true -> {
                        SearchLazyList(
                            lastFocusedIndex = searchResultFocusedIndex,
                            searchResultList = uiState.result!!.searchItemDtoList!!,
                            searchResultFocusRequesterList = searchResultFocusRequesterList,
                            onSearchResultFocusedIndexChange = { int ->
                                searchResultFocusedIndex = int
                            },
                            onItemClick = { item ->
                                if (item.itemId != null && item.contentType != null) {
                                    searchViewModel.onEvent(
                                        SearchEvent.SaveToRecentSearch(
                                            contentType = item.contentType,
                                            contentID = item.itemId
                                        )
                                    )
                                }
                                onSearchItemClick.invoke(item)
                            },
                            onSearchLastFocusedIndexChange = { int ->
                                searchResultLastFocusedIndex = int
                            }
                        )
                    }

                    uiState.result?.searchItemDtoList?.isEmpty() == true -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No results found",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                            )
                        }
                    }

                    else -> {
                    }
                }


            }


        }


    }


}


@Preview(
    name = "Search-NewUi", widthDp = 1920, heightDp = 1080, showBackground = true
)
@Composable
private fun SearchUiPreview() {
    val list = listOf(
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
    )

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
    //SearchScreenUi()
}