package com.faithForward.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.carousel.CarouselContentRow
import com.faithForward.media.carousel.CarouselContentRowDto
import com.faithForward.media.carousel.CarouselItemDto
import com.faithForward.media.components.TitleText
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomePageItem
import com.faithForward.media.viewModel.HomeViewModel
import com.faithForward.util.Resource

@Composable
fun HomeContentSections(
    modifier: Modifier = Modifier, homeViewModel: HomeViewModel
) {

    LaunchedEffect(Unit) {
        homeViewModel.fetchHomePageData(sectionId = 1)
    }

    val homePageItemsResource by homeViewModel.homePageData.collectAsStateWithLifecycle()

    if (homePageItemsResource is Resource.Unspecified
        || homePageItemsResource is Resource.Error
        || homePageItemsResource is Resource.Loading
    ) return

    val homePageItems = homePageItemsResource.data ?: return




    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(unFocusMainColor)
            .padding(start = 63.dp),
    ) {


        itemsIndexed(homePageItems) { rowIndex, homePageItem ->

            when (homePageItem) {
                is HomePageItem.CarouselRow -> CarouselContentRow(
                    carouselList = homePageItem.dto.carouselItemsDto,
                )

                is HomePageItem.CategoryRow -> CategoryRow(
                    categoryRowDto = homePageItem.dto
                )

                is HomePageItem.PosterRow -> ContentRow(
                    posterRowDto = homePageItem.dto,
                    onChangeContentRowFocusedIndex = { index ->
                        homeViewModel.onContentRowFocusedIndexChange(index)
                    })
            }
        }
    }
}