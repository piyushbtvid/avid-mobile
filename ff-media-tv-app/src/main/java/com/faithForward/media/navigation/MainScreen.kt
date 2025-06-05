package com.faithForward.media.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.faithForward.media.R
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.SideBarViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
    playerViewModel: PlayerViewModel,
    startRoute: String,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
    val sideBarItems = sideBarViewModel.sideBarItems
    val sideBarState by sideBarViewModel.sideBarState

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showSidebar = currentRoute in sidebarVisibleRoutes


    Box(
        modifier = modifier.fillMaxSize()
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
            playerViewModel = playerViewModel,
            loginViewModel = loginViewModel
        )

        AnimatedVisibility(
            visible = startRoute == Routes.Home.route && showSidebar, enter = slideInHorizontally(
                initialOffsetX = { -it }, animationSpec = tween(
                    durationMillis = 1500, easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(durationMillis = 300)
            ), exit = slideOutHorizontally(
                targetOffsetX = { -it }, animationSpec = tween(
                    durationMillis = 1000, easing = FastOutSlowInEasing
                )
            ) + fadeOut(
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            SideBar(columnList = sideBarItems,
                modifier = Modifier.align(Alignment.TopStart),
                isSideBarFocusable = sideBarState.isSideBarFocusable,
                sideBarSelectedPosition = sideBarState.sideBarSelectedPosition,
                sideBarFocusedIndex = sideBarState.sideBarFocusedIndex,
                onSideBarItemClick = { item ->
                    Log.e("SIDE_BAR", "side bar item is $item")
                    when (item.tag) {
                        Routes.Creator.route -> {
                            navController.navigate(Routes.Creator.route) {
                                popUpTo(Routes.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }

                        Routes.Home.route -> {
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }

                        Routes.Movies.route -> {
                            navController.navigate(Routes.Movies.route) {
                                popUpTo(Routes.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }

                        Routes.MyList.route -> {
                            navController.navigate(Routes.MyList.route) {
                                popUpTo(Routes.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }

                        Routes.Series.route -> {
                            navController.navigate(Routes.Series.route) {
                                popUpTo(Routes.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
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
}

val sidebarVisibleRoutes = listOf(
    Routes.Home.route,
    Routes.Movies.route,
    Routes.Creator.route,
    Routes.MyList.route,
    Routes.Series.route
)