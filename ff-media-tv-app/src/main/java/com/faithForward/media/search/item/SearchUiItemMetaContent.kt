package com.faithForward.media.search.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.whiteMain

@Composable
fun SearchUiItemMetaContent(
    modifier: Modifier = Modifier,
    imdb: String? = null,
    duration: String? = null,
    genre: String? = null,
    title: String? = null,
    creatorName: String? = null,
    creatorViews: String? = null,
    creatorUploadDate: String? = null,
    creatorVideoNumber: String? = null,
    seasonNumber: String? = null,
) {

    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {


        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            if (imdb != null) {
                ImdbBox(text = imdb)
            }

            if (duration != null) {
                TitleText(
                    text = duration,
                    color = whiteMain,
                    textSize = 15,
                    lineHeight = 15,
                    fontWeight = FontWeight.W500
                )
            }

            if (seasonNumber != null) {
                TitleText(
                    text = seasonNumber,
                    color = whiteMain,
                    textSize = 15,
                    lineHeight = 15,
                    fontWeight = FontWeight.W500
                )
            }

            if (creatorVideoNumber != null) {
                TitleText(
                    text = creatorVideoNumber,
                    color = whiteMain,
                    textSize = 15,
                    lineHeight = 15,
                    fontWeight = FontWeight.W500
                )


            }

            if (title != null) {
                TitleText(
                    text = title,
                    color = whiteMain,
                    textSize = 15,
                    lineHeight = 15,
                    fontWeight = FontWeight.W500
                )
            }

        }


        if (genre != null) {
            TitleText(
                text = "Genre:  $genre",
                color = whiteMain,
                textSize = 10,
                lineHeight = 10,
                fontWeight = FontWeight.Medium
            )
        }


        if (creatorName != null) {
            TitleText(
                text = "Creator:  $creatorName",
                color = whiteMain,
                textSize = 10,
                lineHeight = 10,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            if (creatorViews != null) {
                TitleText(
                    text = creatorViews,
                    color = whiteMain,
                    textSize = 10,
                    lineHeight = 10,
                    fontWeight = FontWeight.Medium
                )
            }

            if (creatorUploadDate != null) {
                TitleText(
                    text = creatorUploadDate,
                    color = whiteMain,
                    textSize = 10,
                    lineHeight = 10,
                    fontWeight = FontWeight.Medium
                )
            }

        }

    }

}


@Preview
@Composable
private fun SearchMetaDataPreview() {

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        SearchUiItemMetaContent(
            imdb = "PG",
            duration = "1h 48m",
            title = "The Last Ride",
            genre = "Drama,Comedy,Thriler"
        )

        SearchUiItemMetaContent(
            imdb = "Tv-14",
            title = "The Last Ride",
            creatorViews = "300k Views",
            creatorName = "Terrel Jones",
            creatorUploadDate = "5 month ago",
            creatorVideoNumber = "16 Videos"
        )

        SearchUiItemMetaContent(
            imdb = "PG",
            seasonNumber = "6 Seasons",
            title = "The Last Ride",
            genre = "Drama,Comedy,Thriler"
        )

    }
}