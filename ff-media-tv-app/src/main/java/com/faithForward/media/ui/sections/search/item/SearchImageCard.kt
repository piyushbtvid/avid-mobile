package com.faithForward.media.ui.sections.search.item

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.util.FocusState


@Composable
fun SearchImageCard(
    modifier: Modifier = Modifier,
    searchItemDto: SearchItemDto,
    focusState: FocusState,
    onItemClick: () -> Unit,
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




    Column(
        modifier = modifier
            .width(135.dp)
            .scale(scale)
            .zIndex(
                when (focusState) {
                    FocusState.SELECTED, FocusState.FOCUSED -> 1f
                    else -> 0f
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(searchItemDto.image) // fallback if blank
                .error(placeholderRes)
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
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable(interactionSource = null, indication = null, onClick = {
                    onItemClick.invoke()
                }
                )
        )
    }
}