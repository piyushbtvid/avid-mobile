package com.faithForward.media.home.creator.list

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.home.creator.card.CreatorCard
import com.faithForward.media.home.creator.card.CreatorCardDto


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatorCardGrid(
    modifier: Modifier = Modifier,
    creators: List<CreatorCardDto>
) {
    var focusedIndex by remember { mutableIntStateOf(-1) }

    val creatorsList = creators.chunked(5)

    val focusRequester = remember { FocusRequester() }

    creatorsList.forEachIndexed { creatorsListIndex, creatorCardDtos ->
        Column(
            modifier = modifier.padding(top = 30.dp, start = 40.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val isFirstRow = creatorsListIndex == 0
            LazyRow(
                modifier = (if (isFirstRow) Modifier.focusRestorer {
                    focusRequester
                } else Modifier).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                itemsIndexed(creatorCardDtos) { index, creator ->
                    val absoluteIndex = creatorsListIndex * 5 + index
                    CreatorCard(
                        creatorCardDto = creator, isFocused = absoluteIndex == focusedIndex,
                        modifier = (if (isFirstRow && index == 0) Modifier.focusRequester(
                            focusRequester
                        ) else Modifier)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    focusedIndex = absoluteIndex
                                } else {
                                    if (focusedIndex == absoluteIndex) {
                                        focusedIndex = -1
                                    }
                                }
                            }
                            .focusable(),
                    )
                }
            }
        }
    }

//    LazyVerticalGrid(
//        columns = GridCells.Adaptive(minSize = 140.dp),
//        modifier = modifier
//            .heightIn(max = 600.dp)
//            .padding(top = 35.dp),
//        contentPadding = PaddingValues(top = 12.dp, bottom = 50.dp, start = 63.dp, end = 70.dp),
//        horizontalArrangement = Arrangement.spacedBy(space = 10.dp, Alignment.CenterHorizontally),
//        verticalArrangement = Arrangement.spacedBy(space = 10.dp, Alignment.CenterVertically)
//    ) {
//        itemsIndexed(creators) { index: Int, creator: CreatorCardDto ->
//            CreatorCard(
//                creatorCardDto = creator, isFocused = index == focusedIndex,
//                modifier = Modifier
//                    .focusRestorer()
//                    .onFocusChanged {
//                        if (it.isFocused) {
//                            focusedIndex = index
//                        } else {
//                            if (focusedIndex == index) {
//                                focusedIndex = -1
//                            }
//                        }
//                    }
//                    .focusable(),
//            )
//        }
//    }

}

@Preview
@Composable
private fun CreatorCardGridPreview() {
    CreatorCardGrid(
        creators = listOf(
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
                channelDescription = "jhb,hb"
            ),
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
                channelDescription = "jhb,hb"
            ),
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
                channelDescription = "jhb,hb"
            ),
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
                channelDescription = "jhb,hb"
            ),
        )
    )
}