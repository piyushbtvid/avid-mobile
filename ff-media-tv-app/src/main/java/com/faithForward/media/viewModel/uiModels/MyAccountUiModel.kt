package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.R
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenuItemDto
import com.faithForward.network.dto.ContentItem

val profileMenuItemDtoList = listOf(
    ProfileMenuItemDto(
        name = "Continue Watching",
        icon = R.drawable.menu_continue
    ),
    ProfileMenuItemDto(
        name = "My List",
        icon = R.drawable.add_new
    ), ProfileMenuItemDto(
        name = "Settings",
        icon = R.drawable.menu_settings
    )
)

sealed class MyAccountEvent {
    data object GetContinueWatching : MyAccountEvent()
    data object GetMyList : MyAccountEvent()
}

data class MyAccountUiState(
    val myWatchSectionItemDtoList: List<WatchSectionItemDto>? = null,
    val profileMenuItemList: List<ProfileMenuItemDto> = profileMenuItemDtoList,
)


fun ContentItem.toWatchSectionItem() : WatchSectionItemDto {

    return WatchSectionItemDto(
        contentType = content_type ?: "",
        id = id.toString(),
        contentSlug = slug ?: "",
        title = name ?: "",
        description = description ?: "",
        progress = progressSeconds ?: 0,
        duration = duration?.toLong() ?: 0,
        timeLeft = duration?.toString() ?: "",
        image = landscape ?: "",
    )

}