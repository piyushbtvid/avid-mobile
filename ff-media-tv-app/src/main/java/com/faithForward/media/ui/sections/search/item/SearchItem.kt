package com.faithForward.media.ui.sections.search.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.util.FocusState


data class SearchItemDto(
    val contentType: String? = null,
    val itemId: String? = null,
    val contentSlug: String? = null,
    val image: String? = null,
    val title: String? = null,
    val genre: String? = null,
    val imdb: String? = null,
    val duration: String? = null,
    val creatorName: String? = null,
    val creatorViews: String? = null,
    val creatorUploadDate: String? = null,
    val creatorVideoNumber: String? = null,
    val seasonNumber: String? = null,
)

@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    searchItemDto: SearchItemDto,
    onItemClick: () -> Unit,
    focusState: FocusState,
) {

    with(searchItemDto) {
        Column(
            modifier = modifier.wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {


            SearchImageCard(
                searchItemDto = searchItemDto,
                focusState = focusState,
                onItemClick = onItemClick
            )

            TitleText(
                modifier = Modifier.width(135.dp),
                text = title ?: "",
                color = Color.White,
                fontWeight = FontWeight.W400,
                textSize = 9
            )

        }
    }

}


@Composable
fun SearchUiItem(
    modifier: Modifier = Modifier,
    searchItemDto: SearchItemDto,
    onItemClick: () -> Unit,
    focusState: FocusState,
) {


    with(searchItemDto) {

        Row(
            modifier = modifier
                .width(450.dp)
                .wrapContentHeight()
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchImage(
                imageSrc = image
            )

            SearchUiItemMetaContent(
                imdb = imdb,
                title = title,
                duration = duration,
                creatorUploadDate = creatorUploadDate,
                creatorName = creatorName,
                creatorViews = creatorViews,
                creatorVideoNumber = creatorVideoNumber,
                seasonNumber = seasonNumber,
                genre = genre,
            )
        }
    }

}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun SearchItemPreview() {
//
//    SearchItem(
//        searchItemDto = SearchItemDto(
//            title = "The Saga of water lbsdlibsd lbaslbclblbs ljbslbf   ,bdclibsaf   aslbavslb"
//        ),
//        focusState = FocusState.UNFOCUSED,
//        onItemClick = {
//
//        }
//    )
//
//}

@Preview
@Composable
private fun SearchUiItemPreview() {

    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        SearchUiItem(
            searchItemDto = SearchItemDto(
                title = "The Saga of water lbsdlibsd lbaslbclblbs ljbslbf   ,bdclibsaf   aslbavslb",
                imdb = "PG",
                duration = "1h 48m",
                genre = "Drama,Comedy,Thriler"
            ),
            focusState = FocusState.UNFOCUSED,
            onItemClick = {

            }
        )

        SearchUiItem(
            searchItemDto = SearchItemDto(
                imdb = "Tv-14",
                title = "The Last Ride",
                creatorViews = "300k Views",
                creatorName = "Terrel Jones",
                creatorUploadDate = "5 month ago",
                creatorVideoNumber = "16 Videos"
            ),
            focusState = FocusState.UNFOCUSED,
            onItemClick = {

            }
        )


        SearchUiItem(
            searchItemDto = SearchItemDto(
                imdb = "PG",
                seasonNumber = "6 Seasons",
                title = "The Last Ride",
                genre = "Drama,Comedy,Thriler"
            ),
            focusState = FocusState.UNFOCUSED,
            onItemClick = {

            }
        )
    }
}