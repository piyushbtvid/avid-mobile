package com.faithForward.media

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//@Composable
// fun SideBarContent(
//    modifier: Modifier,
//    columnItems: List<SideBarItem>,
//    sideBarFocusedIndex: Int,
//    onFocusChange: (Int) -> Unit,
//    animatedWidth: Dp,
//    animatedHeight: Dp
//) {
//    Box(
//        modifier = modifier
//            .width(if (sideBarFocusedIndex != -1) 175.dp else 71.dp)
//            .height(if (sideBarFocusedIndex != -1) 574.dp else 384.dp)
//            .padding(start = 15.dp, top = 17.dp, bottom = 17.dp),
//        contentAlignment = Alignment.TopStart
//    ) {
//        Crossfade(
//            targetState = sideBarFocusedIndex != -1,
//            animationSpec = tween(durationMillis = 300),
//            label = "imageCrossfade"
//        ) { isFocused ->
//            Image(
//                painter = painterResource(
//                    if (isFocused) R.drawable.side_bar_focused_background
//                    else R.drawable.side_bar_unfocused_background
//                ),
//                contentDescription = "",
//                modifier = Modifier
//                    .width(animatedWidth)
//                    .height(animatedHeight),
//                contentScale = ContentScale.FillBounds
//            )
//        }
//
//        SideBarColumn(
//            columnItems = columnItems,
//            focusedIndex = sideBarFocusedIndex,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 40.dp),
//            onFocusChange = onFocusChange
//        )
//    }
//}