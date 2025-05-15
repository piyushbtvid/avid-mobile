package com.faithForward.media.home.creator.list

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 136.dp),
        modifier = modifier.heightIn(max = 600.dp),
        contentPadding = PaddingValues(top = 47.dp, bottom = 50.dp, start = 63.dp, end = 70.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(space = 10.dp, Alignment.CenterVertically)
    ) {
        itemsIndexed(creators) { index: Int, creator: CreatorCardDto ->
            CreatorCard(
                creatorCardDto = creator, isFocused = index == focusedIndex,
                modifier = Modifier
                    .focusRestorer()
                    .onFocusChanged {
                        if (it.isFocused) {
                            focusedIndex = index
                        } else {
                            if (focusedIndex == index) {
                                focusedIndex = -1
                            }
                        }
                    }
                    .focusable(),
            )
        }
    }
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
            ),
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
            ),
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
            ),
            CreatorCardDto(
                creatorSubscriberText = "130K Subscribers",
                creatorImageUrl = "",
                creatorName = "Jasmine Wright",
            ),
        )
    )
}