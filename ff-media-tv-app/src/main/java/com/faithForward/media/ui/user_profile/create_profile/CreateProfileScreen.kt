package com.faithForward.media.ui.user_profile.create_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.viewModel.ProfileScreenViewModel
import com.faithForward.media.viewModel.uiModels.ProfileEvent
import com.faithForward.util.Resource

@Composable
fun CreateProfileScreen(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel,
) {

    LaunchedEffect(Unit) {
        profileScreenViewModel.onEvent(ProfileEvent.GetAllAvatars)
    }

    val allAvatarsResponse by profileScreenViewModel.allAvatars.collectAsState()

    if (allAvatarsResponse is Resource.Unspecified || allAvatarsResponse is Resource.Error) {
        return
    }

    val allAvatarsList = allAvatarsResponse.data ?: emptyList()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleText(
                text = "Create Profile",
                color = whiteMain,
                fontWeight = FontWeight.ExtraBold,
                textSize = 26,
                lineHeight = 26
            )


            SelectAvatarRow(
                avatarList = allAvatarsList
            )


        }


    }


}