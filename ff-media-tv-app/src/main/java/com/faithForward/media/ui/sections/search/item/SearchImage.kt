package com.faithForward.media.ui.sections.search.item

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R

@Composable
fun SearchImage(
    modifier: Modifier = Modifier,
    imageSrc: String?,
    @DrawableRes placeholderRes: Int = R.drawable.test_poster, // Your drawable
) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageSrc) // fallback if blank
      //      .error(placeholderRes)
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
        modifier = modifier
            .width(60.5.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(5.dp))

    )


}