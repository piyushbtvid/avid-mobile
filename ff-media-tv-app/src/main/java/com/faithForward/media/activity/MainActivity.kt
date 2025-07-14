package com.faithForward.media.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.R
import com.faithForward.media.navigation.MainScreen
import com.faithForward.media.navigation.Routes
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.sidebar.SideBarItem
import com.faithForward.media.theme.FfmediaTheme
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.SharedPlayerViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import com.faithForward.media.viewModel.uiModels.SharedPlayerEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var sharedPlayerViewModel: SharedPlayerViewModel
    private var isControlsVisible: Boolean = true
    private var currentRoute: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FfmediaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val sideBarViewModel: SideBarViewModel = hiltViewModel()
                    val loginViewModel: LoginViewModel = hiltViewModel()
                    sharedPlayerViewModel = viewModel()
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    currentRoute = navBackStackEntry?.destination?.route
                    isControlsVisible =
                        sharedPlayerViewModel.state.collectAsState().value.isControlsVisible
                    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
                    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()

                    LaunchedEffect(loginState.isCheckingLoginStatus, isLoggedIn) {
                        Log.e("IS_LOGIN", "is login status in mainActivity is $isLoggedIn")
                    }

                    // Use Crossfade to animate between loading and MainScreen
                    Crossfade(
                        targetState = loginState.isCheckingLoginStatus,
                        modifier = Modifier.padding(innerPadding),
                        animationSpec = tween(durationMillis = 100) // Adjust duration as needed
                    ) { isChecking ->
                        if (isChecking) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(pageBlackBackgroundColor),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = focusedMainColor
                                )
                            }
                        } else {
                            MainScreen(
                                modifier = Modifier.fillMaxSize(),
                                sideBarViewModel = sideBarViewModel,
                                loginViewModel = loginViewModel,
                                playerViewModel = sharedPlayerViewModel,
                                navController = navController,
                                startRoute = if (isLoggedIn) Routes.Home.route else Routes.LoginQr.route
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (currentRoute == Routes.PlayerScreen.route && !isControlsVisible) {
            Log.e(
                "ON_USER_INTERCATION",
                "on user intercation is called if current route is player with $isControlsVisible"
            )
            sharedPlayerViewModel.onUserInteraction("onUSerInterction")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.e("Logging", "onKeyDown()")

        // ✅ Show controls for ANY key press on PlayerScreen if they're not already visible
        if (currentRoute == Routes.PlayerScreen.route && !isControlsVisible) {
            sharedPlayerViewModel.handleEvent(SharedPlayerEvent.ShowControls)
        }

        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (currentRoute == Routes.PlayerScreen.route) {
                    Log.e("CONTROLES", "isControlers visible is $isControlsVisible")
                    if (isControlsVisible) {
                        sharedPlayerViewModel.togglePlayPause()
                    }
                }
                return true
            }

            KeyEvent.KEYCODE_MEDIA_REWIND, KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (currentRoute == Routes.PlayerScreen.route) {
                    if (isControlsVisible) {
                        sharedPlayerViewModel.handlePlayerAction(PlayerPlayingState.REWINDING)
                    }
                }
                return true
            }

            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (currentRoute == Routes.PlayerScreen.route) {
                    if (isControlsVisible) {
                        sharedPlayerViewModel.handlePlayerAction(PlayerPlayingState.FORWARDING)
                    }
                }
                return true
            }

            KeyEvent.KEYCODE_DPAD_DOWN -> {
                Log.e("DPAD", "on dpad down called in main")
                return true
            }

            KeyEvent.KEYCODE_DPAD_UP -> {
                Log.e("DPAD", "on dpad up called in main")
                if (!isControlsVisible && currentRoute == Routes.PlayerScreen.route) {
                    sharedPlayerViewModel.handleEvent(SharedPlayerEvent.HideControls)
                }
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

}


@Composable
fun TestScreen(modifier: Modifier = Modifier) {

    val sideBarTestList = listOf(
        SideBarItem(
            name = "Search",
            img = R.drawable.search_ic,
            tag = "search",
        ),
        SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        ),
        SideBarItem(
            name = "MyList",
            img = R.drawable.plus_ic,
            tag = "myList",
        ),
        SideBarItem(
            name = "Creators",
            img = R.drawable.group_person_ic,
            tag = "creators",
        ),
        SideBarItem(
            name = "Series",
            img = R.drawable.screen_ic,
            tag = "series",
        ),
        SideBarItem(
            name = "Movies",
            img = R.drawable.film_ic,
            tag = "movie",
        ),
        SideBarItem(
            name = "Tithe",
            img = R.drawable.fi_rs_hand_holding_heart,
            tag = "tithe",
        ),

        )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
        contentAlignment = Alignment.TopStart
    ) {
        var isFocused by remember { mutableStateOf(false) }

        Image(
            painter = painterResource(R.drawable.banner_test_img),
            modifier = Modifier
                .padding(start = 176.dp)
                .width(945.dp)
                .height(541.dp),
            contentDescription = null,
            alignment = Alignment.TopCenter
        )

        Button(
            onClick = { /* TODO: Handle button click */ },
            modifier = Modifier
                .align(Alignment.Center)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFocused) Color.Red else Color.LightGray
            )
        ) {
            Text(
                text = "Click Me", color = if (isFocused) Color.White else Color.Black
            )
        }

        SideBar(columnList = sideBarTestList,
            modifier = Modifier.align(Alignment.TopStart),
            isSideBarFocusable = true,
            sideBarFocusedIndex = 1,
            sideBarSelectedPosition = 1,
            onSideBarSelectedPositionChange = {

            },
            onSideBarFocusedIndexChange = {

            },
            onSideBarItemClick = {

            })
    }
}

