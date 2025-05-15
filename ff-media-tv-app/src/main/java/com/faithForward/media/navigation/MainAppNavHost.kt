package com.faithForward.media.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithForward.media.home.HomePage
import com.faithForward.media.home.creator.CreatorScreen
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.HomeViewModel


@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onDataLoadedSuccess: () -> Unit
) {

    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(route = Routes.Home.route) { bacStackEntry ->
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomePage(
                modifier = modifier,
                homeViewModel = homeViewModel,
                onDataLoadedSuccess = onDataLoadedSuccess
            )
        }

        composable(route = Routes.Creator.route) { bacStackEntry ->
            val creatorViewModel: CreatorViewModel = hiltViewModel()
            CreatorScreen(
                creatorViewModel = creatorViewModel
            )
        }

    }
}