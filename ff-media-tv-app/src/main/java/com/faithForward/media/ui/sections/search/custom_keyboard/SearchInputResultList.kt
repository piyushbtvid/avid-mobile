package com.faithForward.media.ui.sections.search.custom_keyboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun SearchInputResultList(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Image(
            modifier = Modifier.size(29.dp),
            painter = painterResource(R.drawable.search_ic),
            contentDescription = null
        )


        TitleText(
            modifier = Modifier.height(35.dp),
            text = text,
            textSize = 30,
            lineHeight = 30,
            color = whiteMain,
            )


    }

}

@Preview
@Composable
private fun InPutResultPreview() {
    SearchInputResultList(
        text = "mnmmmm"
    )
}