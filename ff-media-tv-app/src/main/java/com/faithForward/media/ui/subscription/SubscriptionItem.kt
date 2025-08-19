package com.faithForward.media.ui.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.theme.blackColor
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain

data class SubscriptionUiItem(
    val amount: String,
    val time: String,
    val headLineText: String,
    val featureTextList: List<String>,
    val subHeadLineText: String,
)

@Composable
fun SubscriptionItem(
    modifier: Modifier = Modifier,
    subscriptionUiItem: SubscriptionUiItem,
    buttonText: String,
    focusRequester: FocusRequester = FocusRequester(),
) {

    var isButtonFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .width(340.dp)
            .border(
                width = 2.dp,
                color = if (isButtonFocused) focusedMainColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color(0xFF0D0B25),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 30.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = subscriptionUiItem.headLineText,
            style = TextStyle(
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        )



        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = subscriptionUiItem.amount,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = subscriptionUiItem.time,
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            )

        }





        Text(
            text = subscriptionUiItem.subHeadLineText,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp
            ),
            textAlign = TextAlign.Center
        )



        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            subscriptionUiItem.featureTextList.forEach { feature ->
                FeatureItem(feature)
            }
        }




        Button(
            onClick = { /* Handle click */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isButtonFocused) focusedMainColor else whiteMain.copy(alpha = 0.9f)
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.Start)
                .focusRequester(focusRequester = focusRequester)
                .onFocusChanged {
                    isButtonFocused = it.hasFocus
                }
                .focusable()
        ) {
            Text(
                text = buttonText,
                color = if (isButtonFocused) whiteMain else blackColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null,
            tint = focusedMainColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}


@Preview
@Composable
private fun SubscriptionItemPreview() {
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
    )
}
