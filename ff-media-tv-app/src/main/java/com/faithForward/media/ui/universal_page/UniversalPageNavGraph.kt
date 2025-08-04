package com.faithForward.media.ui.universal_page

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithForward.media.ui.universal_page.stream.StreamPage

sealed class UniversalPageRoutes(val route: String) {


    data object Stream : UniversalPageRoutes("STREAM_PAGE")

    data object Live : UniversalPageRoutes("LIVE_PAGE")

    data object Browse : UniversalPageRoutes("BROWSE_PAGE")
}

@Composable
fun UniversalPageNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = UniversalPageRoutes.Stream.route) {

        composable(route = UniversalPageRoutes.Stream.route) {
            StreamPage()
        }



    }


}