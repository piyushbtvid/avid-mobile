package com.faithForward.media.home.creator.detail.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class ContentDto(
    val image: String,
    val title: String,
    val views: String,
    val duration: String,
    val description: String,
    val time: String,
)


@Composable
fun ContentPage(
    modifier: Modifier = Modifier,
    contentDto: ContentDto,
) {

}