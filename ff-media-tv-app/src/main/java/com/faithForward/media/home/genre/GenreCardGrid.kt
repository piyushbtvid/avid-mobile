package com.faithForward.media.home.genre

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.commanComponents.RoundedIconButton
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.homeBackgroundColor
import com.faithForward.media.theme.textFocusedMainColor
import com.faithForward.media.util.FocusState


data class GenreGridDto(
    val title: String,
    val genreCardList: List<GenreCardDto>
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GenreCardGrid(
    modifier: Modifier = Modifier,
    genreGridDto: GenreGridDto,
) {

    var contentRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var isMicFocused by rememberSaveable { mutableStateOf(false) }
    var isSearchFocused by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberLazyGridState()
    val gridFocusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(homeBackgroundColor)
            .padding(start = 41.dp),

        ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RoundedIconButton(
                    modifier = Modifier
                        .onFocusChanged {
                            isMicFocused = it.hasFocus
                        }
                        .then(
                            if (isMicFocused) {
                                Modifier
                                    .shadow(
                                        color = Color.White.copy(alpha = .11f),
                                        borderRadius = 40.dp,
                                        blurRadius = 7.dp,
                                        spread = 5.dp,
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = textFocusedMainColor,
                                        shape = RoundedCornerShape(40.dp)
                                    )
                            } else Modifier
                        ),
                    imageId = R.drawable.microphone_ic,
                    iconHeight = 15,
                    boxSize = 43,
                    iconWidth = 15,
                    backgroundColor = Color.White.copy(alpha = .75f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                RoundedIconButton(
                    modifier = Modifier
                        .onFocusChanged {
                            isSearchFocused = it.hasFocus
                        }
                        .then(
                            if (isSearchFocused) {
                                Modifier
                                    .shadow(
                                        color = Color.White.copy(alpha = .11f),
                                        borderRadius = 40.dp,
                                        blurRadius = 7.dp,
                                        spread = 5.dp,
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = textFocusedMainColor,
                                        shape = RoundedCornerShape(40.dp)
                                    )
                            } else Modifier
                        ),
                    imageId = R.drawable.search_ic,
                    iconHeight = 15,
                    boxSize = 43,
                    iconWidth = 15,
                    backgroundColor = Color.White.copy(alpha = .75f)
                )

            }

            Column(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.back_ic),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    TitleText(
                        text = genreGridDto.title,
                        color = Color.Black,
                        fontWeight = FontWeight.W600,
                        textSize = 30
                    )
                }
                LazyVerticalGrid(
                    state = scrollState,
                    columns = GridCells.Fixed(5),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        bottom = 101.dp, start = 47.5.dp, end = 140.dp, top = 20.dp
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRestorer()
                ) {
                    itemsIndexed(genreGridDto.genreCardList) { index, genreCardItem ->

                        val uiState = when (index) {
                            contentRowFocusedIndex -> FocusState.FOCUSED
                            else -> FocusState.UNFOCUSED
                        }
                        GenreCard(
                            modifier = Modifier
                                .focusRequester(if (index == 0) gridFocusRequester else FocusRequester())
                                .onFocusChanged {
                                    if (it.hasFocus) {
                                        contentRowFocusedIndex = index
                                    } else {
                                        if (contentRowFocusedIndex == index) {
                                            contentRowFocusedIndex = -1
                                        }
                                    }
                                }
                                .focusable(),
                            genreCardDto = genreCardItem,
                            focusState = uiState,
                        )
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun GenreGridPreview() {
    GenreCardGrid(
        genreGridDto = GenreGridDto(
            title = "Podcast",
            genreCardList = listOf(
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
                GenreCardDto(
                    genreId = "1",
                    name = "The Last Horizon",
                    description = "A sci-fi adventure exploring the edge of human survival in space.",
                    image = "",
                    views = "300k Views",
                ),
            ),
        )
    )
}