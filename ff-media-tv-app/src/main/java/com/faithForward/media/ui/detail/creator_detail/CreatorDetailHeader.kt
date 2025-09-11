package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import com.faithForward.media.ui.commanComponents.RoundedIconButton
import com.faithForward.media.util.extensions.shadow
import com.faithForward.media.ui.theme.textFocusedMainColor

@Composable
fun CreatorDetailHeader(
    modifier: Modifier = Modifier,
    isMicFocused: Boolean,
    isSearchFocused: Boolean,
    onMicFocusChanged: (Boolean) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0f)
        ) {
            RoundedIconButton(
                modifier = Modifier
                    .onFocusChanged {
                        onMicFocusChanged(it.hasFocus)
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
                        onSearchFocusChanged(it.hasFocus)
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
}

@Preview
@Composable
private fun CreatorDetailHeaderPreview() {
    CreatorDetailHeader(
        isMicFocused = false,
        isSearchFocused = false,
        onMicFocusChanged = {},
        onSearchFocusChanged = {}
    )
}

