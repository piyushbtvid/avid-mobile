package com.faithForward.media.navigation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.R
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.theme.homeBackgroundColor
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.SideBarViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
    startRoute: String,
    loginViewModel: LoginViewModel
) {
    val sideBarItems = sideBarViewModel.sideBarItems
    val sideBarState by sideBarViewModel.sideBarState
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showSidebar = currentRoute in sidebarVisibleRoutes


    Box(
        modifier = modifier
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(R.drawable.background_blur__1_),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        MainAppNavHost(
            navController = navController,
            onDataLoadedSuccess = {
                sideBarViewModel.onEvent(SideBarEvent.ChangeFocusState(true))
            },
            changeSideBarSelectedPosition = { value ->
                sideBarViewModel.onEvent(SideBarEvent.ChangeSelectedIndex(value))
            },
            startRoute = startRoute,
            loginViewModel = loginViewModel
        )

        if (startRoute == Routes.Home.route && showSidebar) {
            SideBar(
                columnList = sideBarItems,
                modifier = Modifier.align(Alignment.TopStart),
                isSideBarFocusable = sideBarState.isSideBarFocusable,
                sideBarSelectedPosition = sideBarState.sideBarSelectedPosition,
                sideBarFocusedIndex = sideBarState.sideBarFocusedIndex,
                onSideBarItemClick = { item ->
                    Log.e("SIDE_BAR", "side bar item is $item")
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

                    if (item.tag == Routes.Movies.route) {
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
                }
            )
        }
    }
}

val sidebarVisibleRoutes = listOf(
    Routes.Home.route,
    Routes.Movies.route,
    Routes.Creator.route
)