package com.faithForward.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.components.TitleText
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.viewModel.HomeViewModel

@Composable
fun HomeContentSections(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {

    val homeSectionData by homeViewModel.sectionData.collectAsStateWithLifecycle()

    val contentRowFocusedIndex = homeViewModel.contentRowFocusedIndex

    LaunchedEffect(Unit) {
        homeViewModel.getGivenSectionData(1)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(unFocusMainColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (homeSectionData.data != null) {
                itemsIndexed(homeSectionData.data!!.data) { rowIndex, item ->
                    Column {
                        TitleText(
                            text = item.title,
                            modifier = Modifier.padding(start = 88.dp)
                        )
                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        ContentRow(
                            contentList = item.items,
                            onChangeContentRowFocusedIndex = { index ->
                                homeViewModel.onContentRowFocusedIndexChange(index)
                            }
                        )
                    }
                }
            }
        }
    }
}