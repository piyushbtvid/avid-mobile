package com.faithForward.media.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.SideBarViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
) {
    val sideBarItems = sideBarViewModel.sideBarItems
    val sideBarState by sideBarViewModel.sideBarState
    val navController = rememberNavController()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
    ) {
        MainAppNavHost(navController = navController,
            onDataLoadedSuccess = {
                sideBarViewModel.onEvent(SideBarEvent.ChangeFocusState(true))
            }, changeSideBarSelectedPosition = { value ->
                sideBarViewModel.onEvent(SideBarEvent.ChangeSelectedIndex(value))
            })

        SideBar(
            columnList = sideBarItems,
            modifier = Modifier.align(Alignment.TopStart),
            isSideBarFocusable = sideBarState.isSideBarFocusable,
            sideBarSelectedPosition = sideBarState.sideBarSelectedPosition,
            sideBarFocusedIndex = sideBarState.sideBarFocusedIndex,
            onSideBarItemClick = { item ->
                if (item.tag == Routes.Creator.route) {
                    navController.navigate(item.tag) {
                        popUpTo(Routes.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
                if (item.tag == Routes.Home.route) {
                    navController.navigate(item.tag) {
                        popUpTo(Routes.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            onSideBarSelectedPositionChange = { index ->
                sideBarViewModel.onEvent(SideBarEvent.ChangeSelectedIndex(index))
            },
            onSideBarFocusedIndexChange = { index ->
                sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(index))
            })
    }
}