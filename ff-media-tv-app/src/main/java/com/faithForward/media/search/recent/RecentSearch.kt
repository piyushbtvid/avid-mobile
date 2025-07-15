package com.faithForward.media.search.recent

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.focusedTextColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecentSearch(
    modifier: Modifier = Modifier,
    list: List<String>,
    lastFocusedIndex: Int,
) {


    Column(
        modifier = modifier.wrapContentWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {


        TitleText(
            text = "Recent Searches",
            color = whiteMain,
            textSize = 15,
            lineHeight = 15,
            fontWeight = FontWeight.Medium
        )

        LazyColumn(
            modifier = Modifier
                .wrapContentWidth()
                .focusRestorer(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            itemsIndexed(list) { index, item ->

                val uiState = when (index) {
                    lastFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                TitleText(modifier = Modifier
                    .onFocusChanged {
                        if (it.hasFocus) {

                        } else {

                        }
                    }
                    .focusable(),
                    text = item,
                    textSize = if (uiState == FocusState.FOCUSED) 18 else 10,
                    lineHeight = 10,
                    color = if (uiState == FocusState.FOCUSED) focusedTextColor else whiteMain
                )

            }

        }

    }
}


@Preview
@Composable
private fun RecentSearchPreview() {

    val list = listOf(
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
        "Betty The Cry",
    )


    RecentSearch(
        list = list,
        lastFocusedIndex = 3
    )

}