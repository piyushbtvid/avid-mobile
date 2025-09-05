package com.faithForward.media.ui.sections.my_account.profile_menu

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.util.FocusState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileMenu(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    selectedIndex: Int,
    userInfoItemDto: UserInfoItemDto,
    onFocusedIndexChange: (Int) -> Unit,
    onSelectedPositionChange: (Int) -> Unit,
    onItemClick: (ProfileMenuItemType) -> Unit,
    profileMenuItemDtoLists: List<ProfileMenuItemDto>,
) {
    val focusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier.focusRestorer {
            focusRequester
        },
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {
            UserInfoItem(
                userInfoItemDto = userInfoItemDto
            )
        }

        itemsIndexed(profileMenuItemDtoLists) { index, item ->

            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                selectedIndex -> FocusState.SELECTED
                else -> FocusState.UNFOCUSED
            }

            ProfileMenuItem(
                modifier = Modifier
                    .focusRequester(
                        if (index == 0) focusRequester else FocusRequester()
                    )
                    .onFocusChanged {
                        if (it.isFocused) {
                            onFocusedIndexChange.invoke(index)
                        } else {
                            onFocusedIndexChange.invoke(-1)
                        }
                    }
                    .focusable()
                    .clickable(interactionSource = null, indication = null, onClick = {
                        onSelectedPositionChange.invoke(index)
                        onFocusedIndexChange.invoke(-1)
                        onItemClick.invoke(item.menuType)
                    }),
                profileMenuItemDto = item,
                focusState = uiState
            )

        }

    }
}


@Preview
@Composable
private fun ProfileMenuPreview() {

    val userInfo = UserInfoItemDto(
        userName = "Amit",
        userEmail = "Subscriber1@gmail.com",
        initialName = "AP"
    )

    val menuItemList = listOf(
        ProfileMenuItemDto(
            name = "Continue Watching",
            icon = R.drawable.plus_ic,
            menuType = ProfileMenuItemType.MY_LIST
        ),
        ProfileMenuItemDto(
            name = "Continue Watching",
            icon = R.drawable.plus_ic,
            menuType = ProfileMenuItemType.MY_LIST
        ),
        ProfileMenuItemDto(
            name = "Continue Watching",
            icon = R.drawable.plus_ic,
            menuType = ProfileMenuItemType.MY_LIST
        )
    )

    ProfileMenu(
        focusedIndex = 1,
        profileMenuItemDtoLists = menuItemList,
        userInfoItemDto = userInfo,
        selectedIndex = 0,
        onSelectedPositionChange = {

        },
        onFocusedIndexChange = {

        },
        onItemClick = {

        }
    )

}