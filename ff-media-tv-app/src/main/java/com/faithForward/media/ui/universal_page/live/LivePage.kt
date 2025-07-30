package com.faithForward.media.ui.universal_page.live

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.liveTopBarTextFocusedStyle
import com.faithForward.media.ui.universal_page.top_bar.TopBarItemDto
import com.faithForward.media.ui.universal_page.top_bar.TopBarRow

@Composable
fun LivePage(
    modifier: Modifier = Modifier,
) {

    val topBarList = remember {
        mutableStateOf(
            listOf(
                TopBarItemDto(
                    name = "LIVE", tag = "live"
                ),
                TopBarItemDto(
                    name = "GUIDE", tag = "guide"
                ),
                TopBarItemDto(
                    name = "MY CHANNEL", tag = "my_channel"
                )
            )
        )
    }

    var topBarFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    val focusRequesterList = remember(topBarList.value.size) {
        List(topBarList.value.size) { FocusRequester() }
    }

    LaunchedEffect(Unit) {
        try {
            focusRequesterList[1].requestFocus()
        } catch (_: Exception) {

        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        TopBarRow(
            modifier = Modifier.padding(top = 20.dp),
            focusedIndex = topBarFocusedIndex,
            onFocusedIndexChange = { int ->
                topBarFocusedIndex = int
            },
            topBarItemList = topBarList.value,
            textFocusedStyle = liveTopBarTextFocusedStyle,
            backgroundFocusedColor = Color.Transparent,
            shadowColor = Color.Transparent,
            borderColor = Color.Transparent,
            focusRequesterList = focusRequesterList
        )


    }

}


@Preview
@Composable
private fun LivePagePreview() {
    LivePage()
}