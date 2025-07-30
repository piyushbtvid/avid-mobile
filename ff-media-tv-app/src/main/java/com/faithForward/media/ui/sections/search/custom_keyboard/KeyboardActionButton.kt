package com.faithForward.media.ui.sections.search.custom_keyboard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.focusedTextColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain


enum class KeyboardActionState {
    space, number, alphabet, clear
}

@Composable
fun KeyboardActionButton(
    modifier: Modifier = Modifier,
    actionState: KeyboardActionState,
    onClick: (KeyboardActionState) -> Unit,
    searchResultLastFocusedIndex: Int,
    displayText: String? = null,
    firstFocusRequester: FocusRequester? = null,
    onRequestFocusOnFirst: () -> Unit,
    iconId: Int? = null,
) {

    var isFocused by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        try {
            if ((actionState == KeyboardActionState.number || actionState == KeyboardActionState.alphabet) && searchResultLastFocusedIndex == -1) {
                Log.e(
                    "SEARCH_RESULT",
                    "KEYboard button request Focus called with $searchResultLastFocusedIndex"
                )
                firstFocusRequester?.requestFocus()
            }
        } catch (_: Exception) {

        }
    }


    Box(modifier = modifier
        .wrapContentSize()
        .background(
            color = if (isFocused) focusedMainColor else focusedTextColor,
            shape = RoundedCornerShape(30.dp)
        )
        .padding(
            horizontal = 10.dp, vertical = 5.dp
        )
        .focusRequester(firstFocusRequester ?: FocusRequester())
        .onFocusChanged {
            isFocused = it.hasFocus
        }
        .onKeyEvent { keyEvent ->
            if (keyEvent.key == Key.DirectionRight && keyEvent.type == KeyEventType.KeyDown) {
                Log.e("DIRECTION_RIGHT", "direction right clicked")
                if (actionState == KeyboardActionState.clear) {
                    onRequestFocusOnFirst.invoke()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
        .clickable(interactionSource = null, indication = null, onClick = {
            onClick.invoke(actionState)
        })
        .focusable(), contentAlignment = Alignment.Center
    ) {

        if (displayText != null) {
            TitleText(
                text = displayText,
                color = if (isFocused) whiteMain else pageBlackBackgroundColor,
                textSize = 10,
                lineHeight = 10
            )
        }

        if (iconId != null) {
            Image(
                modifier = Modifier
                    .width(12.dp)
                    .height(10.dp),
                painter = painterResource(iconId),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = if (isFocused) whiteMain else pageBlackBackgroundColor)
            )
        }
    }
}

@Preview
@Composable
private fun ActionButtonPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        KeyboardActionButton(
            actionState = KeyboardActionState.number,
            displayText = "123",
            searchResultLastFocusedIndex = 1,
            onRequestFocusOnFirst = {},
            onClick = { },
        )

        KeyboardActionButton(
            actionState = KeyboardActionState.clear,
            iconId = R.drawable.outline_backspace_24,
            searchResultLastFocusedIndex = 1,
            onRequestFocusOnFirst = {},
            onClick = { },
        )


        KeyboardActionButton(
            actionState = KeyboardActionState.space,
            displayText = "Space",
            searchResultLastFocusedIndex = 1,
            onRequestFocusOnFirst = {},
            onClick = { },
        )

    }
}