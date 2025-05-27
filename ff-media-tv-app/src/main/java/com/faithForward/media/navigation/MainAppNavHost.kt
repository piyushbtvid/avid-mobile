package com.faithForward.media.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faithForward.media.home.HomePage
import com.faithForward.media.home.creator.CreatorScreen
import com.faithForward.media.home.genre.GenreDataScreen
import com.faithForward.media.home.movies.MoviesPage
import com.faithForward.media.login.LoginScreen
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.GenreViewModel
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
                },
                onCategoryClick = { id ->
                    if (id.isNotEmpty()) {
                        navController.navigate(Routes.GenreData.createRoute(id)) {
                            popUpTo(Routes.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
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

        composable(
            route = Routes.GenreData.route,
            arguments = listOf(navArgument("genreId") {
                type = NavType.StringType
            }
            )
        ) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getString("genreId") ?: ""

            val genreViewModel: GenreViewModel = hiltViewModel()

            GenreDataScreen(
                genreId = genreId,
                viewModel = genreViewModel
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