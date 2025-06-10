package com.faithForward.media.home.creator.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.theme.cardShadowColor
import com.faithForward.media.theme.creatorTitleStyle
import com.faithForward.media.theme.tv_02


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
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier,
                    text = creatorChannelCategory,
                    maxLines = 1,
                    style = tv_02
                )
                Box(
                    modifier = Modifier
                        .padding(start = 7.5.dp)
                        .clip(shape = RoundedCornerShape(2.5.dp))
                        .size(5.dp)
                        .background(color = cardShadowColor)
                )
                Text(
                    modifier = Modifier
                        .padding(start = 7.5.dp),
                    text = genre,
                    maxLines = 1,
                    style = tv_02
                )
                Text(
                    modifier = Modifier
                        .padding(start = 40.dp),
                    text = subscribersText,
                    maxLines = 1,
                    style = tv_02)
            }
            Text(
                modifier = Modifier,
                text = about,
                style = tv_02,
                textAlign = TextAlign.Start
            )

        }
    }
}

@Preview
@Composable
private fun CreatorDetailPreview() {
    CreatorDetail(
        creatorDetailDto = CreatorDetailDto(
            about = "About the Creator: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor.",
            genre = "Comedy",
            subscribersText = "130K Subscribers",
            creatorChannelCategory = "Reality"
        )
    )
}