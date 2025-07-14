package com.faithForward.media.home.carousel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState

data class CarouselContentRowDto(
    val carouselItemsDto: List<CarouselItemDto>,
)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CarouselContentRow(
    modifier: Modifier = Modifier,
    carouselList: List<CarouselItemDto>,
    rowIndex: Int,
    focusRequesters: MutableMap<Pair<Int, Int>, FocusRequester>,
    lastFocusedItem: Pair<Int, Int>,
    onItemFocused: (Pair<Int, Int>) -> Unit,
    shouldFocusOnFirstItem: Boolean = false,
    listState: LazyListState,
    onCarouselItemClick: (CarouselItemDto) -> Unit,
    onToggleFavorite: (String?) -> Unit,
    onToggleLike: (String?) -> Unit,
    onSearchClick: () -> Unit,
    onToggleDisLike: (String?) -> Unit,
) {

    val scope = rememberCoroutineScope()

    var carouselRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var micFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var searchFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var addToWatchListFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var likeFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var disLikeFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val itemFocusRequesters = remember { List(carouselList.size) { FocusRequester() } }

//    LaunchedEffect(shouldFocusOnFirstItem) {
//        if (shouldFocusOnFirstItem) {
//            try {
//                itemFocusRequesters[0].requestFocus()
//            } catch (ex: Exception) {
//                Log.e("FOCUS_ISSUE", "${ex.message}")
//            }
//        }
//    }
    HorizontalPager(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
            .focusRestorer {
                itemFocusRequesters[0]
            },
        state = rememberPagerState(
            pageCount = { carouselList.size }
        ),
    ) { index: Int ->

        focusRequesters[Pair(rowIndex, index)] = itemFocusRequesters[index]

        LaunchedEffect(lastFocusedItem) {
            if (lastFocusedItem == Pair(rowIndex, index)) {
                try {
                    itemFocusRequesters[index].requestFocus()
                } catch (_: Exception) {

                }
            }
        }

        val carouselItem = carouselList.get(index)
        val uiState = when (index) {
            carouselRowFocusedIndex -> FocusState.FOCUSED
            else -> FocusState.UNFOCUSED
        }

        val micUiState = when (index) {
            micFocusedIndex -> FocusState.FOCUSED
            else -> FocusState.UNFOCUSED
        }

        val searchUiState = when (index) {
            searchFocusedIndex -> FocusState.FOCUSED
            else -> FocusState.UNFOCUSED
        }

        val addToWatchListUiState = when (index) {
            addToWatchListFocusedIndex -> FocusState.FOCUSED
            else -> FocusState.UNFOCUSED
        }

        val likeUiState = when (index) {
            likeFocusedIndex -> FocusState.FOCUSED
            else -> FocusState.UNFOCUSED
        }

        val disLikeUiState = when (index) {
            disLikeFocusedIndex -> FocusState.FOCUSED
            else -> FocusState.UNFOCUSED
        }

        CarouselItem(
            modifier = Modifier
                .focusRequester(itemFocusRequesters[index])
                .onFocusChanged {
                    if (it.isFocused) {
                        onItemFocused(Pair(rowIndex, index))
                        carouselRowFocusedIndex = index
                        Log.d("Logging", "carouselRowFocusedIndex$index")
                        //  onChangeContentRowFocusedIndex.invoke(index)
                        //  playListItemFocusedIndex = index
                        //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                    } else {
                        if (carouselRowFocusedIndex == index) {
                            carouselRowFocusedIndex = -1
                            //  onChangeContentRowFocusedIndex.invoke(index)
                            // playListItemFocusedIndex = -1
                        }
                    }
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    onCarouselItemClick.invoke(carouselItem)
                }
                )
                .focusable(),
            carouselItemDto = carouselItem,
            focusState = uiState,
            searchUiSate = searchUiState,
            micUiState = micUiState,
            micModifier = Modifier
                .onFocusChanged {
                    if (it.hasFocus) {
                        //    onItemFocused(Pair(rowIndex, index))
                        micFocusedIndex = index
                        //  onChangeContentRowFocusedIndex.invoke(index)
                        //  playListItemFocusedIndex = index
                        //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                    } else {
                        if (micFocusedIndex == index) {
                            micFocusedIndex = -1
                            //  onChangeContentRowFocusedIndex.invoke(index)
                            // playListItemFocusedIndex = -1
                        }
                    }
                }
                .focusable(),
            searchIcModifier = Modifier
                .onFocusChanged {
                    if (it.hasFocus) {
                        searchFocusedIndex = index
                    } else {
                        if (searchFocusedIndex == index) {
                            searchFocusedIndex = -1
                        }
                    }
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    Log.e("SEARCH_IC", "on search click")
                    onSearchClick.invoke()
                }
                )
                .focusable(),
            addToWatchListModifier = Modifier
                .onFocusChanged {
                    if (it.hasFocus) {
                        addToWatchListFocusedIndex = index
                    } else {
                        if (addToWatchListFocusedIndex == index) {
                            addToWatchListFocusedIndex = -1
                        }
                    }
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    onToggleFavorite(carouselItem.slug)
                }
                )
                .focusable(),
            likeModifier = Modifier
                .onFocusChanged {
                    if (it.hasFocus) {
                        likeFocusedIndex = index
                    } else {
                        if (likeFocusedIndex == index) {
                            likeFocusedIndex = -1
                        }
                    }
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    onToggleLike(carouselItem.slug)
                }
                )
                .focusable(),
            disLikeModifier = Modifier
                .onFocusChanged {
                    if (it.hasFocus) {
                        disLikeFocusedIndex = index
                    } else {
                        if (disLikeFocusedIndex == index) {
                            disLikeFocusedIndex = -1
                        }
                    }
                }
                .clickable(interactionSource = null, indication = null, onClick = {
                    onToggleDisLike(carouselItem.slug)
                }
                )
                .focusable(),
            addToWatchListUiState = addToWatchListUiState,
            likeUiState = likeUiState,
            dislikeUiState = disLikeUiState,
        )
    }

}