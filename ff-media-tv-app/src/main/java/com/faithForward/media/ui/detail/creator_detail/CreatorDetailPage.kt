package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.detail.creator_detail.content.ContentDto
import com.faithForward.media.util.Util.isTvDevice

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
    contentDtoList: List<ContentDto>,
    onCreatorContentClick: (ContentDto) -> Unit,
) {
    // State management
    var isMicFocused by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }
    var isSubscriberFocused by remember { mutableStateOf(false) }
    var isAccessPremiumFocused by remember { mutableStateOf(false) }
    var contentRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var lastRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    // Focus requesters
    val subscriberButtonFocusRequester = remember { FocusRequester() }
    val contentFocusRequesters = remember(contentDtoList.size) {
        List(contentDtoList.size) { FocusRequester() }
    }

    // Focus management
    LaunchedEffect(Unit) {
        if (lastRowFocusedIndex > -1 && lastRowFocusedIndex < contentFocusRequesters.size) {
            try {
                contentFocusRequesters[lastRowFocusedIndex].requestFocus()
            } catch (_: Exception) {
            }
        } else {
            try {
                subscriberButtonFocusRequester.requestFocus()
            } catch (_: Exception) {
            }
        }
    }

    val isTv = LocalContext.current.isTvDevice()

    if (isTv) {
        // TV Layout - Original horizontal layout
        Row(modifier = modifier) {
            Column(modifier = Modifier.weight(1.2f)) {
                // Header with mic and search buttons
                CreatorDetailHeader(
                    isMicFocused = isMicFocused,
                    isSearchFocused = isSearchFocused,
                    onMicFocusChanged = { isMicFocused = it },
                    onSearchFocusChanged = { isSearchFocused = it }
                )

                // Anchored image
                AnchoredImage(
                    modifier = Modifier.padding(top = 27.dp),
                    creatorName = creatorDetailDto.creatorName ?: "",
                    creatorImageUrl = creatorDetailDto.creatorImageUrl ?: "",
                    creatorBackgroundImageUrl = creatorDetailDto.creatorBackgroundImageUrl ?: ""
                )

                // Creator details
                if (creatorDetailDto.creatorContentDto != null) {
                    CreatorDetail(
                        modifier = Modifier.padding(start = 110.dp, top = 6.dp),
                        creatorDetailDto = creatorDetailDto.creatorContentDto
                    )
                }

                // Action buttons
                CreatorDetailButtons(
                    modifier = Modifier.padding(start = 110.dp, top = 20.dp),
                    isSubscriberFocused = isSubscriberFocused,
                    isAccessPremiumFocused = isAccessPremiumFocused,
                    onSubscriberFocusChanged = { isSubscriberFocused = it },
                    onAccessPremiumFocusChanged = { isAccessPremiumFocused = it },
                    onSubscribeClick = { /* Handle subscribe */ },
                    onAccessPremiumClick = { /* Handle premium access */ },
                    subscriberButtonFocusRequester = subscriberButtonFocusRequester,
                    isMobile = false
                )
            }

            // Content list
            CreatorDetailContent(
                modifier = Modifier.weight(1f),
                contentDtoList = contentDtoList,
                onCreatorContentClick = onCreatorContentClick,
                contentRowFocusedIndex = contentRowFocusedIndex,
                onContentRowFocusChange = { contentRowFocusedIndex = it },
                onLastRowFocusedIndexChange = { lastRowFocusedIndex = it },
                focusRequesters = contentFocusRequesters,
                isMobile = false
            )
        }
    } else {
        // Mobile Layout - Single LazyColumn with ALL content
        CreatorDetailContent(
            modifier = modifier.fillMaxSize(),
            contentDtoList = contentDtoList,
            onCreatorContentClick = onCreatorContentClick,
            contentRowFocusedIndex = contentRowFocusedIndex,
            onContentRowFocusChange = { contentRowFocusedIndex = it },
            onLastRowFocusedIndexChange = { lastRowFocusedIndex = it },
            focusRequesters = contentFocusRequesters,
            isMobile = true,
            creatorDetailDto = creatorDetailDto,
            onSubscribeClick = { /* Handle subscribe */ },
            onAccessPremiumClick = { /* Handle premium access */ }
        )
    }
}

@Preview(device = "spec:width=1920px,height=1080px,dpi=320")
@Composable
private fun CreatorDetailPagePreview() {
    CreatorDetailPage(
        creatorDetailDto = CreatorDetailDto(
            creatorName = "Faith Forward Media",
            creatorImageUrl = "https://example.com/creator-avatar.jpg",
            creatorBackgroundImageUrl = "https://example.com/creator-banner.jpg",
            creatorContentDto = CreatorContentDto(
                about = "Faith Forward Media is a leading Christian content creator dedicated to spreading the Gospel through engaging videos, inspiring stories, and educational content. Our mission is to help people grow in their faith and discover the love of Christ.",
                description = "Join us on this journey of faith as we explore biblical teachings, share testimonies, and create content that uplifts and encourages believers worldwide.",
                subscribersText = "2.5M Subscribers",
                genre = "Christian Content",
                channelName = "Faith Forward Media",
                creatorChannelCategory = "Religion & Spirituality"
            )
        ),
        contentDtoList = listOf(
            ContentDto(
                slug = "daily-devotion-1",
                image = "https://example.com/thumbnail1.jpg",
                title = "Daily Devotion: Walking in Faith",
                views = "125K views",
                duration = "12:34",
                description = "Start your day with this powerful devotion about trusting God's plan for your life.",
                time = "2 days ago"
            ),
            ContentDto(
                slug = "testimony-story-1",
                image = "https://example.com/thumbnail2.jpg",
                title = "Miracle Testimony: From Broken to Blessed",
                views = "89K views",
                duration = "18:45",
                description = "Hear Sarah's incredible story of how God transformed her life from despair to hope.",
                time = "1 week ago"
            ),
            ContentDto(
                slug = "bible-study-1",
                image = "https://example.com/thumbnail3.jpg",
                title = "Bible Study: The Book of Romans Chapter 8",
                views = "156K views",
                duration = "25:12",
                description = "Deep dive into Romans 8 and discover the power of living in the Spirit.",
                time = "2 weeks ago"
            ),
            ContentDto(
                slug = "prayer-session-1",
                image = "https://example.com/thumbnail4.jpg",
                title = "Prayer Session: Healing and Restoration",
                views = "203K views",
                duration = "15:30",
                description = "Join us in prayer for healing, restoration, and God's miraculous intervention.",
                time = "3 weeks ago"
            ),
            ContentDto(
                slug = "worship-music-1",
                image = "https://example.com/thumbnail5.jpg",
                title = "Worship Music: Amazing Grace (Acoustic)",
                views = "312K views",
                duration = "4:22",
                description = "Beautiful acoustic rendition of the timeless hymn Amazing Grace.",
                time = "1 month ago"
            )
        ),
        onCreatorContentClick = { contentDto ->
            // Preview click handler - in real app this would navigate to content detail
        }
    )
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun CreatorDetailPageMobilePreview() {
    CreatorDetailPage(
        creatorDetailDto = CreatorDetailDto(
            creatorName = "Faith Forward Media",
            creatorImageUrl = "https://example.com/creator-avatar.jpg",
            creatorBackgroundImageUrl = "https://example.com/creator-banner.jpg",
            creatorContentDto = CreatorContentDto(
                about = "Faith Forward Media is a leading Christian content creator dedicated to spreading the Gospel through engaging videos, inspiring stories, and educational content. Our mission is to help people grow in their faith and discover the love of Christ.",
                description = "Join us on this journey of faith as we explore biblical teachings, share testimonies, and create content that uplifts and encourages believers worldwide.",
                subscribersText = "2.5M Subscribers",
                genre = "Christian Content",
                channelName = "Faith Forward Media",
                creatorChannelCategory = "Religion & Spirituality"
            )
        ),
        contentDtoList = listOf(
            ContentDto(
                slug = "daily-devotion-1",
                image = "https://example.com/thumbnail1.jpg",
                title = "Daily Devotion: Walking in Faith",
                views = "125K views",
                duration = "12:34",
                description = "Start your day with this powerful devotion about trusting God's plan for your life.",
                time = "2 days ago"
            ),
            ContentDto(
                slug = "testimony-story-1",
                image = "https://example.com/thumbnail2.jpg",
                title = "Miracle Testimony: From Broken to Blessed",
                views = "89K views",
                duration = "18:45",
                description = "Hear Sarah's incredible story of how God transformed her life from despair to hope.",
                time = "1 week ago"
            ),
            ContentDto(
                slug = "bible-study-1",
                image = "https://example.com/thumbnail3.jpg",
                title = "Bible Study: The Book of Romans Chapter 8",
                views = "156K views",
                duration = "25:12",
                description = "Deep dive into Romans 8 and discover the power of living in the Spirit.",
                time = "2 weeks ago"
            ),
            ContentDto(
                slug = "prayer-session-1",
                image = "https://example.com/thumbnail4.jpg",
                title = "Prayer Session: Healing and Restoration",
                views = "203K views",
                duration = "15:30",
                description = "Join us in prayer for healing, restoration, and God's miraculous intervention.",
                time = "3 weeks ago"
            ),
            ContentDto(
                slug = "worship-music-1",
                image = "https://example.com/thumbnail5.jpg",
                title = "Worship Music: Amazing Grace (Acoustic)",
                views = "312K views",
                duration = "4:22",
                description = "Beautiful acoustic rendition of the timeless hymn Amazing Grace.",
                time = "1 month ago"
            )
        ),
        onCreatorContentClick = { contentDto ->
            // Preview click handler - in real app this would navigate to content detail
        }
    )
}