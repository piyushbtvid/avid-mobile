package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.user_profile.UserProfileUiItem
import com.faithForward.network.dto.profile.Profile

fun Profile.toUserProfileUiItem(): UserProfileUiItem {

    return UserProfileUiItem(
        id = id,
        currentAvatarImg = avatar_img,
        avatarId = avatar_id,
        isDefault = is_default,
        name = name
    )

}