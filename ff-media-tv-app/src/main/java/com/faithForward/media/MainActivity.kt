package com.faithForward.media

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.FfmediaTheme
import com.faithForward.media.ui.theme.unFocusMainColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FfmediaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TestScreen(modifier: Modifier = Modifier) {

    val sideBarTestList = listOf(
        SideBarItem(
            name = "Home",
            img = R.drawable.home_ic,
            tag = "home",
        ),
        SideBarItem(
            name = "Search",
            img = R.drawable.search_ic,
            tag = "search",
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
                .padding(start = 88.dp)
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
                text = "Click Me",
                color = if (isFocused) Color.White else Color.Black
            )
        }

        SideBar(
            columnList = sideBarTestList,
            modifier = Modifier.align(Alignment.TopStart) // Explicitly align SideBar to TopStart
        )
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainPreview() {
    TestScreen()
}
