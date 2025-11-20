package com.faithForward.media.ui.sections.genre

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.faithForward.media.R
import com.faithForward.media.ui.theme.gray
import com.faithForward.media.util.FocusState
import com.faithForward.media.util.Util.isTvDevice

data class GenreImageCardDto(
    val posterImageSrc: String,
)

@Composable
fun GenreImageCard(
    modifier: Modifier = Modifier,
    posterCardDto: GenreImageCardDto,
    focusState: FocusState,
    onItemClick: () -> Unit,
    imageContentScale: ContentScale = ContentScale.FillBounds,
    cardShadowColor: Color = com.faithForward.media.ui.theme.cardShadowColor,
    @DrawableRes placeholderRes: Int = R.drawable.test_poster, // Your drawable
) {

    val scale by animateFloatAsState(
        targetValue = when (focusState) {
            FocusState.SELECTED, FocusState.FOCUSED -> 1.13f
            else -> 1f
        },
        animationSpec = tween(300), label = ""
    )



    val isTv = LocalContext.current.isTvDevice()

    Box(
        modifier = modifier
            .width(if (isTv)135.dp else 102.dp)
            .height(if (isTv) 210.dp else 160.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(gray) // fills the transparent area, keeps rounded shape
            .clickable(
                interactionSource = null,
                indication = null
            ) { onItemClick.invoke() }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterCardDto.posterImageSrc)
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
            contentScale = imageContentScale,
            modifier = Modifier.fillMaxSize()
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PosterCardLazyRowPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            items(3) { index ->
                GenreImageCard(
                    posterCardDto = GenreImageCardDto(posterImageSrc = ""), // Leave blank to test drawable fallback
                    focusState = if (index == 0) FocusState.FOCUSED else FocusState.UNFOCUSED,
                    onItemClick = {

                    }
                )
            }
        }
    }
}


