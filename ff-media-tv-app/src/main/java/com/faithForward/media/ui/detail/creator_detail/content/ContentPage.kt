package com.faithForward.media.ui.detail.creator_detail.content

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.detail.creator_detail.ContentCard
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain

data class ContentDto(
    val slug: String?,
    val image: String,
    val title: String,
    val views: String,
    val duration: String,
    val description: String,
    val time: String,
)


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ContentPage(
    modifier: Modifier = Modifier,
    contentDtoList: List<ContentDto>,
    onCreatorContentClick: (ContentDto) -> Unit,
    contentRowFocusedIndex: Int,
    onLastRowFocusedIndexChange: (Int) -> Unit,
    onContentRowFocusChange: (Int) -> Unit,
    focusRequesters: List<FocusRequester>,
) {
    LazyColumn(
        modifier = modifier
            .focusRestorer {
                focusRequesters[0]
            }
            .fillMaxHeight()
            .padding(start = 40.dp, top = 20.dp)
            .width(350.dp),
        contentPadding = PaddingValues(end = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TitleText(
                text = "Content",
                color = whiteMain,
                textSize = 20
            )
        }

        itemsIndexed(contentDtoList) { index, item ->
            val focusRequester = focusRequesters.getOrNull(index) ?: FocusRequester()

            ContentCard(
                contentDto = item,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onContentRowFocusChange(index)
                            onLastRowFocusedIndexChange(index)
                        } else {
                            onContentRowFocusChange(-1)
                        }
                    }
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {
                        onCreatorContentClick.invoke(item)
                    }
                    .focusable()
                    .border(
                        width = if (contentRowFocusedIndex == index) 2.dp else 0.dp,
                        color = if (contentRowFocusedIndex == index) focusedMainColor else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 2.dp)
            )
        }
    }
}
