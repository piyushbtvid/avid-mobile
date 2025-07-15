package com.faithForward.media.search.custom_keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun CustomKeyBoard(
    modifier: Modifier = Modifier,
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
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {


        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            KeyboardActionButton(
                actionState = KeyboardActionState.number,
                onClick = { state ->

                },
                displayText = "123",
            )


            KeyboardActionButton(
                actionState = KeyboardActionState.space,
                onClick = { state ->

                },
                displayText = "Space",
            )

        }


        InputKeyList(
            focusedIndex = inputListFocusedIndex,
            onFocusedIndexChanged = { int ->
                inputListFocusedIndex = int
            },
            onItemClick = { value ->

            },
            list = alphabetList
        )

        KeyboardActionButton(
            actionState = KeyboardActionState.clear,
            onClick = { state ->

            },
            iconId = R.drawable.outline_backspace_24,
        )

    }


}


@Preview(
    name = "Custom Keyboard - 1920x1080",
    widthDp = 1920,
    heightDp = 1080,
    showBackground = true
)
@Composable
private fun CustomKeyBoardPreview() {
    CustomKeyBoard()
}