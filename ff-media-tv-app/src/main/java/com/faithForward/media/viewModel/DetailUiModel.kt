package com.faithForward.media.viewModel

import androidx.compose.ui.graphics.Color
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.DetailDto
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.network.dto.detail.CardDetail


sealed interface DetailPageItem {
    data class CardWithRelated(
        val detailDto: DetailDto,
        val relatedList: List<PosterCardDto> = emptyList(),
    ) : DetailPageItem
}

fun CardDetail.toDetailDto(): DetailDto {
    return DetailDto(
        imgSrc = data.landscape,
        title = data.name,
        description = data.description,
        releaseDate = data.dateUploaded,
        genre = data.genres?.mapNotNull { it.name }  // safely extract non-null names
            ?.joinToString(", "),
        duration = data.duration.toString(),
        imdbRating = data.rating,
    )
}


// Data class for UI-specific states
data class UiState(
    val contentColor: Color = Color.Black,
    val buttonUnfocusedColor: Color = Color.White,
    val textUnfocusedColor: Color = textUnFocusColor, // Replace with your textUnFocusColor
    val contentRowTint: Color = Color.White,
    val relatedContentColor: Color = Color.Black,
    val targetHeight: Int = 250,
)

// Sealed class for UI events
sealed class DetailScreenEvent {
    data class LoadCardDetail(val id: String, val relatedList: List<PosterCardDto>) :
        DetailScreenEvent()

    data class RelatedRowFocusChanged(val hasFocus: Boolean) : DetailScreenEvent()
    data object RelatedRowUpClick : DetailScreenEvent()
}