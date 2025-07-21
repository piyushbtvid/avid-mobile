package com.faithForward.media.ui.navigation

import android.util.Log
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.faithForward.media.ui.navigation.sidebar.SideBar
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.RefreshViewModel
import com.faithForward.media.viewModel.SharedPlayerViewModel
import com.faithForward.media.viewModel.SideBarViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
    playerViewModel: SharedPlayerViewModel,
    startRoute: String,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    refreshViewModel: RefreshViewModel,
) {
    val sideBarItems = sideBarViewModel.sideBarItems
    val sideBarState by sideBarViewModel.sideBarState

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showSidebar = currentRoute in sidebarVisibleRoutes

    var showExitDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val activity = (LocalActivity.current)

    val scope = rememberCoroutineScope()


    // Collect one-time event safely for logout success
    LaunchedEffect(Unit) {
        sideBarViewModel.logoutEvent.collect {
            Log.e("LOGOUT_COLLECT", "on logout event recived in main screen ")
            showLogoutDialog = false
            refreshViewModel.cancelRefreshJob()
            navController.navigate(Routes.LoginQr.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
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
            sharedPlayerViewModel = playerViewModel,
            loginViewModel = loginViewModel,
            refreshViewModel = refreshViewModel,
            onBackClickForExit = {
                showExitDialog = true
            }
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

                        "log_out" -> {
                            showLogoutDialog = true
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

        ExitDialog(
            showDialog = showExitDialog,
            onDismiss = {
                showExitDialog = false
            },
            onExitConfirm = {
                showExitDialog = false
                activity?.finish()
            },
        )

        LogoutDialog(
            showDialog = showLogoutDialog,
            onDismiss = {
                showLogoutDialog = false
            },
            onLogoutConfirm = {
                sideBarViewModel.onEvent(SideBarEvent.LogoutClick)
            }
        )
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