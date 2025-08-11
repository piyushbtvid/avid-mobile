package com.faithForward.media.ui.sections.search.custom_keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KeyBoardUi(
    modifier: Modifier = Modifier,
    searchInputText: String,
    onInputTextChange: (String) -> Unit,
    searchResultLastFocusedIndex: Int = -1,
    currentKeyboardMode: KeyboardMode,
    onKeyBoardActionButtonClick: (NewKeyboardActionState) -> Unit,
) {


    Column(
        modifier = modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        SearchInputResultList(
            text = searchInputText
        )

        CustomKeyBoard(
            onKeyClick = { value ->
                onInputTextChange.invoke(value)
            },
            searchResultLastFocusedIndex = searchResultLastFocusedIndex,
            currentKeyboardMode = currentKeyboardMode,
            onKeyBoardActionButtonClick = onKeyBoardActionButtonClick
        )

    }


}