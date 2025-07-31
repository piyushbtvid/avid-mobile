package com.faithForward.media.ui.universal_page.live.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithForward.media.ui.universal_page.live.LivePage
import com.faithForward.media.ui.universal_page.live.guide.GuidePage
import com.faithForward.media.viewModel.UniversalViewModel

sealed class LiveRoutes(val route: String) {

    data object Live : LiveRoutes("LIVE_PAGE")

    data object Guide : LiveRoutes("GUIDE_PAGE")

    data object MyChannel : LiveRoutes("MY_CHANNEL")
}

@Composable
fun LiveNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

 //   val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LiveRoutes.Live.route)
    {

        composable(route = LiveRoutes.Live.route) { backStackEntry ->
            val universalViewModel: UniversalViewModel = hiltViewModel(backStackEntry)
            LivePage(
                universalViewModel = universalViewModel
            )
        }

        composable(route = LiveRoutes.Guide.route) { backStackEntry ->
            val universalViewModel: UniversalViewModel = hiltViewModel(backStackEntry)
            GuidePage(
                universalViewModel = universalViewModel
            )
        }

    }

}