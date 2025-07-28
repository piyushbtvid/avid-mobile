package com.faithForward.media.ui.sections.my_account.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain

data class SettingDto(
    val email: String,
    val passwordTxt: String,
    val phoneNumber: String,
    val language: String,
    val autoPlay: String,
    val notification: String,
    val subtitles: String,

    )

@Composable
fun Setting(
    modifier: Modifier = Modifier,
    settingItemDto: SettingDto,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        with(settingItemDto) {

            AccountSection(
                email = email,
                passwordTxt = passwordTxt,
                phoneNumber = phoneNumber,
            )

            Divider(
                color = whiteMain.copy(
                    alpha = 0.5f
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )


            Preferences(
                language = language,
                autoPlay = autoPlay,
                notification = notification,
                subtitles = subtitles,
            )


            Divider(
                color = whiteMain.copy(
                    alpha = 0.5f
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )

        }

    }

}

@Composable
fun AccountSection(
    modifier: Modifier = Modifier,
    email: String,
    passwordTxt: String,
    phoneNumber: String,
) {

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        TitleText(
            text = "Account",
            textSize = 17,
            lineHeight = 17,
            color = whiteMain,
            fontWeight = FontWeight.ExtraBold
        )

        TextWithTitle(
            displayText = email,
            titleText = "Email:"
        )

        TextWithTitle(
            displayText = passwordTxt,
            titleText = "Password:"
        )

        TextWithTitle(
            displayText = phoneNumber,
            titleText = "Phone Number"
        )

    }

}


@Composable
fun Preferences(
    modifier: Modifier = Modifier,
    language: String,
    autoPlay: String,
    notification: String,
    subtitles: String,
) {

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        TitleText(
            text = "Preferences",
            textSize = 17,
            lineHeight = 17,
            color = whiteMain,
            fontWeight = FontWeight.ExtraBold
        )

        TextWithTitle(
            displayText = language,
            titleText = "Language"
        )

        TextWithTitle(
            displayText = autoPlay,
            titleText = "AutoPlay"
        )

        TextWithTitle(
            displayText = notification,
            titleText = "Notification"
        )

        TextWithTitle(
            displayText = subtitles,
            titleText = "Subtitles"
        )

    }


}