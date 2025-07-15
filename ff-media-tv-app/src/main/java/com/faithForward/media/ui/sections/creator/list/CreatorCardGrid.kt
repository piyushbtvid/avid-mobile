package com.faithForward.media.ui.sections.creator.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.creator.card.CreatorCard
import com.faithForward.media.ui.sections.creator.card.CreatorCardDto


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

    // Initialize focus requesters for all creators
    val itemFocusRequesters = remember { List(creators.size) { FocusRequester() } }

    // Chunk creators into groups of 5
    val creatorsList = creators.chunked(5)

    // Map to store LazyListState for each LazyRow
    val rowListStates = remember { mutableMapOf<Int, LazyListState>() }
    creatorsList.forEachIndexed { index, _ ->
        rowListStates[index] = rowListStates[index] ?: rememberLazyListState()
    }

    Column(
        modifier = modifier.padding(top = 30.dp, start = 40.dp),
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
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                itemsIndexed(creatorCardDtos) { index, creator ->
                    // Calculate absolute index for the item
                    val absoluteIndex = creatorsListIndex * 5 + index

                    // Assign focus requester using absolute index
                    focusRequesters[Pair(rowIndex, absoluteIndex)] =
                        itemFocusRequesters[absoluteIndex]

                    // Restore focus to the last focused item
                    LaunchedEffect(lastFocusedItem) {
                        if (lastFocusedItem == Pair(rowIndex, absoluteIndex)) {
                            try {
                                itemFocusRequesters[absoluteIndex].requestFocus()
                                // Scroll to the item in the correct LazyRow
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