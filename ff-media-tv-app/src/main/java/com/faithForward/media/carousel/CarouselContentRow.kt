package com.faithForward.media.carousel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState
import com.faithForward.network.dto.Item

data class CarouselContentRowDto(
    val carouselItemsDto: List<CarouselItemDto>
)

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CarouselContentRow(
    modifier: Modifier = Modifier,
    carouselList: List<CarouselItemDto>,
    shouldFocusOnFirstItem: Boolean = false,
) {

    var carouselRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val itemFocusRequesters = remember { List(carouselList.size) { FocusRequester() } }

    LaunchedEffect(shouldFocusOnFirstItem) {
        if (shouldFocusOnFirstItem) {
            try {
                itemFocusRequesters[0].requestFocus()
            } catch (ex: Exception) {
                Log.e("FOCUS_ISSUE", "${ex.message}")
            }
        }
    }

    PositionFocusedItemInLazyLayout(
        parentFraction = 0f,
        content = {
            LazyRow(
                modifier =
                modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp)
                    .focusRestorer {
                        itemFocusRequesters[0]
                    },
                horizontalArrangement = Arrangement.spacedBy(9.dp),
            )
            {
                itemsIndexed(carouselList) { index, carouselItem ->

                    val uiState = when (index) {
                        carouselRowFocusedIndex -> FocusState.FOCUSED
                        else -> FocusState.UNFOCUSED
                    }

                    CarouselItem(
                        modifier = Modifier
                            .focusRequester(itemFocusRequesters[index])
                            .onFocusChanged {
                                if (it.hasFocus) {
                                    //    onItemFocused(Pair(rowIndex, index))
                                    carouselRowFocusedIndex = index
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
                            .focusable(),
                        carouselItemDto = carouselItem
                    )
                }
            }
        }
    )

}