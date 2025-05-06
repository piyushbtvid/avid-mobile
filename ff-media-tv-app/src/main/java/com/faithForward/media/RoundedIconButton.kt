package com.faithForward.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIconButton(
    modifier: Modifier = Modifier,
    imageId: Int,
) {

    Box(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(40.dp)
            )
            .size(43.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            modifier = Modifier.size(15.dp)
        )
    }

}

@Preview
@Composable
private fun IconPreview() {
    RoundedIconButton(imageId = R.drawable.microphone_ic)
}