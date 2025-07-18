package com.faithForward.media.ui.navigation.sidebar

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