package com.faithForward.media.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.SideBarViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
) {
    val sideBarItems = sideBarViewModel.sideBarItems
    val isSideBarFocusable = sideBarViewModel.isSideBarFocusable
    val navController = rememberNavController()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
    ) {
        MainAppNavHost(
            navController = navController,
            onDataLoadedSuccess = {
                sideBarViewModel.changeSideBarFocusState(true)
            }
        )

        SideBar(
            columnList = sideBarItems,
            modifier = Modifier.align(Alignment.TopStart),
            isSideBarFocusable = isSideBarFocusable,
            onSideBarItemClick = { item ->
                if (item.tag == Routes.Creator.route) {
                    navController.navigate(item.tag) {
                        launchSingleTop = true
                    }
                }
                if (item.tag == Routes.Home.route) {
                    navController.navigate(item.tag) {
                        launchSingleTop = true
                    }
                }
            }
        )
    }
}