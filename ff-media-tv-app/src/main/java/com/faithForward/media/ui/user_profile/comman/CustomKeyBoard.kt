package com.faithForward.media.ui.user_profile.comman

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyboardMode
import com.faithForward.media.ui.sections.search.custom_keyboard.NewKeyboardActionState

@Composable
fun CustomKeyBoard(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit,
    currentKeyboardMode: KeyboardMode,
    keyboardActionState: NewKeyboardActionState = NewKeyboardActionState.large,
    onCurrentKeyBoardModeChange: (KeyboardMode) -> Unit,
    onKeyBoardActionButtonClick: (NewKeyboardActionState) -> Unit,
) {


    val alphabetList = remember {
        mutableListOf(
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"
        )
    }

    val smallAlphabetList = remember {
        mutableListOf(
            "a", "b", "c", "d", "e", "f", "g",
            "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u",
            "v", "w", "x", "y", "z"
        )
    }

    val numberList = remember { mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0") }



    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputKeyList(
            inputList = when (currentKeyboardMode) {
                KeyboardMode.ALPHABET -> {
                    if (keyboardActionState == NewKeyboardActionState.large) alphabetList else smallAlphabetList
                }

                KeyboardMode.NUMBER -> numberList
                else -> alphabetList // default fallback
            },
            keyboardMode = currentKeyboardMode,
            onBackspace = { state ->
                onKeyBoardActionButtonClick.invoke(state)
            },
            onKeyPress = { value ->
                onKeyClick.invoke(value)
            },
            onUpPress = { state ->
                Log.e("ON_UP", "on up click with $state")
                onKeyBoardActionButtonClick.invoke(state)
            },
            keyboardActionState = keyboardActionState
        )


        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            KeyBoardActionButton(
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp),
                actionState = NewKeyboardActionState.space,
                displayText = "Space",
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                }
            )

            KeyBoardActionButton(
                modifier = Modifier
                    .width(60.dp)
                    .height(36.dp),
                actionState = if (currentKeyboardMode == KeyboardMode.ALPHABET) NewKeyboardActionState.number else if (currentKeyboardMode == KeyboardMode.NUMBER) NewKeyboardActionState.alphabet else NewKeyboardActionState.alphabet,
                displayText = if (currentKeyboardMode == KeyboardMode.ALPHABET) "123" else if (currentKeyboardMode == KeyboardMode.NUMBER) "abc" else "abc",
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                }
            )

            KeyBoardActionButton(
                modifier = Modifier
                    .width(100.dp)
                    .height(36.dp),
                actionState = NewKeyboardActionState.clearAll,
                displayText = "Clear all",
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                }
            )
        }

    }


}