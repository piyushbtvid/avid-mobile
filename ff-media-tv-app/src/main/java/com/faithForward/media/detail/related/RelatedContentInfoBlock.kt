package com.faithForward.media.detail.related

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.home.carousel.ContentMetaBlock
import com.faithForward.media.theme.whiteMain

@Composable
fun RelatedContentInfoBlock(
    modifier: Modifier = Modifier,
    currentFocusedItem: PosterCardDto?,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        with(currentFocusedItem) {
            TitleText(
                textSize = 18,
                modifier = Modifier
                    .padding(start = 20.dp),
                text = this?.title ?: "",
                fontWeight = FontWeight.W600,
                color = whiteMain
            )
            ContentMetaBlock(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .wrapContentHeight(),
                description = this?.description,
                title = null,
                textColor = whiteMain,
                buttonModifier = modifier,
                releaseDate = this?.releaseDate,
                imdbRating = this?.imdbRating,
                duration = this?.duration,
                genre = this?.genre,
                seasons = this?.seasons
            )
        }
    }

}