package com.faithForward.media.ui.universal_page.stream

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StreamPage(modifier: Modifier = Modifier) {

    val streamTestList = remember {
        listOf(
            StreamRecommendationsUiItem(),
            StreamRecommendationsUiItem(),
            StreamRecommendationsUiItem(),
            StreamRecommendationsUiItem(),
            StreamRecommendationsUiItem()
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()

    ) {

        StreamRecommendationsColumn(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 63.5.dp, start = 25.dp),
            list = streamTestList
        )


    }


}