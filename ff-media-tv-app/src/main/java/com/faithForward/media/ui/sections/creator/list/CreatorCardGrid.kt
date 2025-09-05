package com.faithForward.media.ui.sections.creator.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.util.Log
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

    // Debug logging
    Log.d("CreatorCardGrid", "Creators count: ${creators.size}")
    Log.d("CreatorCardGrid", "Creators: $creators")

    // Use test data if creators list is empty for debugging
    val displayCreators = if (creators.isEmpty()) {
        Log.d("CreatorCardGrid", "Using test data - creators list is empty")
        listOf(
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Test Creator 1",
                channelDescription = "Test description",
                id = 1
            ),
            CreatorCardDto(
                creatorSubscriberText = "250K Subscribers",
                creatorImageUrl = "",
                creatorName = "Test Creator 2",
                channelDescription = "Test description 2",
                id = 2
            ),
            CreatorCardDto(
                creatorSubscriberText = "500K Subscribers",
                creatorImageUrl = "",
                creatorName = "Test Creator 3",
                channelDescription = "Test description 3",
                id = 3
            )
        )
    } else {
        creators
    }

    // Initialize focus requesters for all creators
    val itemFocusRequesters = remember { List(displayCreators.size) { FocusRequester() } }

    CustomLazyGrid(
        modifier = modifier.padding(top = 30.dp, start = if (LocalContext.current.isTvDevice()) 40.dp else 0.dp),
        items = displayCreators,
        columns = CustomGridCells.Fixed(3),
        verticalSpacing = 10.dp,
        horizontalSpacing = 10.dp,
        columnContentPadding = PaddingValues(vertical = 0.dp),
        rowContentPadding = PaddingValues(horizontal = 20.dp),
        itemContent = { index, creator ->
            // Assign focus requester using index
            focusRequesters[Pair(rowIndex, index)] = itemFocusRequesters[index]

            // Restore focus to the last focused item
            LaunchedEffect(lastFocusedItem) {
                if (lastFocusedItem == Pair(rowIndex, index)) {
                    try {
                        itemFocusRequesters[index].requestFocus()
                    } catch (_: Exception) {
                        // Handle any errors
                    }
                }
            }

            CreatorCard(
                creatorCardDto = creator,
                isFocused = index == focusedIndex,
                modifier = Modifier
                    .focusRequester(itemFocusRequesters[index])
                    .onFocusChanged {
                        if (it.isFocused) {
                            onItemFocused(Pair(rowIndex, index))
                            focusedIndex = index
                        } else {
                            if (focusedIndex == index) {
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
    )
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