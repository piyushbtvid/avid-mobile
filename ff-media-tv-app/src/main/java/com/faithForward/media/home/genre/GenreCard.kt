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
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.util.FocusState

data class GenreCardDto(
    val genreId: String,
    val name: String,
    val description: String,
    val image: String,
    val views: String
)

@Composable
fun GenreCard(
    modifier: Modifier = Modifier,
    genreCardDto: GenreCardDto,
    focusState: FocusState,
) {

    Column(
        modifier = Modifier.wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GenreImageCard(
            modifier = modifier,
            posterCardDto = GenreImageCardDto(
                posterImageSrc = genreCardDto.image
            ),
            focusState = focusState,
        )
        Spacer(modifier = Modifier.height(19.dp))
        Column(
            modifier = Modifier.width(135.dp)
        ) {
            TitleText(
                text = genreCardDto.description,
                color = Color.Black,
                fontWeight = FontWeight.W600,
                textSize = 10
            )
            Spacer(modifier = Modifier.height(1.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleText(
                    text = genreCardDto.name,
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    textSize = 7
                )
                TitleText(
                    text = genreCardDto.views.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    textSize = 7
                )
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
            ),
            focusState = FocusState.UNFOCUSED
        )
    }
}