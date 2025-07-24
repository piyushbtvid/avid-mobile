package com.faithForward.media.ui.sections.my_account.profile_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.myAccountItemBackground
import com.faithForward.media.ui.theme.pillButtonTextUnFocusColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState

data class ProfileMenuItemDto(
    val name: String,
    val icon: Int,
)

@Composable
fun ProfileMenuItem(
    modifier: Modifier = Modifier,
    profileMenuItemDto: ProfileMenuItemDto,
    focusState: FocusState,
) {

    Row(
        modifier = Modifier
            .width(180.dp)
            .height(55.dp)
            .background(myAccountItemBackground),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(8.dp)
                .background(
                    color = if (focusState == FocusState.FOCUSED) focusedMainColor else whiteMain
                )
        )


        Image(
            painter = painterResource(profileMenuItemDto.icon),
            contentDescription = null
        )

        TitleText(
            text = profileMenuItemDto.name,
            textSize = 14,
            lineHeight = 14,
            color = whiteMain
        )

    }

}


@Preview
@Composable
private fun ProfileMenuItemPreview() {
    ProfileMenuItem(
        profileMenuItemDto = ProfileMenuItemDto(
            name = "Continue Watching",
            icon = R.drawable.plus_ic
        ),
        focusState = FocusState.FOCUSED
    )
}