package com.faithForward.media.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.util.Resource

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    changeSideBarSelectedPosition: (Int) -> Unit,
    onDataLoadedSuccess: () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current


    LaunchedEffect(Unit) {
        homeViewModel.fetchHomePageData(sectionId = 1)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.e("SIDE_BAR", "home page compose on resume is called")
                changeSideBarSelectedPosition.invoke(1)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    val homePageItemsResource by homeViewModel.homePageData.collectAsStateWithLifecycle()

    if (homePageItemsResource is Resource.Unspecified
        || homePageItemsResource is Resource.Error
        || homePageItemsResource is Resource.Loading
    ) return

    val homePageItems = homePageItemsResource.data ?: return

    if (homePageItemsResource is Resource.Success) {
        onDataLoadedSuccess.invoke()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
    ) {
        HomeContentSections(
            modifier = Modifier,
            homePageItems = homePageItems,
            onChangeContentRowFocusedIndex = { index ->
                homeViewModel.onContentRowFocusedIndexChange(index)
            }
        )
    }

}