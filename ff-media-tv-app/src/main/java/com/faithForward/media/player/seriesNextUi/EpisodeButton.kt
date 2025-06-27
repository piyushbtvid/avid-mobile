package com.faithForward.media.player.seriesNextUi

import androidx.annotation.DrawableRes
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.R
import com.faithForward.media.commanComponents.LoadImage
import com.faithForward.media.theme.episodeButtonBackground
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.theme.playButtonBackgroundColor
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun EpisodeButton(
    modifier: Modifier = Modifier,
    focusState: FocusState,
    @DrawableRes icon: Int? = R.drawable.ic_play,
    buttonText: String,
    backgroundFocusedColor: Color = focusedMainColor,
    backgroundUnFocusedColor: Color = unFocusMainColor,
    textFocusedColor: Color = whiteMain,
    textUnFocusColor: Color = whiteMain,
    onButtonClick: () -> Unit,
) {

    val containerColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> backgroundFocusedColor
        FocusState.UNFOCUSED -> backgroundUnFocusedColor
        FocusState.UNDEFINED -> backgroundUnFocusedColor
    }

    val textColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> textFocusedColor
        FocusState.UNFOCUSED -> textUnFocusColor
        FocusState.UNDEFINED -> textUnFocusColor
    }

    val buttonModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier
        } else {
            modifier
        }

    Button(
        onClick = {
            onButtonClick.invoke()
        },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(10),
        modifier = buttonModifier.height(36.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                buttonText, color = textColor, fontSize = 10.sp
            )

            if (icon != null) {
                LoadImage(
                    imageResId = icon, modifier = Modifier.size(10.dp)
                )
            }
        }

    }


}


@Preview
@Composable
private fun EpisodeButtonPreview() {
    EpisodeButton(
        focusState = FocusState.FOCUSED,
        textFocusedColor = whiteMain,
        textUnFocusColor = whiteMain,
        backgroundFocusedColor = episodeButtonBackground,
        backgroundUnFocusedColor = pageBlackBackgroundColor,
        buttonText = "Play Now",

        onButtonClick = {

        }
    )
}