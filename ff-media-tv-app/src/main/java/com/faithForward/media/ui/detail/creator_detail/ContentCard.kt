package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.ui.detail.creator_detail.content.ContentDto
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    contentDto: ContentDto,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(contentDto.image)
                .error(R.drawable.test_poster)
                .build(),
            contentDescription = "Content Image",
            modifier = Modifier
                .width(60.5.dp)
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Text content
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Title and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = contentDto.title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = whiteMain,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f) // Title takes available space but leaves room for duration
                )

                Spacer(modifier = Modifier.width(8.dp)) // Space between title and duration

                Text(
                    text = contentDto.duration,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                    color = whiteMain,
                    maxLines = 1,
                    overflow = TextOverflow.Visible // Ensure duration isn't clipped
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                text = contentDto.description,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal),
                color = whiteMain,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Views and upload time
            Text(
                text = "${contentDto.views} • ${contentDto.time}",
                style = MaterialTheme.typography.bodySmall,
                color = whiteMain
            )
        }
    }
}


@Preview
@Composable
private fun ContentCardPreview() {
    ContentCard(
        contentDto = ContentDto(
            image = "",
            title = "hbvkhbvslblsvdb",
            views = "100",
            duration = "30000",
            description = "kdskbk bdlhids",
            time = "lbslibv",
            slug = ""
        )
    )
}