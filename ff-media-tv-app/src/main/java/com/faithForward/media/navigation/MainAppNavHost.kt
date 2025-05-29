package com.faithForward.media.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.DetailScreen
import com.faithForward.media.home.HomePage
import com.faithForward.media.home.creator.CreatorScreen
import com.faithForward.media.home.genre.GenreDataScreen
import com.faithForward.media.home.movies.MoviesPage
import com.faithForward.media.login.LoginScreen
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.media.viewModel.GenreViewModel
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.MoviesViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startRoute: String = Routes.Home.route,
    changeSideBarSelectedPosition: (Int) -> Unit,
    onDataLoadedSuccess: () -> Unit,
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
                onItemClick = { item, list ->
                    if (item.id.isNotEmpty()) {
                        val filteredList = list.filterNot { it.id == item.id }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.id, encodedList))
                    }
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
                moviesViewModel = moviesViewModel,
                onItemClick = { item, list ->
                    if (item.id.isNotEmpty()) {
                        val filteredList = list.filterNot { it.id == item.id }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.id, encodedList))
                    }
                }
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
                viewModel = genreViewModel,
                onItemClick = { item, list ->
                    if (item.id.isNotEmpty()) {
                        val filteredList = list.filterNot { it.id == item.id }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.id, encodedList))
                    }
                }
            )
        }

        composable(
            route = Routes.Detail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType },
                navArgument("listJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            val listJson = backStackEntry.arguments?.getString("listJson") ?: ""

            val detailViewModel: DetailViewModel = hiltViewModel()

            val posterList: List<PosterCardDto> = try {
                val decoded =
                    java.net.URLDecoder.decode(listJson, StandardCharsets.UTF_8.toString())
                Json.decodeFromString(decoded)
            } catch (e: Exception) {
                emptyList()
            }

            Log.e("POSTER", "poster list recived in detail with $posterList")
            DetailScreen(
                itemId = itemId,
                detailViewModel = detailViewModel,
                relatedList = posterList
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