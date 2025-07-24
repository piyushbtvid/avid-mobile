package com.faithForward.media.ui.sections.my_account.profile_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.util.FocusState

@Composable
fun ProfileMenu(
    modifier: Modifier = Modifier,
    focusedIndex: Int,
    userInfoItemDto: UserInfoItemDto,
    onFocusedIndexChange: (Int) -> Unit,
    profileMenuItemDtoList: List<ProfileMenuItemDto>,
) {


    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        item {
            UserInfoItem(
                userInfoItemDto = userInfoItemDto
            )
        }

        itemsIndexed(profileMenuItemDtoList) { index, item ->

            val uiState = when (index) {
                focusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            ProfileMenuItem(
                modifier = Modifier
                    .onFocusChanged {
                        if (it.hasFocus) {
                            onFocusedIndexChange.invoke(index)
                        } else {
                            onFocusedIndexChange.invoke(-1)
                        }
                    }
                    .clickable(interactionSource = null, indication = null, onClick = {

                    }
                    )
                    .focusable(),
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
            icon = R.drawable.plus_ic
        ),
        ProfileMenuItemDto(
            name = "Continue Watching",
            icon = R.drawable.plus_ic
        ),
        ProfileMenuItemDto(
            name = "Continue Watching",
            icon = R.drawable.plus_ic
        )
    )

    ProfileMenu(
        focusedIndex = 1,
        profileMenuItemDtoList = menuItemList,
        userInfoItemDto = userInfo,
        onFocusedIndexChange = {

        }
    )

}