package com.faithForward.media.ui.sections.genre

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState
import com.faithForward.media.util.rememberIsTvDevice

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
    val contentType: String? = null,
)

@Composable
fun GenreCard(
    modifier: Modifier = Modifier,
    genreCardDto: GenreCardDto,
    onItemClick: () -> Unit,
    focusState: FocusState,
) {

    val isTv = rememberIsTvDevice()
    val scale by animateFloatAsState(
        targetValue = when {
            isTv && (focusState == FocusState.SELECTED || focusState == FocusState.FOCUSED) -> 1.13f
            else -> 1f
        }, animationSpec = tween(300), label = ""
    )

    Column(
        modifier = modifier
            .wrapContentWidth()
            .scale(scale)
            .zIndex(
                when {
                    isTv && (focusState == FocusState.SELECTED || focusState == FocusState.FOCUSED) -> 1f
                    else -> 0f
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        genreCardDto.image?.let {
            GenreImageCard(
                modifier = Modifier,
                posterCardDto = GenreImageCardDto(posterImageSrc = it),
                focusState = focusState,
                onItemClick = onItemClick,
                imageContentScale = if (genreCardDto.contentType == "Live Channel") ContentScale.Fit else ContentScale.FillBounds
            )
        }

        Column(modifier = Modifier.width(if (isTv)135.dp else 102.dp)) {
            genreCardDto.description?.let {
                TitleText(
                    text = it, color = whiteMain.copy(
                        alpha = 0.7f
                    ), fontWeight = FontWeight.W600, textSize = 10
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
                            text = it, color = whiteMain.copy(
                                alpha = 0.7f
                            ), fontWeight = FontWeight.W400, textSize = 7
                        )
                    }
                    genreCardDto.views?.let {
                        TitleText(
                            text = it, color = whiteMain.copy(
                                alpha = 0.7f
                            ), fontWeight = FontWeight.W400, textSize = 7
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
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        GenreCard(genreCardDto = GenreCardDto(
            genreId = "1",
            name = "The Last Horizon",
            description = "A sci-fi adventure exploring the edge of human survival in space.",
            image = "",
            views = "300k Views",
            slug = ""
        ), focusState = FocusState.UNFOCUSED, onItemClick = {

        })
    }
}