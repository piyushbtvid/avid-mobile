package com.faithForward.media.commanComponents

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
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.btnShadowColor
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.textFocusedMainColor
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.util.FocusState

@Composable
fun SubscribeButton(
    modifier: Modifier = Modifier,
    backgroundFocusedColor: Color = focusedMainColor,
    backgroundUnFocusedColor: Color = unFocusMainColor,
    buttonShadowColor: Color = focusedMainColor,
    textFocusedColor: Color = textFocusedMainColor,
    textUnFocusedColor: Color = textUnFocusColor,
    buttonText: String,
    onCategoryItemClick: () -> Unit,
    focusState: FocusState,
    @DrawableRes icon: Int? = null,
) {
    val containerColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> backgroundFocusedColor
        FocusState.UNFOCUSED -> backgroundUnFocusedColor
        FocusState.UNDEFINED -> backgroundUnFocusedColor
    }

    val textColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> textFocusedColor
        FocusState.UNFOCUSED -> textUnFocusedColor
        FocusState.UNDEFINED -> textUnFocusedColor
    }

    val buttonModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier
                .shadow(
                    color = backgroundFocusedColor.copy(alpha = .56f),
                    borderRadius = 20.dp,
                    blurRadius = 15.dp,
                    offsetY = 4.dp,
                    offsetX = 0.dp,
                    spread = 4.dp
                )
                .focusable(false)
                .focusProperties {
                    canFocus = false
                }
        } else {
            modifier
                .shadow(
                    color = btnShadowColor,
                    borderRadius = 20.dp,
                    blurRadius = 10.dp,
                    offsetY = 3.dp,
                    offsetX = 0.dp,
                    spread = 1.dp
                )
                .focusable(false)
                .focusProperties {
                    canFocus = false
                }
        }

    Button(
        onClick = {
            onCategoryItemClick.invoke()
        },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(50),
        modifier = buttonModifier.height(38.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                LoadImage(
                    imageResId = icon, modifier = Modifier.size(12.dp)
                )
            }

            Text(
                buttonText, color = textColor, fontSize = 15.sp
            )
        }

    }

}

@Preview
@Composable
fun SubscribeButtonPreview(modifier: Modifier = Modifier) {
    SubscribeButton(focusState = FocusState.UNFOCUSED,
        buttonText = "Subscribe",
        onCategoryItemClick = {

        })
}


@Preview
@Composable
fun SubscribeButtonIconPreview(modifier: Modifier = Modifier) {
    SubscribeButton(focusState = FocusState.UNFOCUSED,
        buttonText = "Access Premium ",
        icon = R.drawable.lock,
        onCategoryItemClick = {

        })
}