package com.faithForward.media.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.R
import com.faithForward.media.commanComponents.LoaderScreen
import com.faithForward.media.navigation.MainScreen
import com.faithForward.media.navigation.Routes
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.sidebar.SideBarItem
import com.faithForward.media.theme.FfmediaTheme
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var playerViewModel: PlayerViewModel
    private var isControlsVisible: Boolean = true
    private var currentRoute: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FfmediaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val sideBarViewModel: SideBarViewModel = viewModel()
                    val loginViewModel = hiltViewModel<LoginViewModel>()
                    playerViewModel = viewModel()
                    val userSession by loginViewModel.userSession.collectAsStateWithLifecycle()
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    currentRoute = navBackStackEntry?.destination?.route
                    if (userSession.isLoading) {
                        LoaderScreen()
                    } else {
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            sideBarViewModel = sideBarViewModel,
                            loginViewModel = loginViewModel,
                            playerViewModel = playerViewModel,
                            navController = navController,
                            startRoute = if (userSession.data != null) Routes.Home.route else Routes.Login.route
                        )
                    }
                }
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (currentRoute == Routes.PlayerScreen.route) {
            Log.e("PLAY_EVENT", "on use interction is called ")
            playerViewModel.onUserInteraction()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (isControlsVisible) {
                    playerViewModel.togglePlayPause()
                } else {
                    playerViewModel.onUserInteraction()
                }
                return true
            }

            KeyEvent.KEYCODE_MEDIA_REWIND, KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (isControlsVisible) {
                    playerViewModel.handlePlayerAction(PlayerPlayingState.REWINDING)
                } else {
                    playerViewModel.onUserInteraction()
                }
                return true
            }

            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (isControlsVisible) {
                    playerViewModel.handlePlayerAction(PlayerPlayingState.FORWARDING)
                } else {
                    playerViewModel.onUserInteraction()
                }
                return true
            }


            KeyEvent.KEYCODE_DPAD_DOWN -> {
                if (!isControlsVisible) {
                    playerViewModel.onUserInteraction()
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

