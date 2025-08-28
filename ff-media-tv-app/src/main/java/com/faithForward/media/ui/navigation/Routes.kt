package com.faithForward.media.ui.navigation

import android.net.Uri
import com.faithForward.media.ui.commanComponents.PosterCardDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Routes(val route: String) {

    data object AllProfile : Routes("ALL_PROFILE_SCREEN")
    data object CreateProfile : Routes("CREATE_PROFILE_SCREEN")
    data object Home : Routes("HOME_SCREEN")
    data object Creator : Routes("CREATOR_SCREEN")
    data object Search : Routes("SEARCH_SCREEN")
    data object Login : Routes("LOGIN_SCREEN")
    data object LoginQr : Routes("LOGIN_QR_SCREEN")
    data object MyAccount : Routes("MY_ACCOUNT_SCREEN")
    data object Universal : Routes("UNIVERSAL_SCREEN")
    data object Live : Routes("LIVE_PAGE")
    data object EditProfile : Routes("EDIT_PROFILE_SCREEN")
    data class UpdateProfile(
        val avatarId: Int,
        val userName: String,
        val profileId: Int,
        val isDefaultProfile: Boolean,
    ) : Routes("update_profile/$avatarId/$userName/$profileId/$isDefaultProfile") {

        companion object {
            const val ROUTE_BASE = "update_profile"
            const val FULL_ROUTE =
                "$ROUTE_BASE/{avatarId}/{userName}/{profileId}/{isDefaultProfile}"
        }
    }


    object Movies {
        const val route = "movies/{contentType}"
        fun createRoute(contentType: String) = "movies/$contentType"
    }

    object Series {
        const val route = "series/{contentType}"
        fun createRoute(contentType: String) = "series/$contentType"
    }

    data object MyList : Routes("MY_LIST_SCREEN")
    data object GenreData : Routes("GENRE_DATA_SCREEN/{genreId}") {
        fun createRoute(genreId: String) = "GENRE_DATA_SCREEN/$genreId"
    }

    data object Detail : Routes("DETAIL_SCREEN/{itemId}") {
        fun createRoute(itemId: String) = "DETAIL_SCREEN/$itemId"
    }

    data object CREATOR_DETAIL : Routes("CREATOR_DETAIL_SCREEN/{creatorId}") {
        fun createRoute(creatorId: Int) = "CREATOR_DETAIL_SCREEN/$creatorId"
    }

    data object PlayerScreen {
        const val route =
            "playerScreen/{playerDtoList}?isContinueWatching={isContinueWatching}&initialIndex={initialIndex}&isFromMyAccount={isFromMyAccount}&isPlayTrailer={isPlayTrailer}"

        fun createRoute(
            playerDtoList: List<PosterCardDto>,
            isContinueWatching: Boolean = false,
            isFromMyAccount: Boolean = false,
            isPlayTrailer: Boolean = false,
            initialIndex: Int = 0,
        ): String {
            val json = Json.encodeToString(playerDtoList)
            return "playerScreen/${Uri.encode(json)}" +
                    "?isContinueWatching=$isContinueWatching" +
                    "&initialIndex=$initialIndex" +
                    "&isFromMyAccount=$isFromMyAccount" +
                    "&isPlayTrailer=$isPlayTrailer"
        }
    }

    data object Subscription {
        const val route =
            "SUBSCRIPTION_SCREEN/{playerDtoList}?isContinueWatching={isContinueWatching}&initialIndex={initialIndex}&isFromMyAccount={isFromMyAccount}&isPlayTrailer={isPlayTrailer}"

        fun createRoute(
            playerDtoList: List<PosterCardDto>,
            isContinueWatching: Boolean = false,
            isFromMyAccount: Boolean = false,
            isPlayTrailer: Boolean = false,
            initialIndex: Int = 0,
        ): String {
            val json = Json.encodeToString(playerDtoList)
            return "SUBSCRIPTION_SCREEN/${Uri.encode(json)}" +
                    "?isContinueWatching=$isContinueWatching" +
                    "&initialIndex=$initialIndex" +
                    "&isFromMyAccount=$isFromMyAccount" +
                    "&isPlayTrailer=$isPlayTrailer"
        }
    }

}