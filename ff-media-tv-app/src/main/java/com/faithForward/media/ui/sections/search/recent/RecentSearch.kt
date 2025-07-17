package com.faithForward.media.ui.sections.search.recent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedTextColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecentSearch(
    modifier: Modifier = Modifier,
    list: List<String>,
    lastFocusedIndex: Int,
    onFocusedIndexChange: (Int) -> Unit,
    onItemClick: (String) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }

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
                .focusRestorer {
                    focusRequester
                },
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {

            itemsIndexed(list) { index, item ->

                val uiState = when (index) {
                    lastFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                val scale by animateFloatAsState(
                    targetValue = if (uiState == FocusState.FOCUSED) 1.4f else 1f,
                    animationSpec = tween(durationMillis = 200),
                    label = "focus-scale"
                )


                Box(
                    modifier = Modifier
                        .height(20.dp) // enough to accommodate the largest text size
                ) {
                    TitleText(
                        modifier = Modifier
                            .focusRequester(if (index == 0) focusRequester else FocusRequester())
                            .onFocusChanged {
                                if (it.hasFocus) {
                                    onFocusedIndexChange.invoke(index)
                                } else {
                                    onFocusedIndexChange.invoke(-1)
                                }
                            }
                            .clickable(interactionSource = null, indication = null, onClick = {
                                onItemClick.invoke(item)
                            })
                            .focusable(),
                        text = item,
                        textSize = if (uiState == FocusState.FOCUSED) 20 else 10,
                        lineHeight = 10,
                        color = if (uiState == FocusState.FOCUSED) focusedTextColor else whiteMain
                    )
                }


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
        lastFocusedIndex = 3,
        onFocusedIndexChange = {

        },
        onItemClick = {

        }
    )

}