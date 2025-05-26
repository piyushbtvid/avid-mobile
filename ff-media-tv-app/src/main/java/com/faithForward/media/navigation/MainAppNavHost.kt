package com.faithForward.media.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithForward.media.home.HomePage
import com.faithForward.media.home.creator.CreatorScreen
import com.faithForward.media.home.movies.MoviesPage
import com.faithForward.media.login.LoginScreen
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.MoviesViewModel


@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startRoute: String = Routes.Home.route,
    changeSideBarSelectedPosition: (Int) -> Unit,
    onDataLoadedSuccess: () -> Unit
) {

    NavHost(
        navController = navController, startDestination = startRoute
    ) {
        composable(route = Routes.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel
            )
        }

        composable(route = Routes.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomePage(modifier = modifier,
                homeViewModel = homeViewModel,
                onDataLoadedSuccess = onDataLoadedSuccess,
                changeSideBarSelectedPosition = { value ->
                    changeSideBarSelectedPosition.invoke(value)
                })
        }

        composable(route = Routes.Creator.route) {
            val creatorViewModel: CreatorViewModel = hiltViewModel()
            CreatorScreen(
                creatorViewModel = creatorViewModel,
            )
        }

        composable(route = Routes.Movies.route) {
            val moviesViewModel: MoviesViewModel = hiltViewModel()
            MoviesPage(
                moviesViewModel = moviesViewModel
            )
        }

    }


//    LaunchedEffect(userSession) {
//        if (userSession.data != null && navController.currentDestination?.route != Routes.Home.route) {
//            navController.navigate(Routes.Home.route) {
//                popUpTo(navController.graph.startDestinationId) {
//                    inclusive = true
//                }
//            }
//        } else if (userSession.data == null && navController.currentDestination?.route != Routes.Login.route) {
//            navController.navigate(Routes.Login.route) {
//                popUpTo(navController.graph.startDestinationId) {
//                    inclusive = true
//                }
//            }
//        }
//    }
}