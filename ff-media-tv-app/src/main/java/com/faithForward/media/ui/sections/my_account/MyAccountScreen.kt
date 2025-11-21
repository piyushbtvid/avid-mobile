package com.faithForward.media.ui.sections.my_account

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.util.Util.isTvDevice
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.sections.my_account.mobile.AccountSettingsMobile
import com.faithForward.media.ui.sections.my_account.mobile.ContinueWatchingRowMobile
import com.faithForward.media.ui.sections.my_account.mobile.MyListRowMobile
import com.faithForward.media.ui.sections.my_account.profile_menu.UserInfoItem
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenu
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenuItemType
import com.faithForward.media.ui.sections.my_account.profile_menu.UserInfoItemDto
import com.faithForward.media.ui.sections.my_account.setting.Setting
import com.faithForward.media.ui.sections.my_account.subscription.SubscriptionPlan
import com.faithForward.media.viewModel.MyAccountViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.uiModels.MyAccountEvent
import com.faithForward.media.viewModel.uiModels.toPosterCardDto

@Composable
fun MyAccountScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
    onBackClick: () -> Unit,
    onItemClick: (WatchSectionItemDto, isFromContinueWatching: Boolean) -> Unit,
    myAccountViewModel: MyAccountViewModel,
    onSwitchProfile : () -> Unit,
    onLogoutRequest: () -> Unit
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


    val context = LocalContext.current

    BackHandler {
        Log.e("ON_BACK", "on back in search called")
        if (sideBarState.sideBarFocusedIndex != -1) {
            Log.e(
                "ON_BACK",
                "on back in home called with side Bar focused index ${sideBarState.sideBarFocusedIndex}"
            )
            onBackClick.invoke()
        } else {
            // For TV devices, My Account is at index 6 (after Search, Home, MyList, Creators, Series, Movies)
            // For mobile devices, My Account is at index 5 (after Home, MyList, Creators, Series, Movies)
            val myAccountIndex = if (context.isTvDevice()) 6 else 5
            sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(myAccountIndex))
        }
    }

    LaunchedEffect(Unit) {

        Log.e("SIDE_BAR_ITEM", "my account is opend")
        if (context.isTvDevice()) {
            // TV: load based on selected menu item
            if (profileSelectedIndex == 0) {
                myAccountViewModel.onEvent(MyAccountEvent.GetContinueWatching)
            } else {
                myAccountViewModel.onEvent(MyAccountEvent.GetMyList)
            }
        } else {
            // Mobile: load both datasets for the two rows
            myAccountViewModel.onEvent(MyAccountEvent.GetContinueWatching)
            myAccountViewModel.onEvent(MyAccountEvent.GetMyList)
        }
    }

    if (context.isTvDevice()) {
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

                        ProfileMenuItemType.SUBSCRIPTION -> {
                            myAccountViewModel.onEvent(MyAccountEvent.GetSubscription)
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
                            settingItemDto = it,
                            onSwitchProfile = onSwitchProfile
                        )
                    }
                }

                ProfileMenuItemType.SUBSCRIPTION -> {
                    SubscriptionPlan(
                        subscription = myAccountUiState.value.subscription,
                        isLoadingSubscription = myAccountUiState.value.isLoadingSubscription
                    )
                }
            }

        }
    } else {
        // Mobile layout: two horizontal rows (Continue Watching, My List) and scrollable Settings below
        val listState = rememberLazyListState()
        val focusRequesters = mutableMapOf<Pair<Int, Int>, androidx.compose.ui.focus.FocusRequester>()
        var lastFocusedItem by rememberSaveable { mutableStateOf(Pair(0, 0)) }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 0.dp, end = 0.dp, top = 16.dp, bottom = 16.dp),
            state = listState,
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // User Info at top
            item {
                UserInfoItem(
                    modifier = Modifier.padding(start = 20.dp, end = 16.dp, bottom = 12.dp),
                    userInfoItemDto = UserInfoItemDto(
                        userName = myAccountUiState.value.currentUserName,
                        userEmail = myAccountUiState.value.currentUserEmail,
                        initialName = myAccountUiState.value.initialName
                    )
                )
            }
            // Continue Watching Row
            item {
                myAccountUiState.value.continueWatchSections?.let { section ->
                    ContinueWatchingRowMobile(
                        section = section,
                        onItemClick = { original -> onItemClick(original, true) }
                    )
                }
            }

            // My List Row
            item {
                myAccountUiState.value.myListWatchSections?.let { section ->
                    MyListRowMobile(
                        section = section,
                        onItemClick = { original -> onItemClick(original, false) }
                    )
                }
            }

            // Settings Section
            item {
                Log.e("SETTINGS_DEBUG", "settingDto is: ${myAccountUiState.value.settingDto}")
                // Always show settings for debugging - use static data if needed
                val settingsData = myAccountUiState.value.settingDto ?: com.faithForward.media.viewModel.uiModels.settingItemDto
                Log.e("SETTINGS_DEBUG", "Using settings data: $settingsData")
                AccountSettingsMobile(
                    setting = settingsData,
                    onSwitchProfile = onSwitchProfile,
                    onLogout = {
                        // Show logout confirmation dialog first, same as TV
                        onLogoutRequest()
                    }
                )
            }
        }
    }
}