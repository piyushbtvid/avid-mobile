package com.faithForward.media.navigation

import android.net.Uri
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
import com.faithForward.media.home.myList.MyListPage
import com.faithForward.media.home.series.SeriesPage
import com.faithForward.media.login.LoginScreen
import com.faithForward.media.player.PlayerScreen
import com.faithForward.media.viewModel.ContentViewModel
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.media.viewModel.GenreViewModel
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.MyListViewModel
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
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
                loginViewModel = loginViewModel, navController = navController
            )
        }

        composable(route = Routes.Home.route) { navBackStackEntry ->
            // Scope the ViewModel to the navigation destination
            val homeViewModel: HomeViewModel = hiltViewModel(navBackStackEntry)
            HomePage(modifier = modifier,
                homeViewModel = homeViewModel,
                onDataLoadedSuccess = onDataLoadedSuccess,
                changeSideBarSelectedPosition = { value ->
                    changeSideBarSelectedPosition.invoke(value)
                },
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = if (item.slug.contains("series")) {
                            emptyList()
                        } else {
                            list.filterNot { it.id == item.id }
                        }

                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug, encodedList))
                    }
                },
                onCarouselItemClick = { carouselItem ->
                    val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                    navController.navigate(route)
                },
                onCategoryClick = { id ->
                    if (id.isNotEmpty()) {
                        navController.navigate(Routes.GenreData.createRoute(id)) {
                            popUpTo(Routes.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                })
        }

        composable(route = Routes.Creator.route) {
            val creatorViewModel: CreatorViewModel = hiltViewModel()
            CreatorScreen(
                creatorViewModel = creatorViewModel,
            )
        }

        composable(
            route = Routes.Movies.route,
            arguments = listOf(navArgument("contentType") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val contentViewModel: ContentViewModel = hiltViewModel(navBackStackEntry)
            MoviesPage(contentViewModel = contentViewModel, onItemClick = { item, list ->
                if (!item.slug.isNullOrEmpty()) {
                    val filteredList = list.filterNot { it.slug == item.slug }
                    val json = Json.encodeToString(filteredList)
                    val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.Detail.createRoute(item.slug, encodedList))
                }
            }, onCarouselItemClick = { carouselItem ->
                val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                navController.navigate(route)
            })
        }

        composable(
            route = Routes.Series.route,
            arguments = listOf(navArgument("contentType") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val contentViewModel: ContentViewModel = hiltViewModel(navBackStackEntry)
            SeriesPage(contentViewModel = contentViewModel, onItemClick = { item, list ->
                if (!item.slug.isNullOrEmpty()) {
                    val filteredList = list.filterNot { it.slug == item.slug }
                    val json = Json.encodeToString(filteredList)
                    val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.Detail.createRoute(item.slug, encodedList))
                }
            }, onCarouselItemClick = { carouselItem ->
                val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                navController.navigate(route)
            })
        }
        composable(route = Routes.MyList.route) {
            val myListViewModel: MyListViewModel = hiltViewModel()
            MyListPage(contentViewModel = myListViewModel, onItemClick = { item, list ->
                if (!item.slug.isNullOrEmpty()) {
                    val filteredList = list.filterNot { it.slug == item.slug }
                    val json = Json.encodeToString(filteredList)
                    val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.Detail.createRoute(item.slug, encodedList))
                }
            }, onCarouselItemClick = { carouselItem ->
                val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                navController.navigate(route)
            }

            )
        }

        composable(route = Routes.GenreData.route, arguments = listOf(navArgument("genreId") {
            type = NavType.StringType
        })) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getString("genreId") ?: ""

            val genreViewModel: GenreViewModel = hiltViewModel()

            GenreDataScreen(genreId = genreId,
                viewModel = genreViewModel,
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.slug == item.slug }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug, encodedList))
                    }
                })
        }

        composable(
            route = Routes.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType },
                navArgument("listJson") { type = NavType.StringType })
        ) {
            val detailViewModel: DetailViewModel = hiltViewModel()

            DetailScreen(detailViewModel = detailViewModel,
                onWatchNowClick = { item, posterItemList ->
                    if (item != null) {
                        val route =
                            Routes.PlayerScreen.createRoute(listOf(item)) // Wrap item in a list
                        navController.navigate(route)
                    } else {
                        if (posterItemList != null) {
                            val route =
                                Routes.PlayerScreen.createRoute(posterItemList) // Wrap item in a list
                            navController.navigate(route)
                        }
                    }
                },
                onRelatedItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.slug == item.slug }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug, encodedList))
                    }
                })
        }


        composable(
            route = Routes.PlayerScreen.route,
            arguments = listOf(navArgument("playerDtoList") { type = NavType.StringType })
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("playerDtoList")
            val playerDtoList =
                json?.let { Json.decodeFromString<List<PosterCardDto>>(Uri.decode(it)) }

            playerDtoList?.let {
                playerViewModel.handleEvent(
                    PlayerEvent.UpdateVideoPlayerDto(
                        itemList = it // Pass the list directly
                    )
                )
            }

            PlayerScreen(
                playerViewModel = playerViewModel
            )
        }


    }

}