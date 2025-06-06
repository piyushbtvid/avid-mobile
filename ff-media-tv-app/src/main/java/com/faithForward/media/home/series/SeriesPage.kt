package com.faithForward.media.home.series

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.home.HomeContentSections
import com.faithForward.media.viewModel.ContentViewModel
import com.faithForward.util.Resource

@Composable
fun SeriesPage(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
) {

    LaunchedEffect(Unit) {
        contentViewModel.loadSectionContent("series", "Series")
    }


    val homePageItemsResource by contentViewModel.homePageData.collectAsStateWithLifecycle()

    if (homePageItemsResource is Resource.Unspecified
        || homePageItemsResource is Resource.Error
        || homePageItemsResource is Resource.Loading
    ) return

    val homePageItems = homePageItemsResource.data ?: return

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        HomeContentSections(
            modifier = Modifier,
            homePageItems = homePageItems,
            onChangeContentRowFocusedIndex = { index ->
                contentViewModel.onContentRowFocusedIndexChange(index)
            },
            onCategoryItemClick = {

            },
            onItemClick = { item, list ->
                onItemClick.invoke(item, list)
            }
        )
    }

}