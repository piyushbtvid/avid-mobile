package com.faithForward.media.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.faithForward.media.player.PlayerScreen
import com.faithForward.media.viewModel.ContentViewModel
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.media.viewModel.GenreViewModel
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    playerViewModel: PlayerViewModel,
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
                    if (!item.id.isNullOrEmpty()) {
                        val filteredList = if (item.id.contains("series")) {
                            emptyList()
                        } else {
                            list.filterNot { it.id == item.id }
                        }

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
            val contentViewModel: ContentViewModel = hiltViewModel()
            MoviesPage(
                contentViewModel = contentViewModel,
                onItemClick = { item, list ->
                    if (!item.id.isNullOrEmpty()) {
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
                    if (!item.id.isNullOrEmpty()) {
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
        ) {
            val detailViewModel: DetailViewModel = hiltViewModel()

            DetailScreen(
                detailViewModel = detailViewModel,
                onWatchNowClick = { item ->
                    val route = Routes.PlayerScreen.createRoute(item)
                    navController.navigate(route)
                },
                onRelatedItemClick = { item, list ->
                    if (!item.id.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.id == item.id }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.id, encodedList))
                    }
                }
            )
        }


        composable(
            route = Routes.PlayerScreen.route,
            arguments = listOf(navArgument("playerDto") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("playerDto")
            val playerDto = json?.let { Json.decodeFromString<PosterCardDto>(Uri.decode(it)) }


            playerDto?.let {
                playerViewModel.handleEvent(
                    PlayerEvent.UpdateVideoPlayerDto(
                        itemList = listOf(playerDto)
                    )
                )

            }

            PlayerScreen(
                playerViewModel = playerViewModel
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