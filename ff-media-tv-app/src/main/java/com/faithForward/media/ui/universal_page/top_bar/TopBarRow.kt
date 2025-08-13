package com.faithForward.media.ui.universal_page.top_bar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.sideBarFocusedTextColor
import com.faithForward.media.ui.theme.topBarTextFocusedStyle
import com.faithForward.media.ui.theme.topBarTextUnFocusedStyle
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun TopBarRow(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    selectedPosition: Int,
    onFocusedIndexChange: (Int) -> Unit,
    topBarItemList: List<TopBarItemDto>,
    backgroundUnFocusedColor: Color = Color.Transparent,
    backgroundFocusedColor: Color = Color.Transparent,
    shadowColor: Color = focusedMainColor.copy(0.55f),
    borderColor: Color = focusedMainColor,
    textFocusedStyle: TextStyle = topBarTextFocusedStyle,
    textUnFocusedStyle: TextStyle = topBarTextUnFocusedStyle,
    selectedTextColor: Color,
    focusRequesterList: List<FocusRequester>,
    onTopBarUpClick: () -> Unit,
    onItemClick: (TopBarItemDto) -> Unit,
    onTopBarLeftClick: () -> Unit,
    onSelectedPositionClick: (Int) -> Unit,
    onTopBarDownClick: () -> Boolean = {
        false
    },
) {


    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {

//        Image(
//            modifier = Modifier.width(260.dp),
//            painter = painterResource(R.drawable.top_bar_background_img_2),
//            contentScale = ContentScale.FillWidth,
//            contentDescription = null
//        )

        TopBarBackGround()

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
                        }
                        .focusable()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionUp) {
                                Log.e("ON_LEFT", "on up click")
                                onTopBarUpClick.invoke()
                                true
                            } else if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionLeft) {
                                if (index == 0) {
                                    onTopBarLeftClick.invoke()
                                    true
                                } else {
                                    false
                                }
                            } else if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionDown) {
                                onTopBarDownClick.invoke()
                            } else {
                                false
                            }
                        },
                    topBarItemDto = item,
                    focusState = uiState,
                    backgroundUnFocusedColor = backgroundUnFocusedColor,
                    backgroundFocusedColor = backgroundFocusedColor,
                    onCategoryItemClick = {
                        onItemClick.invoke(item)
                        onSelectedPositionClick.invoke(index)
                    },
                    shadowColor = shadowColor,
                    borderColor = borderColor,
                    textFocusedStyle = textFocusedStyle,
                    textUnFocusedStyle = textUnFocusedStyle,
                    selectedTextColor = selectedTextColor
                )


            }


        }

    }
}

@Composable
fun TopBarBackGround(
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .width(280.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(50.dp)) // Clip the shape first
            .border(
                width = 0.7.dp,
                color = sideBarFocusedTextColor,
                shape = RoundedCornerShape(50.dp)
            )
            .background(color = whiteMain.copy(alpha = 0.55f)) // Background comes after border
    )

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
        selectedPosition = 1,
        focusedIndex = 0,
        onFocusedIndexChange = {

        },
        backgroundUnFocusedColor = Color.Transparent,
        backgroundFocusedColor = whiteMain.copy(alpha = 0.55f),
        focusRequesterList = emptyList(),
        onItemClick = {

        },
        onTopBarUpClick = {},
        onSelectedPositionClick = {

        },
        selectedTextColor = focusedMainColor,
        onTopBarLeftClick = {}
    )
}

@Preview
@Composable
private fun TopBarImagePreview() {
    TopBarBackGround()
}