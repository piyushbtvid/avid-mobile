package com.faithForward.media.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faithForward.media.home.carousel.CarouselContentRow
import com.faithForward.media.home.category.CategoryRow
import com.faithForward.media.home.content.ContentRow
import com.faithForward.media.home.creator.list.CreatorCardGrid
import com.faithForward.media.viewModel.HomePageItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContentSections(
    modifier: Modifier = Modifier,
    homePageItems: List<HomePageItem>,
    onCategoryItemClick: (String) -> Unit,
    onChangeContentRowFocusedIndex: (Int) -> Unit
) {

    val listState = rememberLazyListState()

//    LaunchedEffect(Unit) {
//        while (true){
//            delay(300)
//            listState.scrollToItem(0)
//        }
//    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 63.dp),
        state = listState,
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {


        itemsIndexed(homePageItems) { rowIndex, homePageItem ->
            val shouldFocusOnFirstItem = rowIndex == 0

            when (homePageItem) {
                is HomePageItem.CarouselRow -> CarouselContentRow(
                    carouselList = homePageItem.dto.carouselItemsDto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem,
                    listState = listState
                )

                is HomePageItem.CategoryRow -> CategoryRow(
                    categoryRowDto = homePageItem.dto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem,
                    onCategoryItemClick = onCategoryItemClick
                )

                is HomePageItem.PosterRow -> ContentRow(posterRowDto = homePageItem.dto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem,
                    onChangeContentRowFocusedIndex = { index ->
                        onChangeContentRowFocusedIndex.invoke(index)
                    })

                is HomePageItem.CreatorGrid -> {
                    CreatorCardGrid(
                        creators = homePageItem.dto
                    )
                }
            }
        }

    }
}