package com.faithForward.media.home.creator.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CreatorDetailPage(modifier: Modifier = Modifier) {
    Row {
        Column {
            AnchoredImage(
                creatorName = "kdjfkdjf",
                creatorImageUrl = "",
                creatorBackgroundImageUrl = ""
            )
            CreatorDetail(
                creatorDetailDto = CreatorDetailDto(
                    about = "",
                    subscribersText = "",
                    genre = "",
                    creatorChannelCategory = ""
                )
            )
        }
    }
}

@Preview
@Composable
private fun CreatorDetailPagePreview() {
    CreatorDetailPage()
}