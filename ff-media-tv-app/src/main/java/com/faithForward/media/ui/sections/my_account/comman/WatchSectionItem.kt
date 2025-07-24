package com.faithForward.media.ui.sections.my_account.comman

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.myAccountItemBackground
import com.faithForward.media.ui.theme.pillButtonTextUnFocusColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState

data class WatchSectionItemDto(
    val contentType: String,
    val id: String,
    val contentSlug: String,
    val title: String,
    val description: String,
    val progress: Long = 0,
    val duration: Long,
    val timeLeft: String,
    val image: String,
)

@Composable
fun WatchSectionItem(
    modifier: Modifier = Modifier,
    focusState: FocusState,
    watchSectionItemDto: WatchSectionItemDto,
) {

    val scale by animateFloatAsState(
        targetValue = when (focusState) {
            FocusState.SELECTED, FocusState.FOCUSED -> 1.13f
            else -> 1f
        },
        animationSpec = tween(300), label = ""
    )

    Column(
        modifier = modifier
            .scale(scale)
            .width(150.dp)
            .wrapContentHeight()
            .background(myAccountItemBackground)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(watchSectionItemDto.image) // fallback if blank
                .error(R.drawable.banner_test_img)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        )
        val progressFloat = if (watchSectionItemDto.duration > 0) {
            watchSectionItemDto.progress.toFloat() / watchSectionItemDto.duration
        } else {
            0f
        }

        if (progressFloat > 0f) {
            LinearProgressIndicator(
                progress = { progressFloat.coerceIn(0f, 1f) },
                color = focusedMainColor,
                trackColor = pillButtonTextUnFocusColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp) // Optional: add height
            )
        }

        WatchSectionMetaData(
            title = watchSectionItemDto.title,
            description = watchSectionItemDto.description,
            timeLeft = watchSectionItemDto.timeLeft
        )

    }

}


@Composable
fun WatchSectionMetaData(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    timeLeft: String,
) {

    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {


        TitleText(
            textSize = 13,
            lineHeight = 1,
            color = whiteMain,
            fontWeight = FontWeight.Bold,
            maxLine = 1,
            text = title
        )

        TitleText(
            textSize = 13, lineHeight = 13, color = whiteMain, maxLine = 2, text = description
        )


        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TitleText(
                text = timeLeft, textSize = 12, lineHeight = 12, color = whiteMain
            )


            Image(
                modifier = Modifier.size(23.dp),
                painter = painterResource(R.drawable.profile_play_ic), contentDescription = null
            )

        }

    }

}

@Preview
@Composable
private fun WatchSectionItemPreview() {

    val item = WatchSectionItemDto(
        title = "The Last Ride",
        description = "The Last Ride The Last Ride The Last Ride The LAst Ride The Last Ride The Last Ride The Last Ride The LAst",
        contentType = "Movie",
        id = "123",
        contentSlug = "mnj",
        progress = 168,
        timeLeft = "13m 1s left",
        image = "",
        duration = 1851
    )

    WatchSectionItem(
        focusState = FocusState.FOCUSED,
        watchSectionItemDto = item
    )

}
