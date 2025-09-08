package com.faithForward.media.ui.sections.common_ui.content

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.PosterCard
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.util.FocusState
import com.faithForward.media.util.Util.isTvDevice

data class PosterRowDto(
    val heading: String,
    val rowId: String,
    val dtos: List<PosterCardDto>,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContentRow(
    modifier: Modifier = Modifier,
    posterRowDto: PosterRowDto,
    onItemClick: (PosterCardDto, List<PosterCardDto>, String) -> Unit,
    rowIndex: Int,
    focusRequesters: MutableMap<Pair<Int, Int>, FocusRequester>,
    onItemFocused: (Pair<Int, Int>) -> Unit,
    lastFocusedItem: Pair<Int, Int>,
    listState: LazyListState,
    showContentOfCard: Boolean,
    shouldFocusOnFirstItem: Boolean = false,
    onChangeContentRowFocusedIndex: (Int) -> Unit,
) {

    var contentRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    val itemFocusRequesters = remember { List(posterRowDto.dtos.size) { FocusRequester() } }

//    LaunchedEffect(shouldFocusOnFirstItem) {
//        if (shouldFocusOnFirstItem) {
//            try {
//                itemFocusRequesters[0].requestFocus()
//            } catch (ex: Exception) {
//                Log.e("FOCUS_ISSUE", "${ex.message}")
//            }
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        TitleText(
            text = posterRowDto.heading, modifier = Modifier.padding(start = 25.dp)
        )
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .focusRestorer {
                    itemFocusRequesters.firstOrNull() ?: FocusRequester()
                },
            contentPadding = PaddingValues(start = 25.dp, end = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        )
        {
            itemsIndexed(posterRowDto.dtos) { index, posterCardDto ->

                if (index < itemFocusRequesters.size) {
                    focusRequesters[Pair(rowIndex, index)] = itemFocusRequesters[index]
                }

                // Restore focus to the last focused item when returning to this row
                LaunchedEffect(lastFocusedItem) {
                    if (lastFocusedItem == Pair(
                            rowIndex,
                            index
                        ) && index < itemFocusRequesters.size
                    ) {
                        try {
                            itemFocusRequesters[index].requestFocus()
                        } catch (_: Exception) {
                        }
                    }
                }


                val uiState = when (index) {
                    contentRowFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                PosterCard(
                    modifier = Modifier
                        .focusRequester(
                            itemFocusRequesters.getOrNull(index) ?: remember { FocusRequester() })
                        .onFocusChanged {
                            if (it.hasFocus) {
                                onItemFocused(Pair(rowIndex, index))
                                contentRowFocusedIndex = index
                                //  onChangeContentRowFocusedIndex.invoke(index)
                                //  playListItemFocusedIndex = index
                                //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                            } else {
                                if (contentRowFocusedIndex == index) {
                                    contentRowFocusedIndex = -1
                                    //  onChangeContentRowFocusedIndex.invoke(index)
                                    // playListItemFocusedIndex = -1
                                }
                            }
                        }
                        .focusable(),
                    posterCardDto = posterCardDto,
                    focusState = uiState,
                    showContent = showContentOfCard,
                    onItemClick = { item ->
                        onItemClick.invoke(item, posterRowDto.dtos, posterRowDto.rowId)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.width(if (LocalContext.current.isTvDevice()) 100.dp else 10.dp))
            }
        }
    }


}