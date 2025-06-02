package com.faithForward.media.viewModel

import androidx.compose.ui.graphics.Color
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.detail.DetailDto
import com.faithForward.media.detail.SeasonDto
import com.faithForward.media.detail.SeasonsNumberDto
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.network.dto.detail.CardDetail
import com.faithForward.network.dto.series.Episode
import com.faithForward.network.dto.series.Season


sealed interface DetailPageItem {
    data class CardWithRelated(
        val detailDto: DetailDto,
        val relatedList: List<PosterCardDto> = emptyList(),
        val seasonNumberList: List<SeasonsNumberDto>? = emptyList(),
        val seasonList: List<SeasonDto>? = emptyList(),
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

fun Season.toSeasonDto(): SeasonDto {
    return SeasonDto(episodesContentDto = episodes.map {
        it.toPosterDto()
    })
}

fun Season.toSeasonNumberDto() : SeasonsNumberDto {
    return  SeasonsNumberDto(
        seasonNumber = season_number
    )
}

fun Episode.toPosterDto(): PosterCardDto {
    return PosterCardDto(id = id,
        posterImageSrc = landscape,
        title = name,
        description = description,
        genre = genres.mapNotNull { it.name }  // safely extract non-null names
            .joinToString(", "),
        duration = duration.toString(),
        imdbRating = rating,
        releaseDate = dateUploaded)
}


// Data class for UI-specific states
data class UiState(
    val contentColor: Color = Color.Black,
    val buttonUnfocusedColor: Color = Color.White,
    val textUnfocusedColor: Color = textUnFocusColor, // Replace with your textUnFocusColor
    val contentRowTint: Color = Color.White,
    val relatedContentColor: Color = Color.Black,
    val targetHeight: Int = 280,
)

// Sealed class for UI events
sealed class DetailScreenEvent {
    data class LoadCardDetail(val id: String, val relatedList: List<PosterCardDto>) :
        DetailScreenEvent()

    data class RelatedRowFocusChanged(val hasFocus: Boolean) : DetailScreenEvent()
    data object RelatedRowUpClick : DetailScreenEvent()
}