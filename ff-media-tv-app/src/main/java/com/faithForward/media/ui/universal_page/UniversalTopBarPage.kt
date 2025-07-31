package com.faithForward.media.ui.universal_page

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.RoundedIconButton
import com.faithForward.media.ui.theme.textFocusedMainColor
import com.faithForward.media.ui.universal_page.top_bar.TopBarRow
import com.faithForward.media.util.extensions.shadow
import com.faithForward.media.viewModel.UniversalViewModel

@Composable
fun UniversalTopBarPage(
    modifier: Modifier = Modifier,
    universalViewModel: UniversalViewModel,
    onSearchClick: () -> Unit,
    onLiveClick: () -> Unit,
) {

    val topBarItemList = universalViewModel.topBarItems
    val liveFirstUrl = universalViewModel.liveVideo
    var topBarFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var selectedPosition by rememberSaveable { mutableIntStateOf(-1) }
    var isMicFocused by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    val focusRequesterList = remember(topBarItemList.size) {
        List(topBarItemList.size) { FocusRequester() }
    }

    LaunchedEffect(Unit) {
        try {
            focusRequesterList[1].requestFocus()
        } catch (_: Exception) {

        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {


        if (liveFirstUrl.value.isNotEmpty()) {
            UniversalPlayer(
                videoUrlList = liveFirstUrl.value.filterNotNull()
            )
        }

        TopBarRow(
            modifier = Modifier.padding(top = 20.dp),
            selectedPosition = selectedPosition,
            focusedIndex = topBarFocusedIndex,
            onFocusedIndexChange = { int ->
                topBarFocusedIndex = int
            },
            topBarItemList = topBarItemList,
            focusRequesterList = focusRequesterList,
            onItemClick = { item ->
                if (item.tag == "live") {
                    onLiveClick.invoke()
                }
            },
            onSelectedPositionClick = { int ->
                selectedPosition = int
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 45.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedIconButton(
                modifier = Modifier
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
                backgroundColor = Color.White.copy(alpha = .75f)
            )

            Spacer(modifier = Modifier.width(10.dp))

            RoundedIconButton(
                modifier = Modifier
                    .onFocusChanged {
                        isSearchFocused = it.hasFocus
                    }
                    .clickable(interactionSource = null, indication = null, onClick = {
                        Log.e("SEARCH_IC", "on search click")
                        onSearchClick.invoke()
                    }
                    )
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
                backgroundColor = Color.White.copy(alpha = .75f)
            )
        }

    }


}