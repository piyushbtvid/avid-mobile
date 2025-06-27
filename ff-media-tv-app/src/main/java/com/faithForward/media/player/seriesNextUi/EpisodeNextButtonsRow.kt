package com.faithForward.media.player.seriesNextUi

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.theme.cardShadowColor
import com.faithForward.media.theme.episodeButtonBackground
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun EpisodeNextButtonsRow(modifier: Modifier = Modifier) {
    var isCancelFocused by remember { mutableStateOf(false) }
    var isPlayNowFocused by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        EpisodeButton(
            modifier = Modifier
                .onFocusChanged { isCancelFocused = it.hasFocus }
                .focusable(),
            focusState = if (isCancelFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
            buttonText = "Cancel",
            icon = null,
            backgroundFocusedColor = episodeButtonBackground,
            backgroundUnFocusedColor = cardShadowColor,
            textFocusedColor = whiteMain,
            textUnFocusColor = whiteMain,
            onButtonClick = {
                // Cancel button click logic
            },
        )
        EpisodeButton(
            modifier = Modifier
                .onFocusChanged { isPlayNowFocused = it.hasFocus }
                .focusable(),
            focusState = if (isPlayNowFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
            buttonText = "Play Now",
            icon = R.drawable.ic_play,
            backgroundFocusedColor = episodeButtonBackground,
            backgroundUnFocusedColor = cardShadowColor,
            textFocusedColor = whiteMain,
            textUnFocusColor = whiteMain,
            onButtonClick = {
                // Play Now button click logic
            }
        )
    }
}


@Preview
@Composable
private fun ButtonRowPreview() {

    EpisodeNextButtonsRow()

}