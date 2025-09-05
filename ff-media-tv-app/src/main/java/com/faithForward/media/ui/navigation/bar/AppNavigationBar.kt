package com.faithForward.media.ui.navigation.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.faithForward.media.ui.navigation.bottombar.BottomNavBar
import com.faithForward.media.ui.navigation.sidebar.SideBar
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.navigation.sidebar.SideBarItem
import com.faithForward.media.ui.navigation.sidebar.SideBarState
import com.faithForward.media.util.Util.isTvDevice
import com.faithForward.media.viewModel.SideBarViewModel

@Composable
fun AppNavigationBar(
    navController: NavController,
    sideBarItems: List<SideBarItem>,
    sideBarState: SideBarState,
    sideBarViewModel: SideBarViewModel,
    showSidebar: Boolean,
    onLogoutRequest: () -> Unit,
) {
    val isTv = LocalContext.current.isTvDevice()

    Box(modifier = Modifier.fillMaxSize()) {
        if (isTv) {
            AnimatedVisibility(
                visible = showSidebar,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(durationMillis = 300)),
                modifier = Modifier.align(Alignment.CenterStart) // ✅ inside
            ) {
                SideBar(
                    columnList = sideBarItems,
                    isSideBarFocusable = sideBarState.isSideBarFocusable,
                    sideBarSelectedPosition = sideBarState.sideBarSelectedPosition,
                    sideBarFocusedIndex = sideBarState.sideBarFocusedIndex,
                    onSideBarItemClick = { item ->
                        handleNavItemClick(item, navController, sideBarViewModel, onLogoutRequest)
                    },
                    onSideBarSelectedPositionChange = { index ->
                        sideBarViewModel.onEvent(SideBarEvent.ChangeSelectedIndex(index))
                    },
                    onSideBarFocusedIndexChange = { index ->
                        sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(index))
                    }
                )
            }
        } else {
            if (showSidebar){
                BottomNavBar(
                    modifier = Modifier.align(Alignment.BottomCenter), // ✅ inside
                    navItems = sideBarItems,
                    selectedPosition = sideBarState.sideBarSelectedPosition,
                    onSelectedPositionChange = { index ->
                        sideBarViewModel.onEvent(SideBarEvent.ChangeSelectedIndex(index))
                    },
                    onItemClick = { item ->
                        handleNavItemClick(item, navController, sideBarViewModel, onLogoutRequest)
                    }
                )
            }
        }
    }
}
