package com.faithForward.media.home.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faithForward.media.home.HomePage
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomePageItem

@Composable
fun CreatorScreen(
    modifier: Modifier = Modifier,
    homePageItems: List<HomePageItem>,
) {
    HomePage(
        modifier = modifier,
        homePageItems = homePageItems,
        onChangeContentRowFocusedIndex = {  }
    )

}