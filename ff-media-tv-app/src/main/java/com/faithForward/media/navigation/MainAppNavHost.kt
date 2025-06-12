package com.faithForward.media.navigation

import android.net.Uri
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
import com.faithForward.media.home.creator.detail.CreatorDetailScreen
import com.faithForward.media.home.genre.GenreDataScreen
import com.faithForward.media.home.movies.MoviesPage
import com.faithForward.media.home.myList.MyListPage
import com.faithForward.media.home.series.SeriesPage
import com.faithForward.media.login.LoginScreen
import com.faithForward.media.player.PlayerScreen
import com.faithForward.media.viewModel.ContentViewModel
import com.faithForward.media.viewModel.CreatorDetailViewModel
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
                        Log.e(
                            "CARSOUEL_SLUG",
                            "onItem with ${item.slug}"
                        )
                        val filteredList =
                            list.filterNot { it.id == item.id }

                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                },
                onCarouselItemClick = { carouselItem, isFromContinueWatching ->
                    val route = Routes.PlayerScreen.createRoute(
                        listOf(carouselItem),
                        isContinueWatching = isFromContinueWatching
                    )
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
            CreatorScreen(creatorViewModel = creatorViewModel, onCreatorItemClick = { item ->
                navController.navigate(Routes.CREATOR_DETAIL.createRoute(item.id))
            })
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
                    navController.navigate(Routes.Detail.createRoute(item.slug))
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
                    navController.navigate(Routes.Detail.createRoute(item.slug))
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
                    navController.navigate(Routes.Detail.createRoute(item.slug))
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
            // val genreId = backStackEntry.arguments?.getString("genreId") ?: ""

            val genreViewModel: GenreViewModel = hiltViewModel()

            GenreDataScreen(
                viewModel = genreViewModel,
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.slug == item.slug }
                        val json = Json.encodeToString(filteredList)
                        val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                })
        }

        composable(
            route = Routes.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
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
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                })
        }



        composable(
            route = Routes.PlayerScreen.route,
            arguments = listOf(
                navArgument("playerDtoList") { type = NavType.StringType },
                navArgument("isContinueWatching") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("playerDtoList")
            val isContinueWatching =
                backStackEntry.arguments?.getBoolean("isContinueWatching") ?: false
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
                playerViewModel = playerViewModel,
                isFromContinueWatching = isContinueWatching,
                onPlayerBackClick = {
                    if (isContinueWatching) {
                        // Get the current item's slug from playerDtoList
                        val currentItem = playerDtoList?.getOrNull(0)
                        if (currentItem?.slug != null) {
                            // Navigate to Detail screen
                            if (currentItem.contentType == "Series" || currentItem.contentType == "Episode" && currentItem.seriesSlug != null) {
                                navController.navigate(Routes.Detail.createRoute(currentItem.seriesSlug!!)) {
                                    // Ensure Home screen remains in the back stack
                                    popUpTo(Routes.Home.route) { inclusive = false }
                                }
                            } else {
                                navController.navigate(Routes.Detail.createRoute(currentItem.slug)) {
                                    // Ensure Home screen remains in the back stack
                                    popUpTo(Routes.Home.route) { inclusive = false }
                                }
                            }
                        } else {
                            // Fallback to default back navigation if slug is unavailable
                            navController.popBackStack()
                        }
                    } else {
                        // Default back navigation (to Detail or Home)
                        navController.popBackStack()
                    }
                }
            )
        }


        composable(
            route = Routes.CREATOR_DETAIL.route,
            arguments = listOf(navArgument("creatorId") { type = NavType.IntType })
        ) {

            val creatorDetailViewModel = hiltViewModel<CreatorDetailViewModel>()

            CreatorDetailScreen(
                creatorDetailViewModel = creatorDetailViewModel,
                onCreatorContentClick = { item ->
                    if (item.slug != null) {
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                }
            )

        }

    }

}