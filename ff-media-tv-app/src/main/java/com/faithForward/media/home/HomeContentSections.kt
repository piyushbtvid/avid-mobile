package com.faithForward.media.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
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
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
    onChangeContentRowFocusedIndex: (Int) -> Unit,
) {

    val listState = rememberLazyListState()
    val focusRequesters = remember { mutableMapOf<Pair<Int, Int>, FocusRequester>() }
    var lastFocusedItem by rememberSaveable { mutableStateOf(Pair(0, 0)) }

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
                    rowIndex = rowIndex,
                    focusRequesters = focusRequesters,
                    lastFocusedItem = lastFocusedItem,
                    onItemFocused = { newFocus ->
                        lastFocusedItem = newFocus
                    },
                    listState = listState
                )

                is HomePageItem.CategoryRow -> CategoryRow(
                    categoryRowDto = homePageItem.dto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem,
                    rowIndex = rowIndex,
                    focusRequesters = focusRequesters,
                    lastFocusedItem = lastFocusedItem,
                    onItemFocused = { newFocus ->
                        lastFocusedItem = newFocus
                    },
                    onCategoryItemClick = onCategoryItemClick
                )

                is HomePageItem.PosterRow -> ContentRow(posterRowDto = homePageItem.dto,
                    shouldFocusOnFirstItem = shouldFocusOnFirstItem,
                    onItemClick = onItemClick,
                    rowIndex = rowIndex,
                    focusRequesters = focusRequesters,
                    lastFocusedItem = lastFocusedItem,
                    onItemFocused = { newFocus ->
                        lastFocusedItem = newFocus
                    },
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

    LaunchedEffect(lastFocusedItem) {
        focusRequesters[lastFocusedItem]?.requestFocus()
    }
}