package com.faithForward.media.ui.sections.my_account.subscription

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.CategoryCompose
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.detailNowTextStyle
import com.faithForward.media.ui.theme.detailNowUnFocusTextStyle
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState
import com.faithForward.network.dto.subscription.Subscription
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SubscriptionPlan(
    modifier: Modifier = Modifier,
    subscription: Subscription?,
    isLoadingSubscription: Boolean = false,
    onSubscribeNow: () -> Unit = {},
) {

    if (subscription != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 50.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TitleText(
                modifier = Modifier.padding(top = 40.dp),
                text = "Subscription Plan",
                textSize = 19,
                lineHeight = 19,
                color = whiteMain,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TitleText(
                        text = "Plan :",
                        textSize = 15,
                        lineHeight = 15,
                        color = whiteMain,
                        fontWeight = FontWeight.Bold
                    )
                    TitleText(
                        text = subscription.title ?: "N/A",
                        textSize = 15,
                        lineHeight = 15,
                        color = focusedMainColor,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TitleText(
                        text = "Expires on :",
                        textSize = 15,
                        lineHeight = 15,
                        color = whiteMain,
                        fontWeight = FontWeight.Bold
                    )
                    TitleText(
                        text = formatExpirationDate(subscription.expirationDate) ?: "N/A",
                        textSize = 15,
                        lineHeight = 15,
                        color = focusedMainColor,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TitleText(
                        text = "Status :",
                        textSize = 15,
                        lineHeight = 15,
                        color = whiteMain,
                        fontWeight = FontWeight.Bold
                    )
                    TitleText(
                        text = subscription.status ?: "N/A",
                        textSize = 15,
                        lineHeight = 15,
                        color = focusedMainColor,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    } else if (!isLoadingSubscription) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                TitleText(
                    text = "You have not active Subscription",
                    textSize = 19,
                    lineHeight = 19,
                    color = whiteMain,
                    fontWeight = FontWeight.Bold
                )

                SubscribeNowButton(
                    onSubscribeNow = onSubscribeNow
                )
            }
        }
    }

}

@Composable
private fun SubscribeNowButton(
    onSubscribeNow: () -> Unit
) {
    var isSubscribeFocused by rememberSaveable { mutableStateOf(false) }
    val subscribeFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            subscribeFocusRequester.requestFocus()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    CategoryCompose(
        modifier = Modifier
            .focusRequester(subscribeFocusRequester)
            .onFocusChanged {
                isSubscribeFocused = it.hasFocus
            }
            .focusable(),
        categoryComposeDto = CategoryComposeDto(btnText = "Subscribe Now", id = ""),
        backgroundFocusedColor = focusedMainColor,
        textFocusedStyle = detailNowTextStyle,
        backgroundUnFocusedColor = Color.White.copy(alpha = 0.35f),
        textUnFocusedStyle = detailNowUnFocusTextStyle,
        onCategoryItemClick = { id ->
            onSubscribeNow.invoke()
        },
        focusState = if (isSubscribeFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
    )
}

private fun formatExpirationDate(dateString: String?): String? {
    if (dateString.isNullOrBlank()) return null

    return try {
        // Try different common date formats
        val inputFormats = listOf(
            "yyyy-MM-dd",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            "dd MMM yyyy",
            "dd-MM-yyyy",
            "MM/dd/yyyy",
            "dd/MM/yyyy"
        )

        var parsedDate: Date? = null
        for (format in inputFormats) {
            try {
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                sdf.isLenient = false
                parsedDate = sdf.parse(dateString)
                if (parsedDate != null) break
            } catch (e: Exception) {
                // Continue to next format
            }
        }

        if (parsedDate != null) {
            // Format to readable date: "dd MMM yyyy" (e.g., "31 Jan 2024")
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            outputFormat.format(parsedDate)
        } else {
            dateString // Return original if parsing fails
        }
    } catch (e: Exception) {
        e.printStackTrace()
        dateString // Return original if formatting fails
    }
}

