package com.faithForward.media.search.custom_keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KeyBoardUi(
    modifier: Modifier = Modifier,
    searchInputText: String = "",
    onInputTextChange: (String) -> Unit,
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
            }
        )

    }


}