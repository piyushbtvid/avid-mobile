package com.faithForward.media.ui.user_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.viewModel.ProfileScreenViewModel
import com.faithForward.util.Resource

@Composable
fun AllProfileScreen(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel,
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
        if (userProfileResponse.data != null) {
            AllProfileScreenRow(
                userProfileList = userProfileResponse.data!!
            )
        }
    }


}