package com.faithForward.media.commanComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.theme.textFocusedMainColor

@Composable
fun RoundedIconButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    iconWidth: Int = 15,
    iconHeight: Int = 15,
    boxSize : Int = 43,
    imageId: Int,
) {

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(40.dp)
            )
            .size(boxSize.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            modifier = Modifier
                .width(iconWidth.dp)
                .height(iconHeight.dp)
        )
    }

}

@Preview
@Composable
private fun IconPreview() {
    RoundedIconButton(
        imageId = R.drawable.play_ic,
        backgroundColor = textFocusedMainColor.copy(alpha = 4.5f)
    )
}