package com.faithForward.media.ui.top_bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun TopBarRow(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    onFocusedIndexChange: (Int) -> Unit,
    topBarItemList: List<TopBarItemDto>,
) {


    val focusRequesterList = remember(topBarItemList.size) {
        List(topBarItemList.size) { FocusRequester() }
    }

    var selectedPosition by rememberSaveable { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) {
        try {
            focusRequesterList[1].requestFocus()
        } catch (_: Exception) {

        }
    }

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.width(260.dp),
            painter = painterResource(R.drawable.top_bar_background_img_2),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            itemsIndexed(topBarItemList) { index, item ->

                val uiState = when (index) {
                    focusedIndex -> FocusState.FOCUSED
                    selectedPosition -> FocusState.SELECTED
                    else -> FocusState.UNFOCUSED
                }


                TopBarItem(
                    modifier = Modifier
                        .focusRequester(focusRequesterList[index])
                        .onFocusChanged {
                            if (it.hasFocus) {
                                onFocusedIndexChange.invoke(index)
                            } else {
                                onFocusedIndexChange.invoke(-1)
                            }
                        },
                    topBarItemDto = item,
                    focusState = uiState,
                    backgroundUnFocusedColor = Color.Transparent,
                    backgroundFocusedColor = whiteMain.copy(alpha = 0.55f),
                    onCategoryItemClick = {

                    }
                )


            }


        }

    }


}


@Preview
@Composable
private fun TopBarPreview() {

    val ls = listOf(
        TopBarItemDto(
            name = "STREAM", tag = "live"
        ),
        TopBarItemDto(
            name = "LIVE", tag = "live"
        ),
        TopBarItemDto(
            name = "BROWSE", tag = "live"
        )
    )

    TopBarRow(
        topBarItemList = ls,
        focusedIndex = 2,
        onFocusedIndexChange = {

        }
    )

}