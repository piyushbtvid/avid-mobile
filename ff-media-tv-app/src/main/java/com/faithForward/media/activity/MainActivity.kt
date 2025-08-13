package com.faithForward.media.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
import com.faithForward.media.ui.epg.ChannelWithProgramsUiModel
import com.faithForward.media.ui.epg.Epg
import com.faithForward.media.ui.epg.EpgUiModel
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramUiModel
import com.faithForward.media.ui.epg.util.generateSampleEpgUiModel
import com.faithForward.media.ui.navigation.MainScreen
import com.faithForward.media.ui.navigation.Routes
import com.faithForward.media.ui.navigation.sidebar.SideBar
import com.faithForward.media.ui.navigation.sidebar.SideBarItem
import com.faithForward.media.ui.theme.FfmediaTheme
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.SharedPlayerViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import com.faithForward.media.viewModel.uiModels.SharedPlayerEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var sharedPlayerViewModel: SharedPlayerViewModel
    private var isControlsVisible: Boolean = false
    private var currentRoute: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FfmediaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val loginViewModel: LoginViewModel = hiltViewModel()
                    val sideBarViewModel: SideBarViewModel = hiltViewModel()
                    sharedPlayerViewModel = viewModel()
                    val isLoading = loginViewModel.isBuffer.collectAsStateWithLifecycle().value
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    currentRoute = navBackStackEntry?.destination?.route
                    isControlsVisible =
                        sharedPlayerViewModel.state.collectAsStateWithLifecycle().value.isControlsVisible

                    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()

                    LaunchedEffect(isLoading) {
                        Log.e("LOADING", "is loading change in main is $isLoading")
                    }

                    // Use CrossFade to animate between loading and MainScreen
                    Crossfade(
                        targetState = isLoading,
                        modifier = Modifier.padding(innerPadding),
                        animationSpec = tween(durationMillis = 100), label = ""
                    ) { loading ->
                        when {
                            loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(pageBlackBackgroundColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = focusedMainColor)
                                }
                            }

                            else -> {
                                Epg(
                                    modifier = Modifier.fillMaxSize().background(color = Color.LightGray),
                                    epgUiModel = generateSampleEpgUiModel(),
                                )
                            }
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

        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (currentRoute == Routes.PlayerScreen.route) {
                    Log.e("CONTROLES", "isControlers visible is $isControlsVisible")
                    sharedPlayerViewModel.togglePlayPause()
                }
                return true
            }

            KeyEvent.KEYCODE_MEDIA_REWIND, KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (currentRoute == Routes.PlayerScreen.route) {
                    sharedPlayerViewModel.handlePlayerAction(PlayerPlayingState.REWINDING)
                }
                return true
            }

            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (currentRoute == Routes.PlayerScreen.route) {
                    sharedPlayerViewModel.handlePlayerAction(PlayerPlayingState.FORWARDING)
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

    override fun onStart() {
        super.onStart()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    override fun onStop() {
        super.onStop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

