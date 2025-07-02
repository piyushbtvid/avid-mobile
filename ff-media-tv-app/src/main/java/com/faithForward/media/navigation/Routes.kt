package com.faithForward.media.navigation

import android.net.Uri
import com.faithForward.media.commanComponents.PosterCardDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Routes(val route: String) {

    data object Home : Routes("HOME_SCREEN")
    data object Creator : Routes("CREATOR_SCREEN")
    data object Search : Routes("SEARCH_SCREEN")
    data object Login : Routes("LOGIN_SCREEN")
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
        const val route = "playerScreen/{playerDtoList}?isContinueWatching={isContinueWatching}&initialIndex={initialIndex}"
        fun createRoute(
            playerDtoList: List<PosterCardDto>,
            isContinueWatching: Boolean = false,
            initialIndex: Int = 0
        ): String {
            val json = Json.encodeToString(playerDtoList)
            return "playerScreen/${Uri.encode(json)}" +
                    "?isContinueWatching=$isContinueWatching&initialIndex=$initialIndex"
        }

}

}