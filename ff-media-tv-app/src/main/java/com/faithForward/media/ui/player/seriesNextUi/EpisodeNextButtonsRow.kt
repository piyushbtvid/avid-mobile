package com.faithForward.media.ui.player.seriesNextUi

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.theme.cardShadowColor
import com.faithForward.media.ui.theme.episodeButtonBackground
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun EpisodeNextButtonsRow(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onPlayNowClick: () -> Unit,
) {
    var isCancelFocused by remember { mutableStateOf(false) }
    var isPlayNowFocused by remember { mutableStateOf(false) }

    val playNowFocusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EpisodeButton(
            modifier = Modifier
                .onFocusChanged { isCancelFocused = it.hasFocus }
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.DirectionLeft || keyEvent.key == Key.DirectionUp || keyEvent.key == Key.DirectionDown) {
                        true
                    } else {
                        false
                    }
                },
            focusState = if (isCancelFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
            buttonText = "Cancel",
            icon = null,
            backgroundFocusedColor = episodeButtonBackground,
            backgroundUnFocusedColor = cardShadowColor,
            textFocusedColor = whiteMain,
            textUnFocusColor = whiteMain,
            onButtonClick = {
                onCancelClick.invoke()
            },
        )
        EpisodeButton(
            modifier = Modifier
                .focusRequester(playNowFocusRequester)
                .onFocusChanged { isPlayNowFocused = it.hasFocus }
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.DirectionRight || keyEvent.key == Key.DirectionUp || keyEvent.key == Key.DirectionDown) {
                        true
                    } else {
                        false
                    }
                },
            focusState = if (isPlayNowFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
            buttonText = "Play Now",
            icon = R.drawable.ic_play,
            backgroundFocusedColor = episodeButtonBackground,
            backgroundUnFocusedColor = cardShadowColor,
            textFocusedColor = whiteMain,
            textUnFocusColor = whiteMain,
            onButtonClick = {
                onPlayNowClick.invoke()
            }
        )
    }

    LaunchedEffect(Unit) {
        playNowFocusRequester.requestFocus()
    }
}


@Preview
@Composable
private fun ButtonRowPreview() {

    EpisodeNextButtonsRow(
        onCancelClick = {

        },
        onPlayNowClick = {

        }
    )

}