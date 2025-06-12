package com.faithForward.media.home.series

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.home.HomeContentSections
import com.faithForward.media.viewModel.ContentViewModel
import com.faithForward.media.viewModel.uiModels.CarouselClickUiState
import com.faithForward.util.Resource

@Composable
fun SeriesPage(
    modifier: Modifier = Modifier,
    contentViewModel: ContentViewModel,
    onCarouselItemClick: (PosterCardDto) -> Unit,
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
) {

    val uiEvent by contentViewModel.uiEvent.collectAsStateWithLifecycle(null)
    val context = LocalContext.current
    val carouselClickUiState by contentViewModel.carouselClickUiState.collectAsState(null)


    val homePageItemsResource by contentViewModel.homePageData.collectAsState()

    if (homePageItemsResource is Resource.Unspecified
        || homePageItemsResource is Resource.Error
        || homePageItemsResource is Resource.Loading
    ) return

    val homePageItems = homePageItemsResource.data ?: return

    when (val state = carouselClickUiState) {
        is CarouselClickUiState.NavigateToPlayer -> {
            LaunchedEffect(state.posterCardDto) {
                onCarouselItemClick.invoke(state.posterCardDto)
                // Reset state to prevent repeated navigation
                contentViewModel.loadBannerDetail("") // Reset to idle
            }
        }

        is CarouselClickUiState.Idle -> {}
        null -> {

        }
    }

    // Showing Toast when uiEvent changes
    LaunchedEffect(uiEvent) {
        uiEvent?.let { event ->
            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            // Reset uiEvent to prevent repeated toasts (optional, depending on ViewModel reset)
            // detailViewModel.resetUiEvent()
        }
    }

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
            onItemClick = { item, list, id ->
                onItemClick.invoke(item, list)
            },
            onToggleFavorite = { slug ->
                if (slug != null) {
                    contentViewModel.toggleFavorite(slug)
                }
            },
            onToggleLike = { slug ->
                if (slug != null) {
                    contentViewModel.toggleLike(slug)
                }
            },
            onToggleDisLike = { slug ->
                if (slug != null) {
                    contentViewModel.toggleDislike(slug)
                }
            },
            onCarouselItemClick = { item ->
                if (item.slug != null) {
                    contentViewModel.loadBannerDetail(item.slug)
                }
            },
            onCreatorItemClick = {

            }
        )
    }

}