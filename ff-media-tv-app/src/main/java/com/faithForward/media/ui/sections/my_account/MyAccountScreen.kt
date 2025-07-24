package com.faithForward.media.ui.sections.my_account

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.my_account.continue_watching.ContinueWatching
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenu
import com.faithForward.media.ui.sections.my_account.profile_menu.UserInfoItemDto
import com.faithForward.media.viewModel.MyAccountViewModel

@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier,
    myAccountViewModel: MyAccountViewModel,
) {

    val myAccountUiState = myAccountViewModel.uiState.collectAsState()
    var profileFocusedIndex by rememberSaveable { mutableStateOf(-1) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 83.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 20.dp
            )
    ) {

        ProfileMenu(
            focusedIndex = profileFocusedIndex,
            userInfoItemDto = UserInfoItemDto(
                userName = "Amit",
                userEmail = "Subscriber1@gmail.com",
                initialName = "AP"
            ),
            onFocusedIndexChange = { int ->
                profileFocusedIndex = int
            },
            profileMenuItemDtoList = myAccountUiState.value.profileMenuItemList,
        )

        if (myAccountUiState.value.myWatchSectionItemDtoList != null) {
            ContinueWatching(
                watchSectionItemDtoList = myAccountUiState.value.myWatchSectionItemDtoList!!,
            )
        }

    }


}