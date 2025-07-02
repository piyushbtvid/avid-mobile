package com.faithForward.media.login.qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.commanComponents.SubscribeButton
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun ActivationCode(
    modifier: Modifier = Modifier,
    code: String,
    url: String,
    expireTime: String,
) {


    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        TitleText(
            text = "Your activation code:",
            textSize = 14,
            lineHeight = 14,
            fontWeight = FontWeight.Bold,
            color = whiteMain.copy(alpha = 0.8f)
        )

        TitleText(
            text = code,
            textSize = 24,
            lineHeight = 24,
            fontWeight = FontWeight.ExtraBold,
            color = focusedMainColor
        )

        ExpireTimeRow(
            time = expireTime
        )


        SubscribeButton(
            focusState = FocusState.FOCUSED,
            rounded = 8,
            buttonText = "Sign in directly on this device",
            onCategoryItemClick = {

            },
            icon = R.drawable.baseline_exit_to_app_24
        )


        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            TitleText(
                text = "Need help? Visit",
                maxLine = 1,
                textSize = 9,
                lineHeight = 9,
                color = whiteMain.copy(.8f)
            )

            TitleText(
                text = url,
                maxLine = 1,
                textSize = 9,
                lineHeight = 9,
                color = focusedMainColor
            )

//            TitleText(
//                text = "or call",
//                maxLine = 1,
//                textSize = 9,
//                lineHeight = 9,
//                color = whiteMain.copy(.8f)
//            )

        }
    }

}


@Composable
fun ExpireTimeRow(
    modifier: Modifier = Modifier,
    time: String,
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.size(10.dp),
            painter = painterResource(R.drawable.watch_later),
            tint = whiteMain.copy(alpha = 0.6f),
            contentDescription = null
        )

        TitleText(
            text = "Code expires in",
            textSize = 9,
            lineHeight = 9,
            color = whiteMain.copy(alpha = 0.6f)
        )

        TitleText(
            text = time,
            textSize = 12,
            lineHeight = 12,
            color = focusedMainColor
        )
    }

}


//@Preview
//@Composable
//private fun TimeRowPreview() {
//    ExpireTimeRow(
//        time = "13:15"
//    )
//}


@Preview
@Composable
private fun ActivationCodePreview() {
    ActivationCode(
        code = "8VR7H",
        url = "http://107.180.208.127:3000/activate",
        expireTime = "15:30",
    )
}