package com.faithForward.media.ui.universal_page.live

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.RoundedIconButton
import com.faithForward.media.ui.theme.liveTopBarTextFocusedStyle
import com.faithForward.media.ui.theme.textFocusedMainColor
import com.faithForward.media.ui.universal_page.live.navigation.LiveNavGraph
import com.faithForward.media.ui.universal_page.live.navigation.LiveRoutes
import com.faithForward.media.ui.universal_page.top_bar.TopBarItemDto
import com.faithForward.media.ui.universal_page.top_bar.TopBarRow
import com.faithForward.media.util.extensions.shadow

@Composable
fun LiveMainPage(
    modifier: Modifier = Modifier,
) {

    val topBarList = remember {
        mutableStateOf(
            listOf(
                TopBarItemDto(
                    name = "LIVE", tag = "live"
                ), TopBarItemDto(
                    name = "GUIDE", tag = "guide"
                ), TopBarItemDto(
                    name = "MY CHANNEL", tag = "my_channel"
                )
            )
        )
    }

    val navController = rememberNavController()

    var topBarFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var selectedPosition by rememberSaveable { mutableIntStateOf(0) }

    var isMicFocused by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    val focusRequesterList = remember(topBarList.value.size) {
        List(topBarList.value.size) { FocusRequester() }
    }

    LaunchedEffect(Unit) {
        try {
            focusRequesterList[0].requestFocus()
        } catch (_: Exception) {

        }
    }

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter
    ) {

        LiveNavGraph(
            navController = navController
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(33.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TopBarRow(focusedIndex = topBarFocusedIndex,
                selectedPosition = selectedPosition,
                onFocusedIndexChange = { int ->
                    topBarFocusedIndex = int
                },
                topBarItemList = topBarList.value,
                textFocusedStyle = liveTopBarTextFocusedStyle,
                backgroundFocusedColor = Color.Transparent,
                shadowColor = Color.Transparent,
                borderColor = Color.Transparent,
                focusRequesterList = focusRequesterList,
                onItemClick = { item ->
                    when (item.tag) {

                        "live" -> {
                            navController.navigate(LiveRoutes.Live.route){
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        "guide" -> {
                            navController.navigate(LiveRoutes.Guide.route){
                                popUpTo(0) { inclusive = true }
                                launchSingleTop = true
                            }
                        }

                        "my_channel" -> {

                        }

                    }
                },
                onSelectedPositionClick = { int ->
                    selectedPosition = int
                })

            Row(modifier = Modifier.wrapContentSize()) {
                RoundedIconButton(modifier = Modifier
                    .onFocusChanged {
                        isMicFocused = it.hasFocus
                    }
                    .focusable()
                    .then(
                        if (isMicFocused) {
                            Modifier
                                .shadow(
                                    color = Color.White.copy(alpha = .11f),
                                    borderRadius = 40.dp,
                                    blurRadius = 7.dp,
                                    spread = 5.dp,
                                )
                                .border(
                                    width = 1.dp,
                                    color = textFocusedMainColor,
                                    shape = RoundedCornerShape(40.dp)
                                )
                        } else Modifier
                    ),
                    imageId = R.drawable.microphone_ic,
                    iconHeight = 15,
                    boxSize = 43,
                    iconWidth = 15,
                    backgroundColor = Color.White.copy(alpha = .75f))

                Spacer(modifier = Modifier.width(10.dp))

                RoundedIconButton(modifier = Modifier
                    .onFocusChanged {
                        isSearchFocused = it.hasFocus
                    }
                    .clickable(interactionSource = null, indication = null, onClick = {
                        Log.e("SEARCH_IC", "on search click")
                        //  onSearchClick.invoke()
                    })
                    .focusable()
                    .then(
                        if (isSearchFocused) {
                            Modifier
                                .shadow(
                                    color = Color.White.copy(alpha = .11f),
                                    borderRadius = 40.dp,
                                    blurRadius = 7.dp,
                                    spread = 5.dp,
                                )
                                .border(
                                    width = 1.dp,
                                    color = textFocusedMainColor,
                                    shape = RoundedCornerShape(40.dp)
                                )
                        } else Modifier
                    ),
                    imageId = R.drawable.search_ic,
                    iconHeight = 15,
                    boxSize = 43,
                    iconWidth = 15,
                    backgroundColor = Color.White.copy(alpha = .75f))
            }
        }

    }

}