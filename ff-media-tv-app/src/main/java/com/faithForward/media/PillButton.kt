package com.faithForward.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.extensions.shadow
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.textFocusedMainColor
import com.faithForward.media.ui.theme.textUnFocusColor
import com.faithForward.media.ui.theme.unFocusMainColor

@Composable
fun PillButton(
    modifier: Modifier = Modifier,
    backgroundFocusedColor: Color = focusedMainColor,
    backgroundUnFocusedColor: Color = unFocusMainColor,
    buttonShadowColor: Color = focusedMainColor,
    textFocusedColor: Color = textFocusedMainColor,
    textUnFocusedColor: Color = textUnFocusColor,
    btnText: String = "Podcasts",
    focusState: FocusState
) {
    val containerColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> backgroundFocusedColor
        FocusState.UNFOCUSED -> backgroundUnFocusedColor
    }

    val textColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> textFocusedColor
        FocusState.UNFOCUSED -> textUnFocusedColor
    }

    val buttonModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier.shadow(
                color = backgroundFocusedColor,
                borderRadius = 20.dp,
                blurRadius = 15.dp,
                offsetY = 4.dp,
                offsetX = 0.dp,
                spread = 4.dp
            )
        } else {
            modifier
        }

    Button(
        onClick = { /* Handle click */ },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(50),
        modifier = buttonModifier.height(38.dp)
    ) {
        Text(
            btnText,
            color = textColor,
            fontSize = 15.sp
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ButtonPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        PillButton(
            focusState = FocusState.FOCUSED
        )
    }
}


