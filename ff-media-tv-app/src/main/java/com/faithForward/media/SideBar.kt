package com.faithForward.media

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SideBar(
    modifier: Modifier = Modifier,
    columnList: List<SideBarItem>,
) {
    var sideBarFocusedIndex by remember { mutableStateOf(2) }

    val animatedWidth by animateDpAsState(
        targetValue = if (sideBarFocusedIndex != -1) 150.dp else 56.dp,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "widthAnimation"
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (sideBarFocusedIndex != -1) 540.dp else 350.dp,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = "heightAnimation"
    )

    Box(
        modifier = modifier
            .width(if (sideBarFocusedIndex != -1) 175.dp else 71.dp)
            .height(if (sideBarFocusedIndex != -1) 574.dp else 384.dp)
            .padding(start = 15.dp, top = 17.dp, bottom = 17.dp),
        contentAlignment = Alignment.TopStart // Change to TopStart to align content correctly
    ) {
        Crossfade(
            targetState = sideBarFocusedIndex != -1,
            animationSpec = tween(durationMillis = 300),
            label = "imageCrossfade"
        ) { isFocused ->
            Image(
                painter = painterResource(
                    if (isFocused) R.drawable.side_bar_focused_background
                    else R.drawable.side_bar_unfocused_background
                ),
                contentDescription = "",
                modifier = Modifier
                    .width(animatedWidth)
                    .height(animatedHeight),
                contentScale = ContentScale.FillBounds
            )
        }

        SideBarColumn(
            columnItems = columnList,
            focusedIndex = sideBarFocusedIndex,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 40.dp),
            onFocusChange = { num ->
                sideBarFocusedIndex = num
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SideBarPreview() {

    val sideBarTestList = listOf(
        SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        ), SideBarItem(
            name = "Search",
            img = R.drawable.search_ic,
            tag = "search",
        ), SideBarItem(
            name = "MyList",
            img = R.drawable.plus_ic,
            tag = "myList",
        ), SideBarItem(
            name = "Creators",
            img = R.drawable.group_person_ic,
            tag = "creators",
        ), SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart
    ) {
        SideBar(
            columnList = sideBarTestList,
        )
    }
}