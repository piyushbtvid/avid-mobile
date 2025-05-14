package com.faithForward.media.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.faithForward.media.commanComponents.LoaderScreen
import com.faithForward.media.sidebar.SideBar
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.util.Resource

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    sideBarViewModel: SideBarViewModel,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        homeViewModel.fetchHomePageData(sectionId = 1)
    }

    val navController = rememberNavController()
    val sideBarItems = sideBarViewModel.sideBarItems
    val homePageItemsResource by homeViewModel.homePageData.collectAsStateWithLifecycle()

    if (homePageItemsResource is Resource.Unspecified
        || homePageItemsResource is Resource.Error
    ) return

    val homePageItems = homePageItemsResource.data ?: return

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
    ) {
        when (homePageItemsResource) {
            is Resource.Loading -> {
                Log.e("LOADER", "isLoading is called")
                LoaderScreen()
            }

            is Resource.Success -> {
                MainAppNavHost(
                    navController = navController,
                    homePageItems = homePageItems,
                    onChangeContentRowFocusedIndex = { index ->
                        homeViewModel.onContentRowFocusedIndexChange(index)
                    }
                )
                SideBar(
                    columnList = sideBarItems,
                    modifier = Modifier.align(Alignment.TopStart),
                    onSideBarItemClick = { item ->
                        navController.navigate(item.tag) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            else -> {}
        }
    }
}