package com.faithForward.media.home.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.home.HomeContentSections
import com.faithForward.media.viewModel.MoviesViewModel
import com.faithForward.util.Resource

@Composable
fun MoviesPage(
    modifier: Modifier = Modifier,
    moviesViewModel: MoviesViewModel,
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
) {


    val homePageItemsResource by moviesViewModel.homePageData.collectAsStateWithLifecycle()

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
                moviesViewModel.onContentRowFocusedIndexChange(index)
            },
            onCategoryItemClick = {

            },
            onItemClick = { item, list ->
                onItemClick.invoke(item, list)
            }
        )
    }

}