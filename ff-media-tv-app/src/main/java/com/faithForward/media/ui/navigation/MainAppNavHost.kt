package com.faithForward.media.ui.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.detail.DetailScreen
import com.faithForward.media.ui.detail.creator_detail.CreatorDetailScreen
import com.faithForward.media.ui.login.LoginScreen
import com.faithForward.media.ui.login.qr.LoginQrScreen
import com.faithForward.media.ui.player.PlayerScreen
import com.faithForward.media.ui.sections.creator.CreatorScreen
import com.faithForward.media.ui.sections.genre.GenreDataScreen
import com.faithForward.media.ui.sections.home.HomePage
import com.faithForward.media.ui.sections.movies.MoviesPage
import com.faithForward.media.ui.sections.myList.MyListPage
import com.faithForward.media.ui.sections.my_account.MyAccountScreen
import com.faithForward.media.ui.sections.search.SearchScreenUi
import com.faithForward.media.ui.sections.series.SeriesPage
import com.faithForward.media.ui.subscription.SubscriptionScreen
import com.faithForward.media.ui.universal_page.UniversalTopBarMainPage
import com.faithForward.media.ui.universal_page.live.LiveMainPage
import com.faithForward.media.ui.user_profile.AllProfileScreen
import com.faithForward.media.ui.user_profile.create_profile.CreateProfileScreen
import com.faithForward.media.ui.user_profile.edit_profile.EditProfileScreen
import com.faithForward.media.ui.user_profile.edit_profile.UpdateProfileScreen
import com.faithForward.media.viewModel.ContentViewModel
import com.faithForward.media.viewModel.CreatorDetailViewModel
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.media.viewModel.GenreViewModel
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.media.viewModel.LoginViewModel
import com.faithForward.media.viewModel.MyAccountViewModel
import com.faithForward.media.viewModel.MyListViewModel
import com.faithForward.media.viewModel.PlayerViewModel
import com.faithForward.media.viewModel.ProfileScreenViewModel
import com.faithForward.media.viewModel.QrLoginViewModel
import com.faithForward.media.viewModel.SearchViewModel
import com.faithForward.media.viewModel.SharedPlayerViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.media.viewModel.SubscriptionViewModel
import com.faithForward.media.viewModel.UniversalViewModel
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.preferences.ConfigManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    sharedPlayerViewModel: SharedPlayerViewModel,
    sideBarViewModel: SideBarViewModel,
    startRoute: String = Routes.AllProfile.route,
    changeSideBarSelectedPosition: (Int) -> Unit,
    onDataLoadedSuccess: () -> Unit,
    onBackClickForExit: () -> Unit,
) {


    NavHost(
        navController = navController, startDestination = startRoute
    ) {
        composable(route = Routes.Login.route) {
            LoginScreen(loginViewModel = loginViewModel, onLogin = {
                Log.e("GOING_TO_HOME", "going to home from Login screen ")
                loginViewModel.checkRefreshToken()
                navController.navigate(Routes.AllProfile.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        composable(route = Routes.LoginQr.route) { backStack ->
            val qrLoginViewModel: QrLoginViewModel = hiltViewModel(backStack)
            val configData = ConfigManager.getConfigData()
            val isLoginEnabled = configData?.enable_login == true

            LoginQrScreen(loginQrLoginViewModel = qrLoginViewModel, onLoggedIn = {
                Log.e("GOING_TO_HOME", "going to home from qr onLogin click")
                Log.e("IS_lOGIN_QR", "onlogin called in NavHost")
                loginViewModel.checkRefreshToken()
                navController.navigate(Routes.AllProfile.route) {
                    popUpTo(0) { inclusive = true }
                }
            }, onLoginPageOpenClick = {
                // Only navigate to Login screen if enable_login is true
                if (isLoginEnabled) {
                    navController.navigate(Routes.Login.route)
                }
            })
        }

        composable(route = Routes.AllProfile.route) { navBackStackEntry ->

            val viewModel: ProfileScreenViewModel = hiltViewModel(navBackStackEntry)

            AllProfileScreen(
                profileScreenViewModel = viewModel,
                onAddProfileClick = {
                    navController.navigate(Routes.CreateProfile.route)
                },
                onManageProfileClick = {
                    navController.navigate(Routes.EditProfile.route)
                },
                onSetProfileSuccess = {
                    navController.navigate(Routes.Home.route)
                }
            )
        }

        composable(route = Routes.CreateProfile.route) { navBackStackEntry ->

            val viewModel: ProfileScreenViewModel = hiltViewModel(navBackStackEntry)

            CreateProfileScreen(
                profileScreenViewModel = viewModel
            )

        }

        composable(
            route = Routes.EditProfile.route
        ) { navBackStackEntry ->

            val viewModel: ProfileScreenViewModel = hiltViewModel(navBackStackEntry)

            EditProfileScreen(
                profileScreenViewModel = viewModel,
                onItemClick = { item ->
                    navController.navigate("update_profile/${item.avatarId}/${item.name}/${item.id}/${item.isDefault}")
                }
            )

        }


        composable(
            route = Routes.UpdateProfile.FULL_ROUTE,
            arguments = listOf(
                navArgument("avatarId") { type = NavType.IntType },
                navArgument("userName") { type = NavType.StringType },
                navArgument("profileId") { type = NavType.IntType },
                navArgument("isDefaultProfile") { type = NavType.BoolType }
            )
        ) { backStackEntry ->

            val viewModel: ProfileScreenViewModel = hiltViewModel(backStackEntry)

            val avatarId = backStackEntry.arguments?.getInt("avatarId") ?: -1
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val profileId = backStackEntry.arguments?.getInt("profileId") ?: -1
            val isDefaultProfile = backStackEntry.arguments?.getBoolean("isDefaultProfile") ?: false

            UpdateProfileScreen(
                profileScreenViewModel = viewModel,
                avatarId = avatarId,
                userName = userName,
                profileId = profileId,
                isDefaultProfile = isDefaultProfile,
                onDeleteSuccess = {
                    navController.popBackStack()
                }
            )
        }


        composable(route = Routes.Home.route) { navBackStackEntry ->
            // Scope the ViewModel to the navigation destination
            val homeViewModel: HomeViewModel = hiltViewModel(navBackStackEntry)
            HomePage(modifier = modifier,
                homeViewModel = homeViewModel,
                sideBarViewModel = sideBarViewModel,
                onDataLoadedSuccess = onDataLoadedSuccess,
                changeSideBarSelectedPosition = { value ->
                    changeSideBarSelectedPosition.invoke(value)
                },
                onBackClick = {
                    Log.e("ON_BACK", "on back in home compose caled")
                    onBackClickForExit.invoke()
                    //activity?.finish()
                },
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        Log.e(
                            "CARSOUEL_SLUG", "onItem with ${item.slug}"
                        )
                        val filteredList = list.filterNot { it.id == item.id }

                        val json = Json.encodeToString(filteredList)
                        //   val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                },
                onCarouselItemClick = { carouselItem, isFromContinueWatching ->
                    Log.e(
                        "CONTINUE_WATCHING_CLICK",
                        "item in continue watching when cliked  is $carouselItem and $isFromContinueWatching"
                    )
                    val route = Routes.PlayerScreen.createRoute(
                        listOf(carouselItem),
                        isContinueWatching = isFromContinueWatching,
                        initialIndex = 0
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
                },
                onSearchClick = {
                    changeSideBarSelectedPosition.invoke(0)
                    navController.navigate(Routes.Search.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onMicDoubleUpClick = {
                    Log.e("DOUBLE", "on double up clicked")
                    navController.navigate(Routes.Universal.route)
                }

            )
        }

        composable(route = Routes.Creator.route) {
            val creatorViewModel: CreatorViewModel = hiltViewModel()
            CreatorScreen(creatorViewModel = creatorViewModel,
                sideBarViewModel = sideBarViewModel,
                onBackClick = {
                    onBackClickForExit.invoke()
                    //   activity?.finish()
                },
                onCreatorItemClick = { item ->
                    navController.navigate(Routes.CREATOR_DETAIL.createRoute(item.id))
                },
                onCarouselClick = { item ->
                    if (item.id != null) {
                        navController.navigate(Routes.CREATOR_DETAIL.createRoute(item.id.toInt()))
                    }
                },
                onSearchClick = {
                    changeSideBarSelectedPosition.invoke(0)
                    navController.navigate(Routes.Search.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }

        composable(
            route = Routes.Movies.route,
            arguments = listOf(navArgument("contentType") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val contentViewModel: ContentViewModel = hiltViewModel(navBackStackEntry)
            MoviesPage(contentViewModel = contentViewModel,
                sideBarViewModel = sideBarViewModel,
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.slug == item.slug }
                        val json = Json.encodeToString(filteredList)
                        // val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                },
                onBackClick = {
                    onBackClickForExit.invoke()
                    //  activity?.finish()
                },
                onCarouselItemClick = { carouselItem ->
                    val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                    navController.navigate(route)
                },
                onSearchClick = {
                    changeSideBarSelectedPosition.invoke(0)
                    navController.navigate(Routes.Search.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }

        composable(
            route = Routes.Series.route,
            arguments = listOf(navArgument("contentType") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val contentViewModel: ContentViewModel = hiltViewModel(navBackStackEntry)
            SeriesPage(contentViewModel = contentViewModel,
                sideBarViewModel = sideBarViewModel,
                onBackClick = {
                    onBackClickForExit.invoke()
                    // activity?.finish()
                },
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.slug == item.slug }
                        val json = Json.encodeToString(filteredList)
                        //   val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                },
                onCarouselItemClick = { carouselItem ->
                    val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                    navController.navigate(route)
                },
                onSearchClick = {
                    changeSideBarSelectedPosition.invoke(0)
                    navController.navigate(Routes.Search.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                })
        }
        composable(route = Routes.MyList.route) {
            val myListViewModel: MyListViewModel = hiltViewModel()
            MyListPage(contentViewModel = myListViewModel,
                sideBarViewModel = sideBarViewModel,
                onBackClick = {
                    onBackClickForExit.invoke()
                    //   activity?.finish()
                },
                onItemClick = { item, list ->
                    if (!item.slug.isNullOrEmpty()) {
                        val filteredList = list.filterNot { it.slug == item.slug }
                        val json = Json.encodeToString(filteredList)
                        // val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                },
                onCarouselItemClick = { carouselItem ->
                    val route = Routes.PlayerScreen.createRoute(listOf(carouselItem))
                    navController.navigate(route)
                },
                onSearchClick = {
                    changeSideBarSelectedPosition.invoke(0)
                    navController.navigate(Routes.Search.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }

            )
        }

        composable(route = Routes.GenreData.route, arguments = listOf(navArgument("genreId") {
            type = NavType.StringType
        })) { backStackEntry ->
            // val genreId = backStackEntry.arguments?.getString("genreId") ?: ""

            val genreViewModel: GenreViewModel = hiltViewModel()

            GenreDataScreen(viewModel = genreViewModel, onItemClick = { item, list ->
                if (!item.slug.isNullOrEmpty()) {
                    val filteredList = list.filterNot { it.slug == item.slug }
                    val json = Json.encodeToString(filteredList)
                    //  val encodedList = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate(Routes.Detail.createRoute(item.slug))
                }
            }, onSearchClick = {
                changeSideBarSelectedPosition.invoke(0)
                navController.navigate(Routes.Search.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }

            )
        }

        composable(
            route = Routes.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = hiltViewModel()

            val slug = backStackEntry.arguments?.getString("itemId")

            DetailScreen(detailViewModel = detailViewModel,
                slug = slug,
                onWatchNowClick = { item, posterItemList, index ->
                    Log.e("SERIES_RELATED", "item is $item")
                    Log.e(
                        "SERIES_RELATED",
                        "item List  Related list  when click in detail NavHost from series episode is ${
                            posterItemList?.get(
                                0
                            )?.relatedList?.get(0)?.relatedList
                        }"
                    )
                    if (item != null) {
                        val itemWithProgressZero = item.copy(progress = 0)
                        val route =
                            Routes.PlayerScreen.createRoute(listOf(itemWithProgressZero)) // Wrap item in a list
                        navController.navigate(route)
                    } else {
                        Log.e(
                            "SERIES_CONTENT",
                            "SERIES CONTENT type is in AppNav ${posterItemList?.get(0)?.contentType}"
                        )
                        if (posterItemList != null) {
                            val route = Routes.PlayerScreen.createRoute(
                                posterItemList, initialIndex = index
                            ) // Wrap item in a list
                            navController.navigate(route)
                        }
                    }
                },
                onPlayTrailerClick = { item, posterItemList, index ->
                    Log.e("SERIES_RELATED", "item is $item")
                    Log.e(
                        "SERIES_RELATED",
                        "item List  Related list  when click in detail NavHost from series episode is ${
                            posterItemList?.get(
                                0
                            )?.relatedList?.get(0)?.relatedList
                        }"
                    )
                    if (item != null) {
                        val itemWithProgressZero = item.copy(progress = 0)
                        val route =
                            Routes.PlayerScreen.createRoute(
                                listOf(itemWithProgressZero),
                                isPlayTrailer = true
                            ) // Wrap item in a list
                        navController.navigate(route)
                    }
                },
                onResumeNowClick = { item, posterItemList, index ->
                    if (item != null) {
                        val route = Routes.PlayerScreen.createRoute(
                            listOf(item), initialIndex = index
                        ) // Wrap item in a list
                        if (item.progress != null && item.progress > 0) {
                            navController.navigate(route)
                        }
                    } else {
                        if (posterItemList != null) {
                            val route = Routes.PlayerScreen.createRoute(
                                posterItemList, initialIndex = index
                            ) // Wrap item in a list
                            navController.navigate(route)
                        }
                    }
                },
                onRelatedItemClick = { item ->
                    if (!item.slug.isNullOrEmpty()) {
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                })
        }


//        composable(
//            route = Routes.PlayerScreen.route,
//            arguments = listOf(navArgument("playerDtoList") { type = NavType.StringType },
//                navArgument("isContinueWatching") {
//                    type = NavType.BoolType
//                    defaultValue = false
//                },
//                navArgument("isFromMyAccount") {
//                    type = NavType.BoolType
//                    defaultValue = false
//                },
//                navArgument("isPlayTrailer") {
//                    type = NavType.BoolType
//                    defaultValue = false
//                },
//                navArgument("initialIndex") {
//                    type = NavType.IntType
//                    defaultValue = 0
//                })
//        ) { backStackEntry ->
//
//            val playerViewModel: PlayerViewModel = hiltViewModel(backStackEntry)
//
//            val encodedJson = backStackEntry.arguments?.getString("playerDtoList")
//            val isContinueWatching =
//                backStackEntry.arguments?.getBoolean("isContinueWatching") ?: false
//
//            val isFromMyAccount = backStackEntry.arguments?.getBoolean("isFromMyAccount") ?: false
//            val isPlayTrailer = backStackEntry.arguments?.getBoolean("isPlayTrailer") ?: false
//
//
//            val initialIndex = backStackEntry.arguments?.getInt("initialIndex") ?: 0
//
//            val playerList = encodedJson?.let {
//                Json.decodeFromString<List<PosterCardDto>>(Uri.decode(it))
//            } ?: emptyList()
//
//
//            playerList?.let {
//                LaunchedEffect(playerList) {
//                    playerList.let {
//                        playerViewModel.handleEvent(
//                            PlayerEvent.UpdateOrLoadPlayerData(
//                                itemList = it,
//                                isFromContinueWatching = isContinueWatching,
//                                isTrailer = isPlayTrailer,
//                                index = initialIndex
//                            )
//                        )
//                    }
//                }
//            }
//
//            PlayerScreen(playerViewModel = playerViewModel,
//                sharedPlayerViewModel = sharedPlayerViewModel,
//                isFromContinueWatching = isContinueWatching,
//                initialIndex = initialIndex,
//                onStreamFromTopBarClick = {
//                    navController.navigate(Routes.Universal.route)
//                },
//                onLiveClick = {
//                    navController.navigate(Routes.Live.route)
//                },
//                onVideoEnded = {
//                    if (isContinueWatching && !isFromMyAccount) {
//                        // Get the current item's slug from playerDtoList
//                        val currentItem = playerList?.getOrNull(0)
//                        if (currentItem?.slug != null) {
//                            // Navigate to Detail screen
//                            if (currentItem.contentType == "Series" || currentItem.contentType == "Episode" && currentItem.seriesSlug != null) {
//                                navController.navigate(Routes.Detail.createRoute(currentItem.seriesSlug!!)) {
//                                    // Ensure Home screen remains in the back stack
//                                    popUpTo(Routes.Home.route) { inclusive = false }
//                                }
//                            } else {
//                                navController.navigate(Routes.Detail.createRoute(currentItem.slug)) {
//                                    // Ensure Home screen remains in the back stack
//                                    popUpTo(Routes.Home.route) { inclusive = false }
//                                }
//                            }
//                        } else {
//                            // Fallback to default back navigation if slug is unavailable
//                            navController.popBackStack()
//                        }
//                    } else if (isFromMyAccount && isContinueWatching) {
//                        // Get the current item's slug from playerDtoList
//                        val currentItem = playerList?.getOrNull(0)
//                        if (currentItem?.slug != null) {
//                            // Navigate to Detail screen
//                            if (currentItem.contentType == "Series" || currentItem.contentType == "Episode" && currentItem.seriesSlug != null) {
//                                navController.navigate(Routes.Detail.createRoute(currentItem.seriesSlug!!)) {
//                                    // Ensure Home screen remains in the back stack
//                                    popUpTo(Routes.MyAccount.route) { inclusive = false }
//                                }
//                            } else {
//                                navController.navigate(Routes.Detail.createRoute(currentItem.slug)) {
//                                    // Ensure Home screen remains in the back stack
//                                    popUpTo(Routes.MyAccount.route) { inclusive = false }
//                                }
//                            }
//                        } else {
//                            // Fallback to default back navigation if slug is unavailable
//                            navController.popBackStack()
//                        }
//                    } else {
//                        // Default back navigation (to Detail or Home)
//                        navController.popBackStack()
//                    }
//                },
//                onEpisodePlayNowClick = { list, index ->
//                    val posterCardList = list.map { it.toPosterCardDto() }
//                    val route =
//                        Routes.PlayerScreen.createRoute(posterCardList, initialIndex = index!!)
//                    navController.navigate(route) {
//                        popUpTo(Routes.PlayerScreen.route) { inclusive = true }
//                        launchSingleTop = true
//                    }
//                },
//                onRelatedItemClick = { item, list, index ->
//                    val posterCardList =
//                        list?.map { it.toPosterCardDto() } ?: listOf(item!!.toPosterCardDto())
//                    val route =
//                        Routes.PlayerScreen.createRoute(posterCardList, initialIndex = index!!)
//                    navController.navigate(route) {
//                        popUpTo(Routes.PlayerScreen.route) { inclusive = true }
//                        launchSingleTop = true
//                    }
//                })
//        }


        composable(
            route = Routes.PlayerScreen.route,
            arguments = listOf(
                navArgument("playerDtoList") { type = NavType.StringType },
                navArgument("isContinueWatching") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("isFromMyAccount") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("isPlayTrailer") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("initialIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->

            val encodedJson = backStackEntry.arguments?.getString("playerDtoList")
            val isContinueWatching =
                backStackEntry.arguments?.getBoolean("isContinueWatching") ?: false
            val isFromMyAccount = backStackEntry.arguments?.getBoolean("isFromMyAccount") ?: false
            val isPlayTrailer = backStackEntry.arguments?.getBoolean("isPlayTrailer") ?: false
            val initialIndex = backStackEntry.arguments?.getInt("initialIndex") ?: 0

            val playerList = encodedJson?.let {
                Json.decodeFromString<List<PosterCardDto>>(Uri.decode(it))
            } ?: emptyList()


            val playerViewModel: PlayerViewModel = hiltViewModel(backStackEntry)

            val firstItem = playerList.getOrNull(initialIndex)
            val userType by playerViewModel.userType.collectAsState()

            var canPlay by remember { mutableStateOf<Boolean?>(null) }

            LaunchedEffect(firstItem, userType) {
                if (firstItem != null && userType != null) {
                    checkVideoPlayback(
                        poster = firstItem,
                        userType = userType!!,
                        onStartPlay = { canPlay = true },
                        onRequireSubscription = {
                            canPlay = false
                            val route = Routes.Subscription.createRoute(
                                playerDtoList = playerList,
                                isContinueWatching = isContinueWatching,
                                initialIndex = initialIndex,
                                isPlayTrailer = isPlayTrailer,
                                isFromMyAccount = isFromMyAccount
                            )
                            navController.navigate(route) {
                                popUpTo(Routes.PlayerScreen.route) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // inside composable scope, decide what to render
            if (canPlay == true) {
                LaunchedEffect(playerList) {
                    playerViewModel.handleEvent(
                        PlayerEvent.UpdateOrLoadPlayerData(
                            itemList = playerList,
                            isFromContinueWatching = isContinueWatching,
                            isTrailer = isPlayTrailer,
                            index = initialIndex
                        )
                    )
                }

                PlayerScreen(
                    playerViewModel = playerViewModel,
                    sharedPlayerViewModel = sharedPlayerViewModel,
                    isFromContinueWatching = isContinueWatching,
                    initialIndex = initialIndex,
                    onStreamFromTopBarClick = {
                        navController.navigate(Routes.Universal.route)
                    },
                    onLiveClick = {
                        navController.navigate(Routes.Live.route)
                    },
                    onVideoEnded = {
                        if (isContinueWatching && !isFromMyAccount) {
                            // Get the current item's slug from playerDtoList
                            val currentItem = playerList?.getOrNull(0)
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
                        } else if (isFromMyAccount && isContinueWatching) {
                            // Get the current item's slug from playerDtoList
                            val currentItem = playerList?.getOrNull(0)
                            if (currentItem?.slug != null) {
                                // Navigate to Detail screen
                                if (currentItem.contentType == "Series" || currentItem.contentType == "Episode" && currentItem.seriesSlug != null) {
                                    navController.navigate(Routes.Detail.createRoute(currentItem.seriesSlug!!)) {
                                        // Ensure Home screen remains in the back stack
                                        popUpTo(Routes.MyAccount.route) { inclusive = false }
                                    }
                                } else {
                                    navController.navigate(Routes.Detail.createRoute(currentItem.slug)) {
                                        // Ensure Home screen remains in the back stack
                                        popUpTo(Routes.MyAccount.route) { inclusive = false }
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
                    },
                    onEpisodePlayNowClick = { list, index ->
                        val posterCardList = list.map { it.toPosterCardDto() }
                        val route =
                            Routes.PlayerScreen.createRoute(posterCardList, initialIndex = index!!)
                        navController.navigate(route) {
                            popUpTo(Routes.PlayerScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onRelatedItemClick = { item, list, index ->
                        val posterCardList =
                            list?.map { it.toPosterCardDto() } ?: listOf(item!!.toPosterCardDto())
                        val route =
                            Routes.PlayerScreen.createRoute(posterCardList, initialIndex = index!!)
                        navController.navigate(route) {
                            popUpTo(Routes.PlayerScreen.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }



        composable(
            route = Routes.CREATOR_DETAIL.route,
            arguments = listOf(navArgument("creatorId") { type = NavType.IntType })
        ) {

            val creatorDetailViewModel = hiltViewModel<CreatorDetailViewModel>()

            CreatorDetailScreen(creatorDetailViewModel = creatorDetailViewModel,
                onCreatorContentClick = { item ->
                    if (item.slug != null) {
                        navController.navigate(Routes.Detail.createRoute(item.slug))
                    }
                })

        }

        composable(
            route = Routes.Search.route
        ) { backStackEntry ->
            val searchViewModel: SearchViewModel = hiltViewModel(backStackEntry)
            SearchScreenUi(searchViewModel = searchViewModel,
                sideBarViewModel = sideBarViewModel,
                onBackClick = {
                    onBackClickForExit.invoke()
                },
                onSearchItemClick = { item ->
                    if (item.contentType == "Creator" && item.itemId != null) {
                        navController.navigate(Routes.CREATOR_DETAIL.createRoute(item.itemId.toInt()))
                    } else {
                        if (item.contentSlug != null) {
                            navController.navigate(Routes.Detail.createRoute(item.contentSlug))
                        }
                    }
                })

        }

        composable(
            route = Routes.MyAccount.route
        ) { backStackEntry ->
            val myAccountViewModel: MyAccountViewModel = hiltViewModel(backStackEntry)
            MyAccountScreen(myAccountViewModel = myAccountViewModel,
                sideBarViewModel = sideBarViewModel,
                onBackClick = {
                    onBackClickForExit.invoke()
                },
                onItemClick = { item, isFromContinueWatching ->

                    if (isFromContinueWatching) {
                        val posterCardDto = item.toPosterCardDto()

                        val route = Routes.PlayerScreen.createRoute(
                            listOf(posterCardDto),
                            isContinueWatching = true,
                            isFromMyAccount = true,
                            initialIndex = 0
                        )
                        navController.navigate(route)
                    } else {
                        navController.navigate(Routes.Detail.createRoute(item.contentSlug))
                    }
                },
                onSwitchProfile = {
                    navController.navigate(Routes.AllProfile.route)
                }
            )
        }

        composable(route = Routes.Universal.route) { backStackEntry ->

            val universalViewModel: UniversalViewModel = hiltViewModel(backStackEntry)

            UniversalTopBarMainPage(universalViewModel = universalViewModel, onSearchClick = {

            }, onLiveClick = {
                navController.navigate(Routes.Live.route)
            }, onLeftClick = {
                navController.popBackStack()
            }

            )
        }

        composable(route = Routes.Live.route) {
            LiveMainPage(
                onTopBarUpClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.Subscription.route,
            arguments = listOf(
                navArgument("playerDtoList") { type = NavType.StringType },
                navArgument("isContinueWatching") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("isFromMyAccount") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("isPlayTrailer") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("initialIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val subscriptionViewModel = hiltViewModel<SubscriptionViewModel>(backStackEntry)

            val encodedJson = backStackEntry.arguments?.getString("playerDtoList")
            val isContinueWatching =
                backStackEntry.arguments?.getBoolean("isContinueWatching") ?: false
            val isFromMyAccount = backStackEntry.arguments?.getBoolean("isFromMyAccount") ?: false
            val isPlayTrailer = backStackEntry.arguments?.getBoolean("isPlayTrailer") ?: false
            val initialIndex = backStackEntry.arguments?.getInt("initialIndex") ?: 0

            val playerList = encodedJson?.let {
                Json.decodeFromString<List<PosterCardDto>>(Uri.decode(it))
            } ?: emptyList()

            LaunchedEffect(Unit) {
                Log.e(
                    "SUBSCRIPTION_SCREEN",
                    "data in subscrioption is $isContinueWatching  and $isFromMyAccount  and $isPlayTrailer  and $initialIndex  and final the list $playerList"
                )
            }

            SubscriptionScreen(
                subscriptionViewModel = subscriptionViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToPlayer = {
                    val route = Routes.PlayerScreen.createRoute(
                        playerDtoList = playerList,
                        isContinueWatching = isContinueWatching,
                        isFromMyAccount = isFromMyAccount,
                        isPlayTrailer = isPlayTrailer,
                        initialIndex = initialIndex
                    )
                    navController.navigate(route){
                        popUpTo(Routes.Subscription.route) { inclusive = true }
                    }
                }
            )
        }

    }


}


fun checkVideoPlayback(
    poster: PosterCardDto,
    userType: String,
    onStartPlay: () -> Unit,
    onRequireSubscription: () -> Unit,
) {
    Log.e("USER_TYPE", "user type in checkVideo is $userType")
    Log.e("USER_TYPE", "item acess type is ${poster.access}")
    if (poster.access == "Paid") {
        if (userType == "Premium") {
            println("Play allowed")
            onStartPlay()
        } else {
            println("Need subscription")
            onRequireSubscription()
        }
    } else {
        println("Free content")
        onStartPlay()
    }
}
