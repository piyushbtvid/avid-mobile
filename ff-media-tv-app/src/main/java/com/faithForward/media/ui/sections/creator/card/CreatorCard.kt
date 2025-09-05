package com.faithForward.media.ui.sections.creator.card

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.faithForward.media.ui.theme.focusedTextColor
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.Util.isTvDevice


data class CreatorCardDto(
    val id: Int,
    val creatorImageUrl: String,
    val channelBannerImage: String? = null,
    val creatorName: String,
    val channelDescription: String,
    val creatorSubscriberText: String,
)


@Composable
fun CreatorCard(
    modifier: Modifier = Modifier,
    creatorCardDto: CreatorCardDto,
    isFocused: Boolean = false,
) {
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.13f else 1f,
        animationSpec = tween(300),
        label = "scaleAnimation"
    )
    val spacerHeight by animateDpAsState(
        targetValue = 26.dp,
        animationSpec = tween(300),
        label = "spacerHeightAnimation"
    )

    Column(
        modifier = modifier
            .zIndex(if (isFocused) 1f else 0f) // Apply zIndex to the entire card
            .padding(8.dp), // Optional: Add padding to prevent overlap issues
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(creatorCardDto.creatorImageUrl) // fallback if blank
                .listener(
                    onError = { request, throwable ->
                        Log.e("CoilError", "Image load failed ${throwable.throwable}")
                    },
                    onSuccess = { _, _ ->
                        Log.e("CoilSuccess", "Image loaded successfully")
                    }
                )
                .crossfade(true)
                .build(),
            contentDescription = "Poster Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(if (LocalContext.current.isTvDevice())136.dp else 100.dp)
                .scale(scale)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0f) // Top-left pivot
                }
                .clip(RoundedCornerShape(70.dp))
                .background(color = unFocusMainColor)
        )
        Spacer(modifier = Modifier.height(spacerHeight))

        Text(
            text = creatorCardDto.creatorName,
            color = if (isFocused) whiteMain else focusedTextColor,
            fontSize = 10.sp,
            lineHeight = 10.sp,

            )
        Spacer(
            modifier = Modifier
                .height(5.dp)
        )
        Text(
            text = creatorCardDto.creatorSubscriberText,
            color = if (isFocused) whiteMain else focusedTextColor,
            fontSize = 7.5.sp,
            lineHeight = 7.5.sp,
        )
    }
}

@Preview
@Composable
private fun CreatorCardPreview() {
    CreatorCard(
        creatorCardDto = CreatorCardDto(
            creatorSubscriberText = "130K Subscribers",
            creatorImageUrl = "",
            creatorName = "Jasmine Wright",
            channelDescription = ",khbhbhb",
            id = 1
        ), isFocused = false
    )
}

@Preview
@Composable
private fun CreatorCardFocusedPreview() {
    CreatorCard(
        creatorCardDto = CreatorCardDto(
            creatorSubscriberText = "130K Subscribers",
            creatorImageUrl = "",
            creatorName = "Jasmine Wright",
            channelDescription = "jhmb",
            id = 1
        ), isFocused = true
    )
}
