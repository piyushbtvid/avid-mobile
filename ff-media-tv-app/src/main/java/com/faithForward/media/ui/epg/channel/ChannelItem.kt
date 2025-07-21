package com.faithForward.media.ui.epg.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.LoadImage
import com.faithForward.media.ui.theme.cardShadowColor

data class Channel(
    val channelName: String,
    val channelImage: String,
)

@Composable
fun ChannelItem(
    modifier: Modifier = Modifier,
    channel: Channel,
) {
    Box(
        modifier = modifier
            .width(136.5.dp)
            .height(75.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = cardShadowColor)
            .padding(10.dp)
    ) {
        LoadImage(modifier = Modifier.fillMaxSize(), imageUrl = channel.channelImage)
    }
}

@Preview
@Composable
private fun ChannelItemPreview() {
    ChannelItem(
        channel = Channel(
            channelImage = "",
            channelName = ""
        )
    )
}