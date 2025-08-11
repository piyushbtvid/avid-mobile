package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.sections.my_account.WatchSectionUiModel
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenuItemDto
import com.faithForward.media.ui.sections.my_account.profile_menu.ProfileMenuItemType
import com.faithForward.media.ui.sections.my_account.setting.SettingDto
import com.faithForward.network.dto.ContentItem

val profileMenuItemDtoList = listOf(
    ProfileMenuItemDto(
        name = "Continue Watching",
        icon = R.drawable.menu_continue,
        menuType = ProfileMenuItemType.CONTINUE_WATCHING
    ),
    ProfileMenuItemDto(
        name = "My List",
        icon = R.drawable.add_new,
        menuType = ProfileMenuItemType.MY_LIST
    ), ProfileMenuItemDto(
        name = "Settings",
        icon = R.drawable.menu_settings,
        menuType = ProfileMenuItemType.SETTING
    )
)

val settingItemDto: SettingDto = SettingDto(
    email = "abc@gmail.com",
    passwordTxt = "Last Changes 3 month ago",
    phoneNumber = "00000011111",
    language = "English",
    autoPlay = "play next episode automatically",
    notification = "Get notified about new releases",
    subtitles = "Show subtitles by default",
)

sealed class MyAccountEvent {
    data object GetContinueWatching : MyAccountEvent()
    data object GetMyList : MyAccountEvent()
    data object GetCurrentUser : MyAccountEvent()
}

data class MyAccountUiState(
    val continueWatchSections: WatchSectionUiModel? = null,
    val myListWatchSections: WatchSectionUiModel? = null,
    val profileMenuItemList: List<ProfileMenuItemDto> = profileMenuItemDtoList,
    val settingDto: SettingDto? = settingItemDto,
    val currentUserEmail: String = "",
    val currentUserName: String = "",
    val initialName : String = ""
)


fun ContentItem.toWatchSectionItem(): WatchSectionItemDto {
    val progress = progressSeconds ?: 0L
    val totalDuration = duration?.toLong() ?: 0L
    val remaining = (totalDuration - progress).coerceAtLeast(0L)


    val timeLeftFormatted = if (progress == 0L) {
        ""
    } else {
        val hours = remaining / 3600
        val minutes = (remaining % 3600) / 60
        val seconds = remaining % 60

        buildString {
            if (hours > 0) append("${hours} h ")
            if (minutes > 0) append("${minutes} m ")
            if (seconds > 0 || (hours == 0L && minutes == 0L)) append("${seconds} s")
            append(" left")
        }
    }
    return WatchSectionItemDto(
        contentType = content_type ?: "",
        id = id.toString(),
        contentSlug = slug ?: "",
        title = name ?: "",
        description = description ?: "",
        progress = progress,
        duration = totalDuration,
        timeLeft = timeLeftFormatted,
        image = landscape ?: "",
        seriesSlug = seriesSlug
    )
}

fun WatchSectionItemDto.toPosterCardDto(): PosterCardDto {
    return PosterCardDto(
        id = id,
        contentType = contentType,
        slug = contentSlug,
        posterImageSrc = image,
        landScapeImg = image,
        title = title,
        description = description,
        duration = duration.toString(),
        seriesSlug = seriesSlug,
        progress = progress,
    )
}
