package com.faithForward.media.commanComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.textFocusedMainColor

@Composable
fun TopMenu(modifier: Modifier = Modifier) {

    var isMicFocused by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedIconButton(
            modifier = Modifier
                .onFocusChanged {
                    isMicFocused = it.hasFocus
                }
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

@Preview
@Composable
private fun TopMenuPreview() {
    TopMenu()
}