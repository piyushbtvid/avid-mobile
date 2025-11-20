package com.faithForward.media.ui.universal_page.live.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.universal_page.UniversalPlayer

@Composable
fun GuideSmallPlayer(
    modifier: Modifier = Modifier,
    urlList: List<String?>,
) {

    Box(
        modifier = modifier
            .width(357.5.dp)
            .height(203.dp)
            .background(pageBlackBackgroundColor)
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        UniversalPlayer(
            videoUrlList = urlList
        )
    }


}