package com.faithForward.media.search.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchUiItemMetaContent(
    modifier: Modifier = Modifier,
    imdb: String?,
    duration: String?,
    genre: String?,
    title: String,
) {

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {



    }

}