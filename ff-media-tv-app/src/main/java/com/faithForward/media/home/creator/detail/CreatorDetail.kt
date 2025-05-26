package com.faithForward.media.home.creator.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.theme.creatorTitleStyle


data class CreatorDetailDto(
    val about: String,
    val subscribersText: String,
    val genre: String,
    val creatorChannelCategory: String,
)

@Composable
fun CreatorDetail(
    modifier: Modifier = Modifier,
    creatorDetailDto: CreatorDetailDto
) {
    with(creatorDetailDto) {
        Column(modifier = modifier) {
            Row {
                Text(
                    modifier = Modifier
                        .padding(start = 110.dp)
                        .offset(y = 41.5.dp),
                    text = creatorChannelCategory,
                    maxLines = 1,
                    style = creatorTitleStyle,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(start = 110.dp)
                        .offset(y = 41.5.dp),
                    text = genre,
                    maxLines = 1,
                    style = creatorTitleStyle,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(start = 110.dp)
                        .offset(y = 41.5.dp),
                    text = subscribersText,
                    maxLines = 1,
                    style = creatorTitleStyle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreatorDetailPreview() {
    CreatorDetail(
        creatorDetailDto = CreatorDetailDto(
            about = "",
            genre = "",
            subscribersText = "",
            creatorChannelCategory = ""
        )
    )
}