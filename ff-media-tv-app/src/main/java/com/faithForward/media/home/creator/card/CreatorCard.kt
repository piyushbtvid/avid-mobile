package com.faithForward.media.home.creator.card

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.faithForward.media.extensions.shadow
import com.faithForward.media.ui.theme.cardShadowColor
import com.faithForward.media.ui.theme.creatorShadowColor
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.text100
import com.faithForward.media.ui.theme.unFocusMainColor


data class CreatorCardDto(
    val creatorImageUrl: String,
    val creatorName: String,
    val creatorSubscriberText: String
)

@Composable
fun CreatorCard(
    modifier: Modifier = Modifier,
    creatorCardDto: CreatorCardDto,
    isFocused: Boolean = false
) {
    Column(
        modifier = modifier,
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
            modifier = (if (isFocused) Modifier
                .shadow(
                    color = creatorShadowColor,
                    borderRadius = 70.dp,
                    blurRadius = 11.dp,
                    offsetY = 8.dp,
                    offsetX = 0.dp,
                    spread = 10.dp,
                ) else Modifier)
                .size(136.dp)
                .clip(RoundedCornerShape(70.dp))
                .border(
                    width = if (isFocused) 2.5.dp else 0.dp,
                    color = if (isFocused) focusedMainColor else Color.Transparent,
                    shape = RoundedCornerShape(70.dp)
                )

                .background(color = unFocusMainColor)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = creatorCardDto.creatorName,
            color = if (isFocused) text100 else cardShadowColor,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = creatorCardDto.creatorSubscriberText,
            color = if (isFocused) text100 else cardShadowColor,
            fontSize = 15.sp
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
        ),
        isFocused = false
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
        ),
        isFocused = true
    )
}
