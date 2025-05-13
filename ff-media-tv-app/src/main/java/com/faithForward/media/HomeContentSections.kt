package com.faithForward.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.carousel.CarouselContentRow
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomePageItem

@Composable
fun HomeContentSections(
    modifier: Modifier = Modifier,
    homePageItems: List<HomePageItem>,
    onChangeContentRowFocusedIndex: (Int) -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(unFocusMainColor)
            .padding(start = 63.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {


        itemsIndexed(homePageItems) { rowIndex, homePageItem ->
            val shouldFocusOnFirstItem = rowIndex == 0

            when (homePageItem) {
                is HomePageItem.CarouselRow -> CarouselContentRow(
                    carouselList = homePageItem.dto.carouselItemsDto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem
                )

                is HomePageItem.CategoryRow -> CategoryRow(
                    categoryRowDto = homePageItem.dto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem
                )

                is HomePageItem.PosterRow -> ContentRow(
                    posterRowDto = homePageItem.dto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem,
                    onChangeContentRowFocusedIndex = { index ->
                        onChangeContentRowFocusedIndex.invoke(index)
                    }
                )
            }
        }

    }
}