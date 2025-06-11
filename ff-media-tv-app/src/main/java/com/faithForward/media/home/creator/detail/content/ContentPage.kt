package com.faithForward.media.home.creator.detail.content

import androidx.compose.foundation.border
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.home.creator.detail.ContentCard
import com.faithForward.media.theme.focusedMainColor

data class ContentDto(
    val image: String,
    val title: String,
    val views: String,
    val duration: String,
    val description: String,
    val time: String,
)


@Composable
fun ContentPage(
    modifier: Modifier = Modifier,
    contentDtoList: List<ContentDto>,
) {
    var contentRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .padding(start = 40.dp, top = 20.dp)
            .width(350.dp),
        contentPadding = PaddingValues(end = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TitleText(
                text = "Content",
                color = Color.Black,
                textSize = 20
            )
        }

        itemsIndexed(contentDtoList) { index, item ->
            ContentCard(
                contentDto = item,
                modifier = Modifier
                    .onFocusChanged { focusState ->
                        contentRowFocusedIndex = if (focusState.isFocused) index else -1
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
