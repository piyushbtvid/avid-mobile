package com.faithForward.media.ui.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.theme.focusedMainColor

data class SubscriptionUiItem(
    val amount : String
)

@Composable
fun SubscriptionItem(
    modifier: Modifier = Modifier,
    subscriptionUiItem: SubscriptionUiItem
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = focusedMainColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color(0xFF0D0B25), // dark background like in screenshot
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Monthly Plan",
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$9.99",
            style = TextStyle(
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "/month",
            style = TextStyle(
                color = Color.LightGray,
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Perfect for trying out premium features",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 12.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            FeatureItem("Ad-free streaming")
            FeatureItem("4K Ultra HD quality")
            FeatureItem("Download for offline viewing")
            FeatureItem("Stream on 4 devices")
            FeatureItem("Cancel anytime")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* Handle click */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = focusedMainColor
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Choose Monthly",
                color = Color.White,
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
        subscriptionUiItem = SubscriptionUiItem(amount = "2")
    )
}
