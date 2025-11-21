package com.faithForward.media.ui.sections.my_account.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun TextWithTitle(
    modifier: Modifier = Modifier,
    titleText: String,
    displayText: String,
) {

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        TitleText(
            text = titleText,
            textSize = 15,
            lineHeight = 15,
            color = whiteMain,
            fontWeight = FontWeight.Bold
        )

        TitleText(
            text = displayText,
            textSize = 11,
            lineHeight = 11,
            color = focusedMainColor,
            fontWeight = FontWeight.Normal
        )


    }


}