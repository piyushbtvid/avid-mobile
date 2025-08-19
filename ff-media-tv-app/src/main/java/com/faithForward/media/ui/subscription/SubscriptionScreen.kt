package com.faithForward.media.ui.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun SubscriptionScreen(
    modifier: Modifier = Modifier,
) {

    val buttonFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            buttonFocusRequester.requestFocus()
        } catch (_: Exception) {

        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor)
            .padding(top = 30.dp, bottom = 20.dp)
            .padding( horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleText(
            text = "Choose your premium plan",
            textSize = 28,
            lineHeight = 28,
            color = whiteMain,
            fontWeight = FontWeight.ExtraBold,
        )

        TitleText(
            modifier = Modifier.width(500.dp),
            text = "Unlock unlimited entertainment with premium features, exclusive content, and ad-free streaming",
            textSize = 13,
            lineHeight = 13,
            maxLine = 2,
            textAlign = TextAlign.Center,
            color = whiteMain.copy(alpha = 0.7f),
            fontWeight = FontWeight.W400,
        )


        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(35.dp)
        ) {

            SubscriptionItem(
                subscriptionUiItem = SubscriptionUiItem(
                    amount = "$9.99",
                    time = "/month",
                    headLineText = "Monthly Plan",
                    featureTextList = listOf(
                        "Ad-free streaming",
                        "4K Ultra HD quality",
                        "Download for offline viewing",
                        "Stream on 4 devices",
                        "Cancel anytime"
                    ),
                    subHeadLineText = "Perfect for trying out premium features",
                ),
                buttonText = "Choose Monthly",
                focusRequester = buttonFocusRequester
            )

            SubscriptionItem(
                subscriptionUiItem = SubscriptionUiItem(
                    amount = "$99.99",
                    time = "/year",
                    headLineText = "Yearly Plan",
                    featureTextList = listOf(
                        "Everything in Monthly Plan",
                        "Exclusive premium content",
                        "Early access to new releases",
                        "Priority customer support",
                        "Stream on 6 devices"
                    ),
                    subHeadLineText = "Best value for premium entertainment",
                ),
                buttonText = "Choose Yearly",
            )
        }
    }

}


@Preview(device = "id:tv_1080p")
@Composable
private fun SubscriptionUiPreview() {
    SubscriptionScreen()
}