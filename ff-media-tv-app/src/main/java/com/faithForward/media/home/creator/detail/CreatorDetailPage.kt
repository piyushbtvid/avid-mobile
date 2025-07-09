package com.faithForward.media.home.creator.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.commanComponents.RoundedIconButton
import com.faithForward.media.commanComponents.SubscribeButton
import com.faithForward.media.extensions.shadow
import com.faithForward.media.home.creator.detail.content.ContentDto
import com.faithForward.media.home.creator.detail.content.ContentPage
import com.faithForward.media.theme.textFocusedMainColor
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
    contentDtoList: List<ContentDto>,
    onCreatorContentClick: (ContentDto) -> Unit,
) {

    var isButtonFocused by remember { mutableStateOf(false) }
    var isMicFocused by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }

    val buttonModifier = if (isButtonFocused) {
        Modifier
            .shadow(
                color = Color.Black.copy(alpha = .30f),
                borderRadius = 40.dp,
                blurRadius = 7.dp,
                spread = 3.dp,
            )
            .border(
                width = 1.dp,
                color = textFocusedMainColor,
                shape = RoundedCornerShape(40.dp)
            )
    } else {
        Modifier
    }

    var isSubscriberFocused by remember { mutableStateOf(false) }
    var isAccessPremiumFocused by remember { mutableStateOf(false) }

    val subscriberButtonFocusRequester = remember { FocusRequester() }


    LaunchedEffect(Unit) {
        try {
            subscriberButtonFocusRequester.requestFocus()
        } catch (_: Exception) {

        }
    }

    Row(modifier = modifier) {
        Column(modifier = Modifier.weight(1.2f)) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
//                RoundedIconButton(
//                    modifier = buttonModifier
//                        .onFocusChanged { focusState ->
//                            isButtonFocused = focusState.isFocused
//                        }
//                        .focusable(enabled = false)
//                        .focusProperties {
//                            canFocus = false
//                        }
//                    ,
//                    imageId = R.drawable.fi_rr_arrow_left,
//                    iconHeight = 25,
//                    boxSize = 42,
//                    iconWidth = 25,
//                    backgroundColor = textFocusedMainColor.copy(alpha = 4.5f),
//                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0f)
                ) {
                    RoundedIconButton(
                        modifier = Modifier
                            .onFocusChanged {
                                isMicFocused = it.hasFocus
                            }
                            .focusable(enabled = false)
                            .focusProperties {
                                canFocus = false
                            }
                            .then(
                                if (isMicFocused) {
                                    Modifier
                                        .shadow(
                                            color = Color.White.copy(alpha = .11f),
                                            borderRadius = 40.dp,
                                            blurRadius = 7.dp,
                                            spread = 5.dp,
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = textFocusedMainColor,
                                            shape = RoundedCornerShape(40.dp)
                                        )
                                } else Modifier
                            ),
                        imageId = R.drawable.microphone_ic,
                        iconHeight = 15,
                        boxSize = 43,
                        iconWidth = 15,
                        backgroundColor = Color.White.copy(alpha = .75f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    RoundedIconButton(
                        modifier = Modifier
                            .onFocusChanged {
                                isSearchFocused = it.hasFocus
                            }
                            .focusable(enabled = false)
                            .focusProperties {
                                canFocus = false
                            }
                            .then(
                                if (isSearchFocused) {
                                    Modifier
                                        .shadow(
                                            color = Color.White.copy(alpha = .11f),
                                            borderRadius = 40.dp,
                                            blurRadius = 7.dp,
                                            spread = 5.dp,
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = textFocusedMainColor,
                                            shape = RoundedCornerShape(40.dp)
                                        )
                                } else Modifier
                            ),
                        imageId = R.drawable.search_ic,
                        iconHeight = 15,
                        boxSize = 43,
                        iconWidth = 15,
                        backgroundColor = Color.White.copy(alpha = .75f)
                    )
                }
            }



            AnchoredImage(
                modifier = Modifier.padding(top = 27.dp),
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
                    modifier = Modifier
                        .focusRequester(subscriberButtonFocusRequester)
                        .onFocusChanged {
                            isSubscriberFocused = it.hasFocus
                        }
                        .focusable(),
                    focusState = if (isSubscriberFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
                    buttonText = "Subscribe",
                    onCategoryItemClick = {

                    })
                SubscribeButton(
                    modifier = Modifier
                        .onFocusChanged {
                            isAccessPremiumFocused = it.hasFocus
                        }
                        .focusable(),
                    focusState = if (isAccessPremiumFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
                    buttonText = "Access Premium",
                    icon = R.drawable.lock,
                    onCategoryItemClick = {

                    })
            }

        }

        ContentPage(
            modifier = Modifier
                .weight(1f)
                .padding(top = 67.dp), // Take up right half roughly
            contentDtoList = contentDtoList,
            onCreatorContentClick = onCreatorContentClick
        )
    }
}


@Preview
@Composable
private fun CreatorDetailPagePreview() {
    // CreatorDetailPage()
}