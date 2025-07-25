package com.faithForward.media.ui.sections.my_account

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenu
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenuItemType
import com.faithForward.media.ui.sections.my_account.profile_menu.UserInfoItemDto
import com.faithForward.media.viewModel.MyAccountViewModel
import com.faithForward.media.viewModel.uiModels.MyAccountEvent

@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier,
    onItemClick: (WatchSectionItemDto, isFromContinueWatching: Boolean) -> Unit,
    myAccountViewModel: MyAccountViewModel,
) {

    val myAccountUiState = myAccountViewModel.uiState.collectAsState()
    var profileFocusedIndex by rememberSaveable { mutableStateOf(-1) }
    var profileSelectedIndex by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(Unit) {

        if (profileSelectedIndex == 0) {
            myAccountViewModel.onEvent(MyAccountEvent.GetContinueWatching)
        } else {
            myAccountViewModel.onEvent(MyAccountEvent.GetMyList)
        }

    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 100.dp, end = 20.dp, top = 20.dp, bottom = 20.dp
            )
    ) {

        ProfileMenu(focusedIndex = profileFocusedIndex,
            userInfoItemDto = UserInfoItemDto(
                userName = "Amit", userEmail = "Subscriber1@gmail.com", initialName = "AP"
            ),
            onFocusedIndexChange = { int ->
                profileFocusedIndex = int
            },
            profileMenuItemDtoLists = myAccountUiState.value.profileMenuItemList,
            selectedIndex = profileSelectedIndex,
            onSelectedPositionChange = { int ->
                profileSelectedIndex = int
            },
            onItemClick = { menuItemType ->
                when (menuItemType) {

                    ProfileMenuItemType.MY_LIST -> {
                        myAccountViewModel.onEvent(MyAccountEvent.GetMyList)
                    }

                    ProfileMenuItemType.CONTINUE_WATCHING -> {
                        myAccountViewModel.onEvent(MyAccountEvent.GetContinueWatching)
                    }

                    ProfileMenuItemType.SETTING -> {

                    }

                }
            })

        if (myAccountUiState.value.continueWatchSections?.items != null) {
            WatchableGridSection(
                watchSectionUiModel = myAccountUiState.value.continueWatchSections!!,
                onItemClick = { item ->
                    onItemClick(
                        item, true
                    )
                }
            )
        }

        if (myAccountUiState.value.myListWatchSections?.items != null) {
            WatchableGridSection(
                watchSectionUiModel = myAccountUiState.value.myListWatchSections!!,
                onItemClick = { item ->
                    onItemClick(
                        item, false
                    )
                }
            )
        }

    }


}