package com.faithForward.media.ui.user_profile.comman

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.R
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyboardMode
import com.faithForward.media.ui.sections.search.custom_keyboard.NewKeyboardActionState
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun InputKeyList(
    modifier: Modifier = Modifier,
    inputList: List<String>,
    onKeyPress: (String) -> Unit,
    keyboardMode: KeyboardMode = KeyboardMode.ALPHABET,
    keyboardActionState: NewKeyboardActionState = NewKeyboardActionState.large,
    onBackspace: (NewKeyboardActionState) -> Unit,
    onUpPress: (NewKeyboardActionState) -> Unit,
) {

    val firstFocusRequester = remember { FocusRequester() }

    val chunkedInput = remember(inputList) {
        inputList.chunked(10) // split into rows of 7 characters each
    }

    var isClearFocused by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            firstFocusRequester.requestFocus()
        } catch (_: Exception) {

        }
    }

    Column(modifier = modifier) {
        chunkedInput.forEachIndexed { rowIndex, row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(vertical = 4.dp)

            ) {
                row.forEachIndexed { index, char ->
                    KeyButton(
                        text = char,
                        focusRequester = if (rowIndex == 0 && index == 0) firstFocusRequester else FocusRequester(),
                        onKeyPress = { onKeyPress(char) }
                    )
                }

                // Show Clear button conditionally
                val shouldShowClear = (keyboardMode == KeyboardMode.ALPHABET && rowIndex == 2) ||
                        (keyboardMode == KeyboardMode.NUMBER && rowIndex == 0)

                if (shouldShowClear) {

                    Spacer(
                        modifier = Modifier
                            .width(24.dp)
                            .height(28.dp)
                    )

                    UpButton(
                        onKeyPress = {
                            val newState = if (keyboardActionState == NewKeyboardActionState.large)
                                NewKeyboardActionState.small
                            else
                                NewKeyboardActionState.large

                            onUpPress.invoke(newState)
                        }
                    )


                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .width(17.dp)
                                .height(14.dp)
                                .onFocusChanged {
                                    isClearFocused = it.hasFocus
                                }
                                .focusable()
                                .clickable(
                                    interactionSource = null,
                                    indication = null
                                ) {
                                    onBackspace(NewKeyboardActionState.clear)
                                },
                            painter = painterResource(R.drawable.clear_ic),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                if (isClearFocused) focusedMainColor else Color.White.copy(alpha = 0.90f)
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    text: String,
    focusRequester: FocusRequester,
    onKeyPress: (String) -> Unit,
) {

    var isFocused by remember {
        mutableStateOf(true)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(24.dp)
            .height(28.dp)
            .background(
                if (isFocused) Color.White else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.hasFocus
            }
            .focusable()
            .clickable(interactionSource = null, indication = null, onClick = {
                onKeyPress.invoke(text)
            })
    ) {
        Text(
            text = text,
            color = if (isFocused) pageBlackBackgroundColor else Color.White.copy(alpha = 0.90f),
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        )
    }

}


@Composable
fun UpButton(
    modifier: Modifier = Modifier,
    onKeyPress: () -> Unit,
) {

    var isFocused by remember {
        mutableStateOf(true)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(24.dp)
            .height(28.dp)
            .background(
                if (isFocused) Color.White else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .onFocusChanged {
                isFocused = it.hasFocus
            }
            .focusable()
            .clickable(interactionSource = null, indication = null, onClick = {
                Log.e("ON_UP","on up click")
                onKeyPress.invoke()
            })
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(R.drawable.up_arrow),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                if (isFocused) pageBlackBackgroundColor else whiteMain
            )
        )
    }

}

@Composable
fun KeyBoardActionButton(
    modifier: Modifier = Modifier,
    actionState: NewKeyboardActionState,
    displayText: String,
    onClick: (NewKeyboardActionState) -> Unit,
) {

    var isFocused by remember { mutableStateOf(false) }

    Box(modifier = modifier
        .background(
            color = if (isFocused) focusedMainColor else Color.White.copy(alpha = .33f),
            shape = RoundedCornerShape(6.dp)
        )
        .onFocusChanged {
            isFocused = it.hasFocus
        }
        .clickable(interactionSource = null, indication = null, onClick = {
            onClick.invoke(actionState)
        })
        .focusable(), contentAlignment = Alignment.Center
    ) {

        Text(
            text = displayText,
            color = if (isFocused) pageBlackBackgroundColor else Color.White.copy(alpha = 0.90f),
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.W500
        )
    }
}
