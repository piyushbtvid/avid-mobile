package com.faithForward.media.navigation

import android.net.Uri
import com.faithForward.media.commanComponents.PosterCardDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Routes(val route: String) {

    data object Home : Routes("HOME_SCREEN")
    data object Creator : Routes("CREATOR_SCREEN")
    data object Login : Routes("LOGIN_SCREEN")
    data object Movies : Routes("MOVIES_SCREEN")
    data object GenreData : Routes("GENRE_DATA_SCREEN/{genreId}") {
        fun createRoute(genreId: String) = "GENRE_DATA_SCREEN/$genreId"
    }

    data object Detail : Routes("DETAIL_SCREEN/{itemId}/{listJson}") {
        fun createRoute(itemId: String, listJson: String) = "DETAIL_SCREEN/$itemId/$listJson"
    }

    data object PlayerScreen : Routes("PLAYER_SCREEN/{playerDto}") {
        fun createRoute(item: PosterCardDto): String {
            val json = Uri.encode(Json.encodeToString(item))
            return "PLAYER_SCREEN/$json"
        }
    }

}