package com.faithForward.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.sideBarShadowColor
import com.faithForward.media.ui.theme.sideBarShadowLightColor


@Composable
fun SideBar(
    modifier: Modifier = Modifier,
    columnList: List<SideBarItem>,
) {
    var sideBarFocusedIndex by remember { mutableStateOf(-1) }
    var sideBarSelectedPosition by remember { mutableStateOf(-1) }

    val animatedWidth by animateDpAsState(
        targetValue = if (sideBarFocusedIndex != -1) 150.dp else 56.dp,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "widthAnimation"
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (sideBarFocusedIndex != -1) 540.dp else 350.dp,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "heightAnimation"
    )

    val outerModifier = if (sideBarFocusedIndex != -1) {
        Modifier
            .width(262.dp)
            .height(540.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        sideBarShadowColor.copy(alpha = 0.9f),
                        sideBarShadowColor.copy(alpha = 0.6f),
                        sideBarShadowColor.copy(alpha = 0.5f),
                        sideBarShadowColor.copy(alpha = 0.4f),
                        sideBarShadowColor.copy(alpha = 0.3f),
                        sideBarShadowColor.copy(alpha = 0.3f),
                        sideBarShadowLightColor.copy(alpha = 0.0f)
                    )
                )
            )
    } else {
        Modifier
            .wrapContentSize()

    }

    Box(
        modifier = outerModifier
    ) {
        Box(
            modifier = modifier
                .width(if (sideBarFocusedIndex != -1) 175.dp else 71.dp)
                .height(if (sideBarFocusedIndex != -1) 574.dp else 384.dp)
                .padding(start = 15.dp, top = 17.dp, bottom = 17.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(animatedHeight)
            ) {
                AnimatedVisibility(
                    visible = sideBarFocusedIndex == -1,
                    enter = fadeIn(animationSpec = tween(800)),
                    exit = fadeOut(animationSpec = tween(800)),
                ) {
                    Image(
                        painter = painterResource(R.drawable.side_bar_unfocused_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                AnimatedVisibility(
                    visible = sideBarFocusedIndex != -1,
                    enter = fadeIn(animationSpec = tween(600)),
                    exit = fadeOut(animationSpec = tween(800)),
                ) {
                    Image(
                        painter = painterResource(R.drawable.side_bar_focused_background),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }



            SideBarColumn(
                columnItems = columnList,
                focusedIndex = sideBarFocusedIndex,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 39.dp),
                selectedPosition = sideBarSelectedPosition,
                onSelectedPositionChange = { index ->
                    sideBarSelectedPosition = index
                },
                onFocusChange = { num ->
                    sideBarFocusedIndex = num
                }
            )
        }
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