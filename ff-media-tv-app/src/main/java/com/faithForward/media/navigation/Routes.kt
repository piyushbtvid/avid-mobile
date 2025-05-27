package com.faithForward.media.navigation

sealed class Routes(val route: String) {

    data object Home : Routes("HOME_SCREEN")
    data object Creator : Routes("CREATOR_SCREEN")
    data object Login : Routes("LOGIN_SCREEN")
    data object Movies : Routes("MOVIES_SCREEN")
    data object GenreData : Routes("GENRE_DATA_SCREEN/{genreId}") {
        fun createRoute(genreId: String) = "GENRE_DATA_SCREEN/$genreId"
    }
}