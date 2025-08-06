package com.faithForward.media.ui.user_profile.create_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyboardActionState
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyboardMode

@Composable
fun CustomKeyBoard(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit,
    currentKeyboardMode: KeyboardMode,
    onCurrentKeyBoardModeChange: (KeyboardMode) -> Unit,
    onKeyBoardActionButtonClick: (KeyboardActionState) -> Unit,
) {



    val alphabetList = remember {
        mutableListOf(
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"
        )
    }
    val numberList = remember { mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0") }



    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.5.dp)
    ) {

        InputKeyList(
            inputList = if (currentKeyboardMode == KeyboardMode.ALPHABET) alphabetList else if (currentKeyboardMode == KeyboardMode.NUMBER) numberList else alphabetList,
            keyboardMode = currentKeyboardMode,
            onBackspace = { state ->
                onKeyBoardActionButtonClick.invoke(state)
            },
            onKeyPress = { value ->
                onKeyClick.invoke(value)
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            KeyBoardActionButton(
                modifier = Modifier
                    .width(162.dp)
                    .height(36.dp),
                actionState = KeyboardActionState.space,
                displayText = "Space",
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                }
            )

            KeyBoardActionButton(
                modifier = Modifier
                    .width(60.dp)
                    .height(36.dp),
                actionState = if (currentKeyboardMode == KeyboardMode.ALPHABET) KeyboardActionState.number else if (currentKeyboardMode == KeyboardMode.NUMBER) KeyboardActionState.alphabet else KeyboardActionState.alphabet,
                displayText = if (currentKeyboardMode == KeyboardMode.ALPHABET) "123" else if (currentKeyboardMode == KeyboardMode.NUMBER) "abc" else "abc",
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                }
            )

        }

        KeyBoardActionButton(
            modifier = Modifier
                .width(228.dp)
                .height(36.dp),
            actionState = KeyboardActionState.clearAll,
            displayText = "Clear all",
            onClick = { state ->
                onKeyBoardActionButtonClick.invoke(state)
            }
        )

    }


}