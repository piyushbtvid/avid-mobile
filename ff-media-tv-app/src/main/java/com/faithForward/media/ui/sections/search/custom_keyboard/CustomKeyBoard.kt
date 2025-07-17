package com.faithForward.media.ui.sections.search.custom_keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R

enum class KeyboardMode {
    ALPHABET, NUMBER
}

@Composable
fun CustomKeyBoard(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit,
    searchResultLastFocusedIndex: Int ,
    currentKeyboardMode: KeyboardMode,
    onKeyBoardActionButtonClick: (KeyboardActionState) -> Unit,
) {

    val alphabetList = remember {
        mutableListOf(
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z"
        )
    }

    val numberList = remember { mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0") }

    var inputListFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    Row(
        modifier = modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {


        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            KeyboardActionButton(
                actionState = if (currentKeyboardMode == KeyboardMode.ALPHABET) KeyboardActionState.number else if (currentKeyboardMode == KeyboardMode.NUMBER) KeyboardActionState.alphabet else KeyboardActionState.alphabet,
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                },
                searchResultLastFocusedIndex = searchResultLastFocusedIndex,
                displayText = if (currentKeyboardMode == KeyboardMode.ALPHABET) "123" else if (currentKeyboardMode == KeyboardMode.NUMBER) "abc" else "abc",
            )


            KeyboardActionButton(
                actionState = KeyboardActionState.space,
                onClick = { state ->
                    onKeyBoardActionButtonClick.invoke(state)
                },
                displayText = "Space",
                searchResultLastFocusedIndex = searchResultLastFocusedIndex,
            )

        }


        InputKeyList(
            focusedIndex = inputListFocusedIndex,
            onFocusedIndexChanged = { int ->
                inputListFocusedIndex = int
            },
            onItemClick = { value ->
                onKeyClick.invoke(value)
            },
            list = if (currentKeyboardMode == KeyboardMode.ALPHABET) alphabetList else if (currentKeyboardMode == KeyboardMode.NUMBER) numberList else alphabetList
        )

        KeyboardActionButton(
            actionState = KeyboardActionState.clear,
            onClick = { state ->
                onKeyBoardActionButtonClick.invoke(state)
            },
            iconId = R.drawable.outline_backspace_24,
            searchResultLastFocusedIndex = searchResultLastFocusedIndex,
        )

    }


}


@Preview(
    name = "Custom Keyboard - 1920x1080", widthDp = 1920, heightDp = 1080, showBackground = true
)
@Composable
private fun CustomKeyBoardPreview() {
    CustomKeyBoard(
        onKeyClick = {

        },
        currentKeyboardMode = KeyboardMode.NUMBER,
        onKeyBoardActionButtonClick = {

        },
        searchResultLastFocusedIndex = 1
    )
}