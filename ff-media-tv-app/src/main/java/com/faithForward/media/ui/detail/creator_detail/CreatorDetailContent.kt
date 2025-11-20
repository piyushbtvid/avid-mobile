package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.SubscribeButton
import com.faithForward.media.ui.detail.creator_detail.content.ContentDto
import com.faithForward.media.ui.detail.creator_detail.content.ContentPage
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.ui.detail.creator_detail.ContentCard

@Composable
fun CreatorDetailContent(
    modifier: Modifier = Modifier,
    contentDtoList: List<ContentDto>,
    onCreatorContentClick: (ContentDto) -> Unit,
    contentRowFocusedIndex: Int,
    onContentRowFocusChange: (Int) -> Unit,
    onLastRowFocusedIndexChange: (Int) -> Unit,
    focusRequesters: List<FocusRequester>,
    isMobile: Boolean = false,
    creatorDetailDto: CreatorDetailDto? = null,
    onSubscribeClick: (() -> Unit)? = null,
    onAccessPremiumClick: (() -> Unit)? = null
) {
    if (isMobile) {
        // Mobile layout - Single LazyColumn with ALL content
        MobileFullContentPage(
            modifier = modifier.fillMaxSize(),
            creatorDetailDto = creatorDetailDto,
            contentDtoList = contentDtoList,
            onCreatorContentClick = onCreatorContentClick,
            onSubscribeClick = onSubscribeClick,
            onAccessPremiumClick = onAccessPremiumClick
        )
    } else {
        // TV layout - use original ContentPage with LazyColumn
        ContentPage(
            modifier = modifier
                .padding(top = 67.dp),
            contentDtoList = contentDtoList,
            onCreatorContentClick = onCreatorContentClick,
            contentRowFocusedIndex = contentRowFocusedIndex,
            onContentRowFocusChange = onContentRowFocusChange,
            onLastRowFocusedIndexChange = onLastRowFocusedIndexChange,
            focusRequesters = focusRequesters
        )
    }
}

@Preview
@Composable
private fun CreatorDetailContentPreview() {
    CreatorDetailContent(
        contentDtoList = listOf(
            ContentDto(
                slug = "test-1",
                image = "https://example.com/thumbnail1.jpg",
                title = "Test Content",
                views = "100K views",
                duration = "10:30",
                description = "Test description",
                time = "1 day ago"
            )
        ),
        onCreatorContentClick = {},
        contentRowFocusedIndex = -1,
        onContentRowFocusChange = {},
        onLastRowFocusedIndexChange = {},
        focusRequesters = listOf(),
        isMobile = false
    )
}

@Composable
private fun MobileFullContentPage(
    modifier: Modifier = Modifier,
    creatorDetailDto: CreatorDetailDto?,
    contentDtoList: List<ContentDto>,
    onCreatorContentClick: (ContentDto) -> Unit,
    onSubscribeClick: (() -> Unit)?,
    onAccessPremiumClick: (() -> Unit)?
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Full-width background image for mobile
        item {
            MobileAnchoredImage(
                creatorName = creatorDetailDto?.creatorName ?: "",
                creatorImageUrl = creatorDetailDto?.creatorImageUrl ?: "",
                creatorBackgroundImageUrl = creatorDetailDto?.creatorBackgroundImageUrl ?: ""
            )
        }

        // Creator details below the image
        if (creatorDetailDto?.creatorContentDto != null) {
            item {
                CreatorDetail(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 60.dp),
                    creatorDetailDto = creatorDetailDto.creatorContentDto
                )
            }
        }

        // Action buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SubscribeButton(
                    modifier = Modifier.weight(1f),
                    focusState = com.faithForward.media.util.FocusState.UNFOCUSED,
                    buttonText = "Subscribe",
                    onCategoryItemClick = { onSubscribeClick?.invoke() }
                )
                SubscribeButton(
                    modifier = Modifier.weight(1f),
                    focusState = com.faithForward.media.util.FocusState.UNFOCUSED,
                    buttonText = "Access Premium",
                    icon = com.faithForward.media.R.drawable.lock,
                    onCategoryItemClick = { onAccessPremiumClick?.invoke() }
                )
            }
        }

        // Content section header
        if (contentDtoList.isNotEmpty()) {
            item {
                TitleText(
                    text = "Content",
                    color = whiteMain,
                    textSize = 20,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        // Content list
        itemsIndexed(contentDtoList) { index, item ->
            ContentCard(
                contentDto = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onCreatorContentClick(item) }
            )
        }
    }
}
