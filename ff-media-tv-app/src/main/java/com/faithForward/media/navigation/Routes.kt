package com.faithForward.media.navigation

import android.net.Uri
import com.faithForward.media.commanComponents.PosterCardDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Routes(val route: String) {

    data object Home : Routes("HOME_SCREEN")
    data object Creator : Routes("CREATOR_SCREEN")
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

    data object Detail : Routes("DETAIL_SCREEN/{itemId}/{listJson}") {
        fun createRoute(itemId: String, listJson: String) = "DETAIL_SCREEN/$itemId/$listJson"
    }

    data object PlayerScreen {
        const val route = "playerScreen/{playerDtoList}"
        fun createRoute(playerDtoList: List<PosterCardDto>): String {
            val json = Json.encodeToString(playerDtoList)
            return "playerScreen/${Uri.encode(json)}"
        }
    }

}