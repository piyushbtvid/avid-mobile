package com.faithForward.media.commanComponents

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.posterCardShadowColor
import com.faithForward.media.util.FocusState
import kotlinx.serialization.Serializable

@Serializable
data class PosterCardDto(
    val id: String?,
    val slug: String?,
    val posterImageSrc: String,
    val title: String,
    val description: String,
    val genre: String? = null,
    val seasons: Int? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
    val releaseDate: String? = null,
    val videoHlsUrl: String? = null,
    val seriesSlug: String? = null,
    val progress: Long? = null,
    val contentType: String? = null,
    val isRelatedSeries: Boolean? = false,
)

@Composable
fun PosterCard(
    modifier: Modifier = Modifier,
    posterCardDto: PosterCardDto,
    focusState: FocusState,
    onItemClick: (PosterCardDto) -> Unit,
    cardShadowColor: Color = posterCardShadowColor,
    @DrawableRes placeholderRes: Int = R.drawable.test_poster, // Your drawable
) {

    val scale by animateFloatAsState(
        targetValue = when (focusState) {
            FocusState.SELECTED, FocusState.FOCUSED -> 1.13f
            else -> 1f
        }, animationSpec = tween(300), label = ""
    )

    val posterModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier.shadow(
                color = cardShadowColor,
                borderRadius = 23.dp,
                blurRadius = 14.dp,
                offsetY = 8.dp,
                offsetX = 8.dp,
                spread = 3.dp,
                scale = scale
            )
        } else {
            modifier
        }


    Column(modifier = posterModifier
        .width(135.dp)
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            transformOrigin = TransformOrigin(0f, 0.5f) // X-center, Y-top
        }
        .zIndex(
            when (focusState) {
                FocusState.SELECTED, FocusState.FOCUSED -> 1f
                else -> 0f
            }
        ), horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterCardDto.posterImageSrc) // fallback if blank
                .listener(onError = { request, throwable ->
                    Log.e("CoilError", "Image load failed ${throwable.throwable}")
                }, onSuccess = { _, _ ->
                    Log.e("CoilSuccess", "Image loaded successfully")
                }).crossfade(true).build(),
            contentDescription = "Poster Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable(interactionSource = null, indication = null, onClick = {
                    onItemClick.invoke(posterCardDto)
                })
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PosterCardLazyRowPreview() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp)
        ) {
            items(3) { index ->
                PosterCard(posterCardDto = PosterCardDto(
                    posterImageSrc = "", id = "", title = "", description = "", slug = ""
                ), // Leave blank to test drawable fallback
                    focusState = if (index == 0) FocusState.FOCUSED else FocusState.UNFOCUSED,
                    onItemClick = {

                    })
            }
        }
    }
}


