package com.faithForward.media.ui.universal_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithForward.media.ui.universal_page.stream.StreamPage

sealed class UniversalPageRoutes(val route: String) {

    data object Empty : UniversalPageRoutes("empty")

    data object Stream : UniversalPageRoutes("STREAM_PAGE")

    data object Live : UniversalPageRoutes("LIVE_PAGE")

    data object Browse : UniversalPageRoutes("BROWSE_PAGE")
}

@Composable
fun UniversalPageNavGraph(
    modifier: Modifier = Modifier,
    onLeftClick: () -> Unit,
    startRoute: String = UniversalPageRoutes.Stream.route,
    navController: NavHostController,
) {

    NavHost(navController = navController, startDestination = startRoute) {

        // Blank screen
        composable(UniversalPageRoutes.Empty.route) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Nothing shown here initially
            }
        }

        composable(route = UniversalPageRoutes.Stream.route) {
            StreamPage(
                onLeftClick = onLeftClick
            )
        }


    }


}