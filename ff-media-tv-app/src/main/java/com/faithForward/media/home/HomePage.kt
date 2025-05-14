package com.faithForward.media.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomePageItem
import com.faithForward.media.viewModel.HomeViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    homePageItems: List<HomePageItem>,
    onChangeContentRowFocusedIndex: (Int) -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
    ) {
        HomeContentSections(
            modifier = Modifier,
            homePageItems = homePageItems,
            onChangeContentRowFocusedIndex = { index ->
                onChangeContentRowFocusedIndex.invoke(index)
            }
        )
    }

}