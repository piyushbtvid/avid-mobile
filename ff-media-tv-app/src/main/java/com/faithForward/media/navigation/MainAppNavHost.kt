package com.faithForward.media.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.faithForward.media.home.HomePage
import com.faithForward.media.home.creator.CreatorScreen
import com.faithForward.media.viewModel.HomePageItem


@Composable
fun MainAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homePageItems: List<HomePageItem>,
    onChangeContentRowFocusedIndex: (Int) -> Unit

) {

    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(route = Routes.Home.route) {

            HomePage(
                modifier = modifier,
                homePageItems = homePageItems,
                onChangeContentRowFocusedIndex = onChangeContentRowFocusedIndex
            )
        }

        composable(route = Routes.Creator.route) {
            CreatorScreen()
        }

    }
}