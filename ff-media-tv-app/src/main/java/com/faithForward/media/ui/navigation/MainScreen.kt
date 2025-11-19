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
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.faithForward.media.ui.navigation.bar.AppNavigationBar
import com.faithForward.media.ui.navigation.bottombar.BottomNavBar
import com.faithForward.media.ui.navigation.sidebar.SideBar
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.util.Util.isTvDevice
import com.faithForward.media.viewModel.LoginViewModel
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
) {
    val sideBarItems = sideBarViewModel.sideBarItems
    val sideBarState by sideBarViewModel.sideBarState

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isTv = LocalContext.current.isTvDevice()

    // Delay showing sidebar when navigating from AllProfile to Home to prevent LayoutNode attachment issues
    // On mobile, need longer delay due to different rendering behavior
    var shouldShowSidebar by remember { mutableStateOf(false) }
    val routeInSidebarList = currentRoute in sidebarVisibleRoutes
    
    LaunchedEffect(currentRoute) {
        if (routeInSidebarList) {
            // Longer delay on mobile to ensure navigation transition and composition complete
            // Mobile needs more time due to different rendering pipeline
            if (isTv){
                shouldShowSidebar = true
            }
        } else {
            // Hide immediately when route changes away from sidebar routes
            shouldShowSidebar = false
        }
    }
    
    val showSidebar = shouldShowSidebar && routeInSidebarList

    var showExitDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val activity = (LocalActivity.current)

    val scope = rememberCoroutineScope()


    // Collect one-time event safely for logout success
    LaunchedEffect(Unit) {
        sideBarViewModel.logoutEvent.collect {
            Log.e("LOGOUT_COLLECT", "on logout event recived in main screen ")
            showLogoutDialog = false
            loginViewModel.cancelRefreshJob()
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
            onBackClickForExit = {
                if (!isTv) {
                    // Mobile: pop back to Home if present; otherwise navigate to Home. Exit if already on Home.
                    val destRoute = navController.currentDestination?.route
                    if (destRoute == Routes.Home.route) {
                        showExitDialog = true
                    } else {
                        val popped = try {
                            navController.popBackStack(Routes.Home.route, inclusive = false)
                        } catch (t: Throwable) {
                            false
                        }
                        if (!popped) {
                            // Replace current destination with Home to avoid a two-back sequence
                            navController.navigate(Routes.Home.route) {
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                } else {
                    // TV behavior unchanged: always show exit dialog on back from a root tab
                    showExitDialog = true
                }
            },
            onLogoutRequest = { showLogoutDialog = true }
        )
        AppNavigationBar(
            navController = navController,
            sideBarViewModel = sideBarViewModel,
            sideBarItems = sideBarItems,
            sideBarState = sideBarState,
            onLogoutRequest = { showLogoutDialog = true },
            showSidebar = showSidebar
        )
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
    Routes.Search.route,
    Routes.MyAccount.route
)