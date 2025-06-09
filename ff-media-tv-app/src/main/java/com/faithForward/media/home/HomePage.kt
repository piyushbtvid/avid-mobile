package com.faithForward.media.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.util.Resource

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    changeSideBarSelectedPosition: (Int) -> Unit,
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
    onCategoryClick: (String) -> Unit,
    onDataLoadedSuccess: () -> Unit,
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val uiEvent by homeViewModel.uiEvent.collectAsStateWithLifecycle(null)
    val context = LocalContext.current


//    LaunchedEffect(Unit) {
//        homeViewModel.fetchHomePageData(sectionId = 1)
//    }

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

    // Showing Toast when uiEvent changes
    LaunchedEffect(uiEvent) {
        uiEvent?.let { event ->
            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            // Reset uiEvent to prevent repeated toasts (optional, depending on ViewModel reset)
            // detailViewModel.resetUiEvent()
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(unFocusMainColor)
    ) {
        HomeContentSections(
            modifier = Modifier,
            homePageItems = homePageItems,
            onChangeContentRowFocusedIndex = { index ->
                homeViewModel.onContentRowFocusedIndexChange(index)
            },
            onItemClick = onItemClick,
            onCategoryItemClick = { id ->
                onCategoryClick.invoke(id)
            },
            onToggleFavorite = { slug ->
                if (slug != null) {
                    homeViewModel.toggleFavorite(slug)
                }
            },
            onToggleLike = { slug ->
                if (slug != null) {
                    homeViewModel.toggleLike(slug)
                }
            },
            onToggleDisLike = { slug ->
                if (slug != null) {
                    homeViewModel.toggleDislike(slug)
                }
            }
        )
    }

}