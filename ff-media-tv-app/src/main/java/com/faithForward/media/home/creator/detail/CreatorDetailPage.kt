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

data class CreatorDetailDto(
    val creatorName: String? = null,
    val creatorImageUrl: String? = null,
    val creatorBackgroundImageUrl: String? = null,
    val creatorContentDto: CreatorContentDto? = null,
)

@Composable
fun CreatorDetailPage(
    modifier: Modifier = Modifier,
    creatorDetailDto: CreatorDetailDto,
) {
    Row(modifier = modifier) {
        Column {
            AnchoredImage(
                creatorName = creatorDetailDto.creatorName ?: "",
                creatorImageUrl = creatorDetailDto.creatorImageUrl ?: "",
                creatorBackgroundImageUrl = creatorDetailDto.creatorBackgroundImageUrl ?: ""
            )
            if (creatorDetailDto.creatorContentDto != null) {
                CreatorDetail(
                    modifier = Modifier.padding(start = 110.dp, top = 6.dp),
                    creatorDetailDto = creatorDetailDto.creatorContentDto
                )
            }

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
    // CreatorDetailPage()
}