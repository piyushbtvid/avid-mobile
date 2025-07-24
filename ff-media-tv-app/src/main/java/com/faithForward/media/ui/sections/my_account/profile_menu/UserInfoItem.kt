package com.faithForward.media.ui.sections.my_account.profile_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.lightPink
import com.faithForward.media.ui.theme.whiteMain

data class UserInfoItemDto(
    val userEmail: String,
    val userName: String,
    val initialName: String,
)

@Composable
fun UserInfoItem(
    modifier: Modifier = Modifier,
    userInfoItemDto: UserInfoItemDto,
) {

    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {


        InitialNameItem(
            name = userInfoItemDto.initialName
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            TitleText(
                text = userInfoItemDto.userName,
                textSize = 15,
                lineHeight = 15,
                fontWeight = FontWeight.Bold,
                color = whiteMain
            )

            TitleText(
                text = userInfoItemDto.userEmail,
                textSize = 12,
                lineHeight = 12,
                fontWeight = FontWeight.Normal,
                color = whiteMain
            )


        }

    }

}


@Composable
fun InitialNameItem(
    modifier: Modifier = Modifier,
    name: String,
) {


    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(
                shape = RoundedCornerShape(30.dp)
            )
            .background(color = lightPink),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = name,
            fontSize = 15.sp,
            color = focusedMainColor
        )

    }
}


@Preview
@Composable
private fun InitialNamePreview() {
    InitialNameItem(name = "AP")
}


@Preview
@Composable
private fun UserInfoItemPreview() {
    UserInfoItem(
        userInfoItemDto = UserInfoItemDto(
            userName = "Amit",
            userEmail = "Subscriber1@gmail.com",
            initialName = "AP"
        )
    )
}
