package com.faithForward.media.ui.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.textFocusedMainColor
import com.faithForward.media.ui.theme.topBarTextFocusedStyle
import com.faithForward.media.ui.theme.topBarTextUnFocusedStyle
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState
import com.faithForward.media.util.extensions.shadow

data class TopBarItemDto(
    val name: String,
    val tag: String,
)

@Composable
fun TopBarItem(
    modifier: Modifier = Modifier,
    topBarItemDto: TopBarItemDto,
    focusState: FocusState,
    backgroundFocusedColor: Color,
    backgroundUnFocusedColor: Color,
    shadowColor: Color = focusedMainColor.copy(0.55f),
    borderColor: Color = focusedMainColor,
    textFocusedStyle: TextStyle = topBarTextFocusedStyle,
    textUnFocusedStyle: TextStyle = topBarTextUnFocusedStyle,
    onCategoryItemClick: (String) -> Unit,
) {

    val containerColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> backgroundFocusedColor
        FocusState.UNFOCUSED -> backgroundUnFocusedColor
        FocusState.UNDEFINED -> backgroundUnFocusedColor // Default to unfocused color for UNDEFINED
    }

    val pillTextStyle = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> textFocusedStyle
        FocusState.UNFOCUSED -> textUnFocusedStyle
        FocusState.UNDEFINED -> textUnFocusedStyle // Default to unfocused text color for UNDEFINED
    }

    val buttonShape = RoundedCornerShape(50.dp)


    val buttonModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier
                .shadow(
                    color = shadowColor,
                    borderRadius = 20.dp,
                    blurRadius = 15.dp,
                    offsetY = 4.dp,
                    offsetX = 0.dp,
                    spread = 3.dp
                )
                .border(
                    width = 0.4.dp,
                    color = borderColor,
                    shape = buttonShape // Pass the same shape
                )
                .clip(buttonShape) // First clip
        } else {
            modifier.shadow(
                color = Color.Transparent,
                borderRadius = 20.dp,
                blurRadius = 10.dp,
                offsetY = 3.dp,
                offsetX = 0.dp,
                spread = 1.dp
            )
        }



    Button(
        onClick = {
            onCategoryItemClick.invoke(topBarItemDto.tag)
        },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = buttonModifier
            .height(25.dp)
    ) {
        Text(
            topBarItemDto.name,
            style = pillTextStyle,
        )
    }
}


@Preview
@Composable
private fun TopBarItemPreview() {

    val item = TopBarItemDto(
        name = "LIVE", tag = "live"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = textFocusedMainColor),
        contentAlignment = Alignment.Center
    ) {

        TopBarItem(topBarItemDto = item,
            focusState = FocusState.UNFOCUSED,
            backgroundUnFocusedColor = Color.Transparent,
            backgroundFocusedColor = whiteMain.copy(alpha = 0.55f),
            onCategoryItemClick = {

            })

    }
}