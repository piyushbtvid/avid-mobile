package com.faithForward.media.ui.sections.my_account

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenu
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenuItemType
import com.faithForward.media.ui.sections.my_account.profile_menu.UserInfoItemDto
import com.faithForward.media.ui.sections.my_account.setting.Setting
import com.faithForward.media.viewModel.MyAccountViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.MyAccountEvent

@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
    onBackClick: () -> Unit,
    onItemClick: (WatchSectionItemDto, isFromContinueWatching: Boolean) -> Unit,
    myAccountViewModel: MyAccountViewModel,
) {

    val myAccountUiState = myAccountViewModel.uiState.collectAsState()
    var profileFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var profileSelectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var continueWatchingLastFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var myListLastFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    var selectedMenuItemType by rememberSaveable {
        mutableStateOf(ProfileMenuItemType.CONTINUE_WATCHING)
    }

    val sideBarState by sideBarViewModel.sideBarState



    BackHandler {
        Log.e("ON_BACK", "on back in search called")
        if (sideBarState.sideBarFocusedIndex != -1) {
            Log.e(
                "ON_BACK",
                "on back in home called with side Bar focused index ${sideBarState.sideBarFocusedIndex}"
            )
            onBackClick.invoke()
        } else {
            sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(6))
        }
    }

    LaunchedEffect(Unit) {

        Log.e("SIDE_BAR_ITEM", "my account is opend")
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
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ProfileMenu(focusedIndex = profileFocusedIndex,
            userInfoItemDto = UserInfoItemDto(
                userName = myAccountUiState.value.currentUserName,
                userEmail = myAccountUiState.value.currentUserEmail,
                initialName = myAccountUiState.value.initialName
            ),
            onFocusedIndexChange = { int ->
                profileFocusedIndex = int
                // making side bar focusable again when focus comes in profile menu item
                if (int != -1) {
                    sideBarViewModel.onEvent(SideBarEvent.ChangeFocusState(true))
                }
            },
            profileMenuItemDtoLists = myAccountUiState.value.profileMenuItemList,
            selectedIndex = profileSelectedIndex,
            onSelectedPositionChange = { int ->
                profileSelectedIndex = int
            },
            onItemClick = { menuItemType ->

                selectedMenuItemType = menuItemType

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

        when (selectedMenuItemType) {
            ProfileMenuItemType.CONTINUE_WATCHING -> {
                myAccountUiState.value.continueWatchSections?.items?.let {
                    WatchableGridSection(
                        watchSectionUiModel = myAccountUiState.value.continueWatchSections!!,
                        onItemClick = { item -> onItemClick(item, true) },
                        lastFocusedIndex = continueWatchingLastFocusedIndex,
                        onLastFocusedIndexChange = { int ->
                            continueWatchingLastFocusedIndex = int
                        }
                    )
                }
            }

            ProfileMenuItemType.MY_LIST -> {
                myAccountUiState.value.myListWatchSections?.items?.let {
                    WatchableGridSection(
                        watchSectionUiModel = myAccountUiState.value.myListWatchSections!!,
                        onItemClick = { item -> onItemClick(item, false) },
                        lastFocusedIndex = myListLastFocusedIndex,
                        onLastFocusedIndexChange = { int ->
                            myListLastFocusedIndex = int
                        }
                    )
                }
            }

            ProfileMenuItemType.SETTING -> {
                myAccountUiState.value.settingDto?.let {
                    Setting(
                        modifier = Modifier.padding(start = 16.dp),
                        settingItemDto = it
                    )
                }
            }
        }

    }


}