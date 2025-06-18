package com.faithForward.media.home.creator

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.home.HomeContentSections
import com.faithForward.media.home.creator.card.CreatorCardDto
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.media.viewModel.SideBarViewModel
import com.faithForward.util.Resource

@Composable
fun CreatorScreen(
    modifier: Modifier = Modifier,
    creatorViewModel: CreatorViewModel,
    sideBarViewModel: SideBarViewModel,
    onBackClick: () -> Unit,
    onCreatorItemClick: (CreatorCardDto) -> Unit,
) {


//    LaunchedEffect(Unit) {
//        creatorViewModel.fetchCreatorData(1)
//    }

    val creatorPageItemsResource by creatorViewModel.creatorPageData.collectAsStateWithLifecycle()

    if (creatorPageItemsResource is Resource.Unspecified
        || creatorPageItemsResource is Resource.Error ||
        creatorPageItemsResource is Resource.Loading
    ) return

    val creatorPageItems = creatorPageItemsResource.data ?: return
    val sideBarState by sideBarViewModel.sideBarState

    BackHandler {
        Log.e("ON_BACK", "on back in creator called")
        if (sideBarState.sideBarFocusedIndex != -1) {
            Log.e(
                "ON_BACK",
                "on back in home called with side Bar focused index ${sideBarState.sideBarFocusedIndex}"
            )
            onBackClick.invoke()
        } else {
            sideBarViewModel.onEvent(SideBarEvent.ChangeFocusedIndex(3))
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor)
    ) {
        HomeContentSections(
            modifier = Modifier,
            sideBarViewModel = sideBarViewModel,
            homePageItems = creatorPageItems,
            onChangeContentRowFocusedIndex = { index ->
                creatorViewModel.onContentRowFocusedIndexChange(index)
            },
            onCategoryItemClick = {

            },
            onItemClick = { item, list, id ->
                Log.e("CREATOR_DETAIL", "creator id when click in grid item is ${item.id}")
            },
            onToggleFavorite = {

            },
            onToggleLike = {

            },
            onToggleDisLike = {

            },
            onCarouselItemClick = {

            },
            onCreatorItemClick = onCreatorItemClick
        )
    }
}