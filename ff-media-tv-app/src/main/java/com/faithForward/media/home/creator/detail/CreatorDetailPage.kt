package com.faithForward.media.home.creator.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.commanComponents.SubscribeButton
import com.faithForward.media.util.FocusState


@Composable
fun CreatorDetailPage(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Column {
            AnchoredImage(
                creatorName = "kdjfkdjf",
                creatorImageUrl = "",
                creatorBackgroundImageUrl = ""
            )
            CreatorDetail(
                modifier = Modifier.padding(start = 110.dp, top = 6.dp),
                creatorDetailDto = CreatorDetailDto(
                    about = "About the Creator: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor.",
                    genre = "Comedy",
                    subscribersText = "130K Subscribers",
                    creatorChannelCategory = "Reality"
                )
            )

            Row(
                modifier = Modifier.padding(start = 110.dp, top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SubscribeButton(
                    focusState = FocusState.UNFOCUSED,
                    buttonText = "Subscribe",
                    onCategoryItemClick = {

                    })
                SubscribeButton(
                    focusState = FocusState.UNFOCUSED,
                    buttonText = "Access Premium",
                    icon = R.drawable.lock,
                    onCategoryItemClick = {

                    })
            }

        }
    }
}

@Preview
@Composable
private fun CreatorDetailPagePreview() {
    CreatorDetailPage()
}