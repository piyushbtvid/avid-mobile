package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.layout.Box
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
fun AnchoredImage(
    modifier: Modifier = Modifier,
    creatorImageUrl: String,
    creatorBackgroundImageUrl: String,
    creatorName: String
) {
    Box(modifier = modifier.height(145.dp + 43.dp)) {
        Box(
            modifier = Modifier
        ) {
            LoadImage(
                contentDescription = "Large Image",
                modifier = Modifier
                    .size(width = 480.dp, height = 145.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .align(Alignment.TopStart),
                contentScale = ContentScale.Crop,
                imageResId = R.drawable.ic_launcher_background,
                imageUrl = creatorBackgroundImageUrl
            )

            LoadImage(
                imageResId = R.drawable.ic_launcher_background,
                contentDescription = "Small Image",
                modifier = Modifier
                    .padding(start = 13.dp)
                    .size(85.5.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = 43.dp)
                    .clip(shape = RoundedCornerShape(45.dp)),
                imageUrl = creatorImageUrl
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 110.dp)
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
private fun AnchoredImagePreview() {
    AnchoredImage(
        creatorImageUrl = "",
        creatorBackgroundImageUrl = "",
        creatorName = "Jasmine Wright"
    )
}