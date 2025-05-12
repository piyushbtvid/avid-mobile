package com.faithForward.media.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.HomeContentSections
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomeViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {

    val sideBarItems = homeViewModel.sideBarItems

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
    ) {
        HomeContentSections(
            modifier = Modifier,
            homeViewModel = homeViewModel
        )

        SideBar(
            columnList = sideBarItems,
            modifier = Modifier.align(Alignment.TopStart) // Explicitly align SideBar to TopStart
        )

    }

}