package com.faithForward.media.home.genre

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.viewModel.GenreViewModel
import com.faithForward.media.viewModel.uiModels.toPosterCardDto
import com.faithForward.util.Resource

@Composable
fun GenreDataScreen(
    modifier: Modifier = Modifier,
    genreId: String,
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
    viewModel: GenreViewModel,
) {

    LaunchedEffect(Unit) {
        viewModel.getGivenGenreDetail(genreId)
    }

    val genreDataResource by viewModel.genreData.collectAsStateWithLifecycle()

    if (genreDataResource is Resource.Unspecified || genreDataResource is Resource.Error || genreDataResource is Resource.Loading) return

    val genreData = genreDataResource.data ?: return

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        GenreCardGrid(
            genreGridDto = genreData,
            onItemClick = { item ->
                val newPosterCardDtoList = genreData.genreCardList.map {
                    it.toPosterCardDto()
                }
                onItemClick.invoke(item, newPosterCardDtoList)
            }
        )
    }

}