package com.faithForward.media.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.theme.pageBlackBackgroundColor
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


    LaunchedEffect(startRoute) {
        Log.e("IS_LOGIN", "start route in mainScreen is $startRoute")
    }

    LaunchedEffect(currentRoute) {
        Log.e("CURRENT_ROUTE", "CURRENT ROUTE in mainScreen is $currentRoute")
    }

    LaunchedEffect(showSidebar) {
        Log.e("CURRENT_ROUTE", "Show Side Bar  in mainScreen is $showSidebar")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor)
    ) {
//        Image(
//            painter = painterResource(R.drawable.background_blur__1_),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize()
//        )
        MainAppNavHost(
            navController = navController,
            onDataLoadedSuccess = {
                sideBarViewModel.onEvent(SideBarEvent.ChangeFocusState(true))
            },
            changeSideBarSelectedPosition = { value ->
                sideBarViewModel.onEvent(SideBarEvent.ChangeSelectedIndex(value))
            },
            startRoute = startRoute,
            sideBarViewModel = sideBarViewModel,
            playerViewModel = playerViewModel,
            loginViewModel = loginViewModel
        )

        AnimatedVisibility(
            visible = showSidebar, enter = slideInHorizontally(
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
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        Routes.Home.route -> {
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Home.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        Routes.Movies.route -> {
                            navController.navigate(Routes.Movies.createRoute("movies")) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        Routes.MyList.route -> {
                            navController.navigate(Routes.MyList.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        Routes.Series.route -> {
                            navController.navigate(Routes.Series.createRoute("series")) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        Routes.Search.route -> {
                            navController.navigate(Routes.Search.route) {
                                popUpTo(0) { inclusive = true }
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
    Routes.Series.route,
    Routes.Search.route
)