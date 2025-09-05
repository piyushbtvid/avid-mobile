package com.faithForward.media.ui.login.qr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun LoginSteps(
    modifier: Modifier = Modifier,
    url: String,
) {


    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        TitleText(
            text = "Link your device",
            textSize = 22,
            lineHeight = 22,
            fontWeight = FontWeight.ExtraBold,
            color = whiteMain
        )

        TitleText(
            text = "To start watching, follow these simple steps:",
            textSize = 12,
            lineHeight = 12,
            fontWeight = FontWeight.W400,
            color = whiteMain.copy(alpha = 0.7f)
        )

        Column(
            modifier = Modifier.padding(top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NumberCircle(number = "1")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    TitleText(
                        text = "Visit",
                        maxLine = 1,
                        textSize = 10,
                        lineHeight = 10,
                        color = whiteMain.copy(.8f)
                    )

                    TitleText(
                        text = url,
                        maxLine = 1,
                        textSize = 10,
                        lineHeight = 10,
                        color = focusedMainColor
                    )

                    TitleText(
                        text = "on your phone or computer",
                        maxLine = 1,
                        textSize = 10,
                        lineHeight = 10,
                        color = whiteMain.copy(.8f)
                    )

                }


            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NumberCircle(number = "2")

                TitleText(
                    text = "Sign in with your account or create a new one",
                    maxLine = 1,
                    textSize = 10,
                    lineHeight = 10,
                    color = whiteMain.copy(.8f)
                )

            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NumberCircle(number = "3")

                TitleText(
                    text = "Enter the code shown below",
                    maxLine = 1,
                    textSize = 10,
                    lineHeight = 10,
                    color = whiteMain.copy(.8f)
                )

            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                NumberCircle(number = "4")

                TitleText(
                    text = "Start enjoying unlimited Christian content!",
                    maxLine = 1,
                    textSize = 10,
                    lineHeight = 10,
                    color = whiteMain.copy(.8f)
                )

            }

        }

    }
}


@Preview
@Composable
private fun LoginStepsPreview() {

    LoginSteps(
        url = "http://107.180.208.127:3000/activate"
    )

}