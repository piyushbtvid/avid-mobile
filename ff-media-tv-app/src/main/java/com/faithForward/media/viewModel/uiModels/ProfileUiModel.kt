package com.faithForward.media.viewModel.uiModels

import com.faithForward.media.ui.user_profile.UserProfileUiItem
import com.faithForward.network.dto.profile.AvatarItem
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

fun AvatarItem.toAvatarUiItem(): com.faithForward.media.ui.user_profile.create_profile.AvatarItem {
    return com.faithForward.media.ui.user_profile.create_profile.AvatarItem(
        imgSrc = image_url,
        id = id
    )
}

sealed class ProfileEvent {
    data object GetAllProfiles : ProfileEvent()
    data object GetAllAvatars : ProfileEvent()
    data class CreateProfile(
        val avatarId : Int,
        val name : String,
    ) : ProfileEvent()
}
