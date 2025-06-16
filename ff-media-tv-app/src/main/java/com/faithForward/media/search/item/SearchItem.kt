package com.faithForward.media.search.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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


data class SearchItemDto(
    val contentType: String? = null,
    val itemId: String? = null,
    val contentSlug: String? = null,
    val image: String? = null,
    val title: String? = null,
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchItemPreview() {

    SearchItem(
        searchItemDto = SearchItemDto(
            title = "The Saga of water lbsdlibsd lbaslbclblbs ljbslbf   ,bdclibsaf   aslbavslb"
        ),
        focusState = FocusState.UNFOCUSED,
        onItemClick = {

        }
    )

}