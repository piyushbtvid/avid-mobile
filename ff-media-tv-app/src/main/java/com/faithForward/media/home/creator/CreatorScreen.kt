package com.faithForward.media.home.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.home.HomeContentSections
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.viewModel.CreatorViewModel
import com.faithForward.util.Resource

@Composable
fun CreatorScreen(
    modifier: Modifier = Modifier,
    creatorViewModel: CreatorViewModel,
) {


    LaunchedEffect(Unit) {
        creatorViewModel.fetchCreatorData(1)
    }

    val creatorPageItemsResource by creatorViewModel.creatorPageData.collectAsStateWithLifecycle()

    if (creatorPageItemsResource is Resource.Unspecified
        || creatorPageItemsResource is Resource.Error ||
        creatorPageItemsResource is Resource.Loading
    ) return

    val creatorPageItems = creatorPageItemsResource.data ?: return

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        HomeContentSections(
            modifier = Modifier,
            homePageItems = creatorPageItems,
            onChangeContentRowFocusedIndex = { index ->
                creatorViewModel.onContentRowFocusedIndexChange(index)
            }
        )
    }

}