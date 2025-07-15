package com.faithForward.media.search.custom_keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun KeyBoardKeyItem(
    modifier: Modifier = Modifier,
    displayText: String,
    focusState: FocusState,
) {

    val modifiedModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier
                .clip(
                    shape = RoundedCornerShape(50.dp)
                )
                .shadow(
                    color = whiteMain.copy(alpha = 0.50f),  //cardShadowColor
                    borderRadius = 23.dp,
                    blurRadius = 4.7.dp,
                    offsetY = 0.dp,
                    offsetX = 0.dp,
                    spread = 5.dp,
                )
                .background(pageBlackBackgroundColor)
        } else {
            modifier
        }


    Box(
        modifier = modifiedModifier
            .size(25.5.dp),
        contentAlignment = Alignment.Center
    ) {


        TitleText(
            text = displayText,
            textSize = 15,
            lineHeight = 15,
            color = whiteMain,
            fontWeight = FontWeight.Medium
        )


    }


}


@Preview(showSystemUi = true)
@Composable
private fun ItemKeBoardPreview() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {

            items(5) {
                KeyBoardKeyItem(
                    displayText = "d",
                    focusState = FocusState.FOCUSED
                )
            }

        }
    }
}