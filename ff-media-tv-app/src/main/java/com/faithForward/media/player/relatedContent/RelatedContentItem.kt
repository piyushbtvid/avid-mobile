package com.faithForward.media.player.relatedContent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState


data class RelatedContentItemDto(
    val image: String,
    val id: String,
    val slug: String,
    val title: String,

    )

@Composable
fun RelatedContentItem(
    modifier: Modifier = Modifier,
    focusState: FocusState,
    relatedContentItemDto: RelatedContentItemDto,
) {

    val scale by animateFloatAsState(
        targetValue = when (focusState) {
            FocusState.SELECTED, FocusState.FOCUSED -> 1.13f
            else -> 1f
        },
        animationSpec = tween(300), label = ""
    )

    with(relatedContentItemDto) {

        Column(
            modifier = modifier
                .wrapContentSize()
                .scale(scale)
                .zIndex(
                    when (focusState) {
                        FocusState.SELECTED, FocusState.FOCUSED -> 1f
                        else -> 0f
                    }
                )
                .clip(RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .error(R.drawable.banner_test_img)
                    .crossfade(true)
                    .build(),
                contentDescription = "Poster Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(190.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(interactionSource = null, indication = null, onClick = {
                        //   onItemClick.invoke()
                    }
                    )
            )

            Spacer(modifier = Modifier.height(5.dp))

            TitleText(
                modifier = Modifier
                    .width(190.dp)
                    .padding(start = 2.dp),
                text = title,
                textSize = 10,
                lineHeight = 10,
                color = whiteMain,
                fontWeight = FontWeight.Bold
            )

        }

    }

}

@Preview(showBackground = true)
@Composable
private fun RowItemPreview() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize() // Optional: makes sure it takes available space
        ,
        contentAlignment = Alignment.Center
    ) {
        RelatedContentItem(
            focusState = FocusState.UNFOCUSED,
            relatedContentItemDto = RelatedContentItemDto(
                image = "",
                id = "",
                slug = "",
                title = "The Last Ride",
            )
        )
    }
}
