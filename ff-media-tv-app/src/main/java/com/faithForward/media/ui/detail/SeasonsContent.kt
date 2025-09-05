package com.faithForward.media.ui.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.PosterCardDto
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.detail.related.RelatedContentInfoBlock
import com.faithForward.media.ui.theme.focusedMainColor


data class SeasonsContentDto(
    val seasonsNumberDtoList: List<SeasonsNumberDto>,
    val listSeasonDto: List<SeasonDto>,
)

data class SeasonDto(
    val seasonNumber: Int,
    val trailer: String?,
    val episodesContentDto: List<PosterCardDto>,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SeasonsContent(
    modifier: Modifier = Modifier,
    seasonsContentDto: SeasonsContentDto,
    relatedContentColor: Color = Color.Black,
    onSeasonUpClick: () -> Boolean,
) {

    var relatedRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var currentFocusedItem by remember { mutableStateOf<PosterCardDto?>(null) }
    var isRelatedTextFocused by remember { mutableStateOf(false) }

    var focusedSeasonEpisodes: List<PosterCardDto> by remember {
        mutableStateOf(
            if (seasonsContentDto.listSeasonDto.isNotEmpty()) {
                seasonsContentDto.listSeasonDto[0].episodesContentDto
            } else {
                emptyList() // Default to an empty list if listSeasonDto is empty
            }
        )
    }

    LaunchedEffect(relatedRowFocusedIndex) {
        if (relatedRowFocusedIndex > -1) {
            currentFocusedItem = focusedSeasonEpisodes.get(relatedRowFocusedIndex)
            Log.e("CURRENT_FOCUS", "current focus item is ${currentFocusedItem?.title}")
        }
    }

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // Related Items Text Heading
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TitleText(text = "Seasons:",
                color = if (isRelatedTextFocused) focusedMainColor else Color.Black,
                fontWeight = if (isRelatedTextFocused) FontWeight.W600 else FontWeight.Normal,
                modifier = Modifier.onFocusChanged {
                    isRelatedTextFocused = it.hasFocus
                })

//            SeasonsNumberRow(
//                seasonsNumberDtoList = seasonsContentDto.seasonsNumberDtoList,
//                onSeasonUpClick = onSeasonUpClick,
//                onSeasonNumberChanged = { seasonNumber ->
//                    focusedSeasonEpisodes =
//                        seasonsContentDto.listSeasonDto[seasonNumber - 1].episodesContentDto
//                }
//            )

        }


        // Related Items Row
//        RelatedContentRow(
//            relatedContentRowDto = focusedSeasonEpisodes,
//            modifier = Modifier
//                .fillMaxWidth(),
//            relatedRowFocusedIndex = relatedRowFocusedIndex,
//            onRelatedUpClick = {
//                false
//            },
//            onRelatedRowFocusedIndexChange = { index ->
//                relatedRowFocusedIndex = index
//            },
//            onItemClick = {
//
//            }
//        )

        //Related Item Content Meta Data
        RelatedContentInfoBlock(
            modifier = Modifier.padding(top = 20.dp),
            currentFocusedItem = currentFocusedItem,
        )
    }

}