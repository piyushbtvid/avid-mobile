package com.faithForward.media.detail

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.util.FocusState

data class RelatedContentRowDto(
    val heading: String,
    val relatedContentDto: List<PosterCardDto>,
)

@Composable
fun RelatedContentRow(
    modifier: Modifier = Modifier,
    relatedContentRowDto: RelatedContentRowDto,
) {

    var relatedRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var currentFocusedItem by remember { mutableStateOf<PosterCardDto?>(null) }
    var isRelatedTextFocused by remember { mutableStateOf(false) }

    LaunchedEffect(relatedRowFocusedIndex) {
        if (relatedRowFocusedIndex > 0) {
            currentFocusedItem = relatedContentRowDto.relatedContentDto.get(relatedRowFocusedIndex)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TitleText(
            text = relatedContentRowDto.heading,
            color = if (isRelatedTextFocused) focusedMainColor else Color.Black,
            fontWeight = if (isRelatedTextFocused) FontWeight.W600 else FontWeight.Normal,
            modifier = Modifier
                .padding(start = 25.dp)
                .onFocusChanged {
                    isRelatedTextFocused = it.hasFocus
                }

        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 25.dp, end = 20.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        )
        {
            itemsIndexed(relatedContentRowDto.relatedContentDto) { index, relatedContentItem ->

                val uiState = when (index) {
                    relatedRowFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                RelatedContent(
                    modifier = Modifier
                        .onFocusChanged {
                            if (it.hasFocus) {
                                relatedRowFocusedIndex = index
                            } else {
                                if (relatedRowFocusedIndex == index) {
                                    relatedRowFocusedIndex = -1
                                }
                            }
                        }
                        .focusable(),
                    relatedContentDto = relatedContentItem,
                    uiState = uiState
                )
            }
        }
        with(currentFocusedItem) {
//            ContentMetaBlock(
//                modifier = Modifier
//                    .padding(start = 20.dp, top = 20.dp)
//                    .wrapContentHeight(),
//                description = description,
//                releaseDate = releaseDate,
//                genre = genre,
//                seasons = seasons,
//                duration = duration,
//                subscribers = subscribers,
//                imdbRating = imdbRating,
//                title = title,
//                textColor = Color.Black,
//                buttonModifier = modifier,
//                addToWatchListModifier = addToWatchListModifier,
//                likeModifier = likeModifier,
//                disLikeModifier = disLikeModifier,
//                addToWatchListUiState = addToWatchListUiState,
//                likeUiState = likeUiState,
//                dislikeUiState = dislikeUiState
//            )
        }
    }

}