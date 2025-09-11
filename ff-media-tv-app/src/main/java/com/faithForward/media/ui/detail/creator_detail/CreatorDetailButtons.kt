package com.faithForward.media.ui.detail.creator_detail

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.SubscribeButton
import com.faithForward.media.util.FocusState

@Composable
fun CreatorDetailButtons(
    modifier: Modifier = Modifier,
    isSubscriberFocused: Boolean,
    isAccessPremiumFocused: Boolean,
    onSubscriberFocusChanged: (Boolean) -> Unit,
    onAccessPremiumFocusChanged: (Boolean) -> Unit,
    onSubscribeClick: () -> Unit,
    onAccessPremiumClick: () -> Unit,
    subscriberButtonFocusRequester: FocusRequester? = null,
    isMobile: Boolean = false
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SubscribeButton(
            modifier = if (isMobile) {
                Modifier.weight(1f)
            } else {
                Modifier
                    .let { mod ->
                        subscriberButtonFocusRequester?.let { requester ->
                            mod.focusRequester(requester)
                        } ?: mod
                    }
                    .onFocusChanged {
                        onSubscriberFocusChanged(it.hasFocus)
                    }
                    .focusable()
            },
            focusState = if (isMobile) FocusState.UNFOCUSED else {
                if (isSubscriberFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
            },
            buttonText = "Subscribe",
            onCategoryItemClick = onSubscribeClick
        )
        
        SubscribeButton(
            modifier = if (isMobile) {
                Modifier.weight(1f)
            } else {
                Modifier
                    .onFocusChanged {
                        onAccessPremiumFocusChanged(it.hasFocus)
                    }
                    .focusable()
            },
            focusState = if (isMobile) FocusState.UNFOCUSED else {
                if (isAccessPremiumFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
            },
            buttonText = "Access Premium",
            icon = R.drawable.lock,
            onCategoryItemClick = onAccessPremiumClick
        )
    }
}

@Preview
@Composable
private fun CreatorDetailButtonsPreview() {
    CreatorDetailButtons(
        isSubscriberFocused = false,
        isAccessPremiumFocused = false,
        onSubscriberFocusChanged = {},
        onAccessPremiumFocusChanged = {},
        onSubscribeClick = {},
        onAccessPremiumClick = {},
        isMobile = false
    )
}

