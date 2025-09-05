package com.faithForward.media.ui.sections.creator.list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.creator.card.CreatorCard
import com.faithForward.media.ui.sections.creator.card.CreatorCardDto
import com.faithForward.media.util.CustomGridCells
import com.faithForward.media.util.CustomLazyGrid
import com.faithForward.media.util.Util.isTvDevice


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatorCardGrid(
    modifier: Modifier = Modifier,
    rowIndex: Int,
    focusRequesters: MutableMap<Pair<Int, Int>, FocusRequester>,
    onItemFocused: (Pair<Int, Int>) -> Unit,
    lastFocusedItem: Pair<Int, Int>,
    onItemClick: (CreatorCardDto) -> Unit,
    creators: List<CreatorCardDto>,
) {
    var focusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val isTv = LocalContext.current.isTvDevice()
    val density = LocalDensity.current

    // Initialize focus requesters for all creators
    val itemFocusRequesters = remember { List(creators.size) { FocusRequester() } }

    Log.d("CreatorCardGrid", "size: ${creators.size}, isTv: $isTv")

    var containerWidth by remember { mutableIntStateOf(0) }
    
    Box(
        modifier = modifier.fillMaxSize().onGloballyPositioned { coords ->
            containerWidth = coords.size.width
        }
    ) {
        if (containerWidth > 0) {
            val itemWidth = if (isTv) 155.dp else 116.dp
            val horizontalSpacing = if (isTv) 10.dp else 5.dp
            val horizontalPadding = (if (isTv) 20.dp else 10.dp ) * 2 // Left and right padding
            
            // Calculate available width for items
            val availableWidth = containerWidth - with(density) { horizontalPadding.roundToPx() }
            val itemWidthPx = with(density) { itemWidth.roundToPx() }
            val spacingPx = with(density) { horizontalSpacing.roundToPx() }
            
            // Calculate number of columns that can fit
            val columnsCount = ((availableWidth + spacingPx) / (itemWidthPx + spacingPx)).coerceAtLeast(1)
            
            Log.d("CreatorCardGrid", "Container width: $containerWidth, Item width: $itemWidth, Columns: $columnsCount")
            
            val creatorsList = creators.chunked(columnsCount)
            val rowListStates = remember { mutableMapOf<Int, LazyListState>() }
            creatorsList.forEachIndexed { index, _ ->
                rowListStates[index] = rowListStates[index] ?: rememberLazyListState()
            }
            
            Column(
                modifier = Modifier.padding(top = 30.dp, start = if (isTv) 40.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                creatorsList.forEachIndexed { creatorsListIndex, creatorCardDtos ->
                    val isFirstRow = creatorsListIndex == 0
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (isFirstRow) Modifier.focusRestorer { itemFocusRequesters[0] }
                                else Modifier
                            ),
                        state = rowListStates[creatorsListIndex] ?: rememberLazyListState(),
                        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        itemsIndexed(creatorCardDtos) { index, creator ->
                            val absoluteIndex = creatorsListIndex * columnsCount + index

                            focusRequesters[Pair(rowIndex, absoluteIndex)] = itemFocusRequesters[absoluteIndex]

                            LaunchedEffect(lastFocusedItem) {
                                if (lastFocusedItem == Pair(rowIndex, absoluteIndex)) {
                                    try {
                                        itemFocusRequesters[absoluteIndex].requestFocus()
                                        rowListStates[creatorsListIndex]?.scrollToItem(index)
                                    } catch (_: Exception) {
                                        // Handle any errors
                                    }
                                }
                            }

                            CreatorCard(
                                creatorCardDto = creator,
                                isFocused = absoluteIndex == focusedIndex,
                                modifier = Modifier
                                    .width(itemWidth)
                                    .focusRequester(itemFocusRequesters[absoluteIndex])
                                    .onFocusChanged {
                                        if (it.isFocused) {
                                            onItemFocused(Pair(rowIndex, absoluteIndex))
                                            focusedIndex = absoluteIndex
                                        } else {
                                            if (focusedIndex == absoluteIndex) {
                                                focusedIndex = -1
                                            }
                                        }
                                    }
                                    .clickable(
                                        interactionSource = null,
                                        indication = null,
                                        onClick = { onItemClick.invoke(creator) }
                                    )
                                    .focusable(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreatorCardGridPreview() {
//    CreatorCardGrid(
//        creators = listOf(
//            CreatorCardDto(
//                creatorSubscriberText = "130K Subscribers",
//                creatorImageUrl = "",
//                creatorName = "Jasmine Wright",
//                channelDescription = "jhb,hb",
//                id = 1
//            ),
//            CreatorCardDto(
//                creatorSubscriberText = "130K Subscribers",
//                creatorImageUrl = "",
//                creatorName = "Jasmine Wright",
//                channelDescription = "jhb,hb",
//                id = 1
//            ),
//            CreatorCardDto(
//                creatorSubscriberText = "130K Subscribers",
//                creatorImageUrl = "",
//                creatorName = "Jasmine Wright",
//                channelDescription = "jhb,hb",
//                id = 1
//            ),
//            CreatorCardDto(
//                creatorSubscriberText = "130K Subscribers",
//                creatorImageUrl = "",
//                creatorName = "Jasmine Wright",
//                channelDescription = "jhb,hb",
//                id = 1
//            ),
//        ),
//        onItemClick = {
//
//        },
//        rowIndex = 1,
//        onItemFocused = Pair(1,2)
//    )
}