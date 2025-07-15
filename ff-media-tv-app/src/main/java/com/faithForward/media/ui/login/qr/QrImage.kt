package com.faithForward.media.ui.login.qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun QrImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .error(R.drawable.banner_test_img)
                .crossfade(true)
                .build(),
            contentDescription = "Poster Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(180.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(4.dp))

        )

        TitleText(
            modifier = Modifier.padding(top = 10.dp),
            text = "Scan this  QR code with your",
            textSize = 11,
            lineHeight = 11,
            color = whiteMain.copy(0.7f),
            maxLine = 1
        )

        TitleText(
            modifier = Modifier.padding(0.dp),
            text = "phone's camera",
            textSize = 11,
            lineHeight = 11,
            color = whiteMain.copy(0.7f),
            maxLine = 1
        )

    }

}


@Preview
@Composable
private fun QRImagePreview() {

    QrImage(imageUrl = "")

}