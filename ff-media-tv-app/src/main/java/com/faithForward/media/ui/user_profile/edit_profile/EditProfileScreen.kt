package com.faithForward.media.ui.user_profile.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.ui.user_profile.AllProfileScreenRow
import com.faithForward.media.ui.user_profile.UserProfileUiItem
import com.faithForward.media.viewModel.ProfileScreenViewModel
import com.faithForward.util.Resource

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel,
    onItemClick: (UserProfileUiItem) -> Unit,
) {

    val userProfileResponse by profileScreenViewModel.allProfiles.collectAsState()


    if (userProfileResponse is Resource.Unspecified || userProfileResponse is Resource.Error || userProfileResponse is Resource.Loading) {
        return
    }


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
                text = "Edit Profile",
                color = whiteMain,
                fontWeight = FontWeight.ExtraBold,
                textSize = 26,
                lineHeight = 26
            )

            if (userProfileResponse.data != null) {
                AllProfileScreenRow(
                    userProfileList = userProfileResponse.data!!,
                    shouldShowAddProfileButton = false,
                    onItemClick = onItemClick,
                    onAddProfileClick = {

                    }
                )
            }


        }
    }


}