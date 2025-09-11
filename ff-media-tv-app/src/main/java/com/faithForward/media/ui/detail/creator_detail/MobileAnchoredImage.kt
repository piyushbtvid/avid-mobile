package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.LoadImage
import com.faithForward.media.ui.theme.creatorTitleStyle

@Composable
fun MobileAnchoredImage(
    modifier: Modifier = Modifier,
    creatorImageUrl: String,
    creatorBackgroundImageUrl: String,
    creatorName: String
) {
    Box(modifier = modifier.height(200.dp)) {
        // Full-width background image for mobile
        LoadImage(
            contentDescription = "Creator Background",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            imageResId = R.drawable.ic_launcher_background,
            imageUrl = creatorBackgroundImageUrl
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(y = 40.dp)
        ) {
            LoadImage(
                imageResId = R.drawable.ic_launcher_background,
                contentDescription = "Creator Avatar",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(40.dp)),
                imageUrl = creatorImageUrl
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .offset(y = 41.5.dp),
                text = creatorName,
                maxLines = 1,
                style = creatorTitleStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun MobileAnchoredImagePreview() {
    MobileAnchoredImage(
        creatorImageUrl = "https://example.com/creator-avatar.jpg",
        creatorBackgroundImageUrl = "https://example.com/creator-banner.jpg",
        creatorName = "Faith Forward Media"
    )
}

