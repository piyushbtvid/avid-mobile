package com.faithForward.media.ui.login.qr

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.SubscribeButton
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.Montserrat
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.ui.epg.util.FocusState

@Composable
fun ActivationCode(
    modifier: Modifier = Modifier,
    code: String,
    url: String,
    expireTime: String,
    onLoginPageOpenClick: () -> Unit,
) {

    var isButtonFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

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
            letterSpacing = 3.sp,
            fontWeight = FontWeight.ExtraBold,
            color = focusedMainColor
        )

        ExpireTimeRow(
            time = expireTime
        )


        SubscribeButton(
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isButtonFocused = it.hasFocus
                }
                .focusable(),
            focusState = if (isButtonFocused) FocusState.FOCUSED else FocusState.UNFOCUSED,
            rounded = 8,
            buttonText = "Sign in directly on this device",
            onCategoryItemClick = {
                onLoginPageOpenClick.invoke()
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

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {

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
        onLoginPageOpenClick = {

        }
    )
}