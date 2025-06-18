package com.faithForward.media.home.genre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

data class GenreCardDto(
    val genreId: String?,
    val name: String?,
    val image: String?,
    val views: String?,
    val description: String?,
    val genre: String? = null,
    val seasons: Int? = null,
    val duration: String? = null,
    val imdbRating: String? = null,
    val releaseDate: String? = null,
    val videoUrl: String? = null,
    val slug: String?,
)

@Composable
fun GenreCard(
    modifier: Modifier = Modifier,
    genreCardDto: GenreCardDto,
    onItemClick: () -> Unit,
    focusState: FocusState,
) {
    Column(
        modifier = Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        genreCardDto.image?.let {
            GenreImageCard(
                modifier = modifier,
                posterCardDto = GenreImageCardDto(posterImageSrc = it),
                focusState = focusState,
                onItemClick = onItemClick
            )
        }

        Spacer(modifier = Modifier.height(19.dp))

        Column(modifier = Modifier.width(135.dp)) {
            genreCardDto.description?.let {
                TitleText(
                    text = it,
                    color = whiteMain,
                    fontWeight = FontWeight.W600,
                    textSize = 10
                )
            }

            Spacer(modifier = Modifier.height(1.dp))

            if (!genreCardDto.name.isNullOrBlank() || !genreCardDto.views.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    genreCardDto.name?.let {
                        TitleText(
                            text = it,
                            color = whiteMain,
                            fontWeight = FontWeight.W400,
                            textSize = 7
                        )
                    }
                    genreCardDto.views?.let {
                        TitleText(
                            text = it,
                            color = whiteMain,
                            fontWeight = FontWeight.W400,
                            textSize = 7
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun GenreCardPreview(

) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GenreCard(
            genreCardDto = GenreCardDto(
                genreId = "1",
                name = "The Last Horizon",
                description = "A sci-fi adventure exploring the edge of human survival in space.",
                image = "",
                views = "300k Views",
                slug = ""
            ),
            focusState = FocusState.UNFOCUSED,
            onItemClick = {

            }
        )
    }
}