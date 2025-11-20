package com.faithForward.media.ui.navigation.bar

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.faithForward.media.ui.navigation.Routes
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.navigation.sidebar.SideBarItem
import com.faithForward.media.viewModel.SideBarViewModel

fun handleNavItemClick(
    item: SideBarItem,
    navController: NavController,
    sideBarViewModel: SideBarViewModel,
    onLogoutRequest: () -> Unit,
) {
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
        Routes.MyAccount.route -> {
            // only Sidebar needs focus handling
            sideBarViewModel.onEvent(SideBarEvent.ChangeFocusState(false))
            navController.navigate(Routes.MyAccount.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
        "log_out" -> {
            onLogoutRequest()
        }
    }
}
