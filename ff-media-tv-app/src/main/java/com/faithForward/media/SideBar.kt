package com.faithForward.media

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SideBar(
    modifier: Modifier = Modifier,
    focusedItemIndex: Int,
    rowList: List<SideBarItem>
) {


    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {


        AnimatedVisibility(
            visible = focusedItemIndex != -1,
            enter = slideInHorizontally(initialOffsetX = { -it }), // Slide in from left
            exit = slideOutHorizontally(targetOffsetX = { -it }) // Slide out to right
        ) {
            Image(
                painter = painterResource(R.drawable.side_bar_focused_background),
                contentDescription = "",
                modifier = Modifier
                    .width(122.dp)
                    .height(506.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        AnimatedVisibility(
            visible = focusedItemIndex == -1,
            enter = slideInHorizontally(initialOffsetX = { -it }), // Slide in from left
            exit = slideOutHorizontally(targetOffsetX = { -it }) // Slide out to right
        ) {
            Image(
                painter = painterResource(R.drawable.side_bar_unfocused_background),
                contentDescription = "",
                modifier = Modifier
                    .width(56.dp)
                    .height(313.dp),
                contentScale = ContentScale.FillBounds
            )
        }


        SideBarColumn(
            rowItems = rowList,
            focusedIndex = focusedItemIndex,
            modifier = Modifier
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
        ),
        SideBarItem(
            name = "Search",
            img = R.drawable.search_ic,
            tag = "search",
        ),
        SideBarItem(
            name = "MyList",
            img = R.drawable.plus_ic,
            tag = "myList",
        ),
        SideBarItem(
            name = "Creators",
            img = R.drawable.group_person_ic,
            tag = "creators",
        ),
        SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        SideBar(
            focusedItemIndex = 2 ,
            rowList = sideBarTestList,
        )
    }
}