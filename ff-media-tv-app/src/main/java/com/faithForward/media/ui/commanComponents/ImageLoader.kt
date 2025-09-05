package com.faithForward.media.ui.commanComponents


import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.request.error
import coil3.size.Size
import com.faithForward.media.R


@Composable
internal fun URLImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
    @DrawableRes fallbackRes: Int? = null,
) {
    val isInPreview = LocalInspectionMode.current


    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .crossfade(true)
            .allowHardware(false)
            .build()
    }

    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .listener(
                onStart = { Log.d("Coil", "Loading started") },
                onSuccess = { _, _ -> Log.d("Coil", "Loading successful") },
                onError = { _, errorResult ->
                }
            )
            .size(Size.ORIGINAL)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = imageRequest,
        imageLoader = imageLoader
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.background(Color.Transparent),
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}

@Composable
internal fun LoadImage(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    @DrawableRes imageResId: Int? = null,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
) {
    when {
        imageUrl.isNullOrBlank().not() -> {
            URLImage(
                imageUrl!!,
                modifier = modifier,
                contentScale = contentScale,
                colorFilter = colorFilter,
            )
        }

        imageResId != null -> {
            val painter = painterResource(id = imageResId)
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale,
                colorFilter = colorFilter
            )
        }

        else -> {

        }
    }
}