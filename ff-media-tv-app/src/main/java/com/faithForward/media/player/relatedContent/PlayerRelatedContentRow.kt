package com.faithForward.media.player.relatedContent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

data class PlayerRelatedContentRowDto(
    val title: String,
    val rowList: List<RelatedContentItemDto>,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerRelatedContentRow(
    modifier: Modifier = Modifier,
    onItemClick: (RelatedContentItemDto) -> Unit,
    onUp: () -> Boolean = {
        false
    },
    playerRelatedContentRowDto: PlayerRelatedContentRowDto,
) {

    var contentRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val itemFocusRequesters =
        remember { List(playerRelatedContentRowDto.rowList.size) { FocusRequester() } }

    with(playerRelatedContentRowDto) {

        Column(
            modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            TitleText(
                text = title, modifier = Modifier.padding(start = 25.dp), color = whiteMain
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRestorer {
                        itemFocusRequesters[0]
                    },
                contentPadding = PaddingValues(start = 25.dp, end = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                itemsIndexed(rowList) { index, item ->


                    val uiState = when (index) {
                        contentRowFocusedIndex -> FocusState.FOCUSED
                        else -> FocusState.UNFOCUSED
                    }

                    RelatedContentItem(modifier = Modifier
                        .focusRequester(itemFocusRequesters[index])
                        .onFocusChanged {
                            if (it.hasFocus) {
//                                onItemFocused(Pair(rowIndex, index))
                                contentRowFocusedIndex = index
                                //  onChangeContentRowFocusedIndex.invoke(index)
                                //  playListItemFocusedIndex = index
                                //onBackgroundChange.invoke(playlistItem.landscape ?: "")
                            } else {
                                if (contentRowFocusedIndex == index) {
                                    contentRowFocusedIndex = -1
                                    //  onChangeContentRowFocusedIndex.invoke(index)
                                    // playListItemFocusedIndex = -1
                                }
                            }
                        }
                        .focusable()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionUp || keyEvent.key == Key.DirectionDown) {
                                Log.e("ON_UP", "on up in related row item")
                                onUp.invoke()
                                true
                            } else {
                                false
                            }
                        },
                        focusState = uiState,
                        relatedContentItemDto = item,
                        onItemClick = onItemClick
                    )
                }
                item {
                    Spacer(modifier = Modifier.width(120.dp))
                }
            }
        }

        LaunchedEffect(Unit) {
            try {
                Log.e("PLAYER_RELATED", "player related content request focus called")
                itemFocusRequesters[0].requestFocus()
            } catch (ex: Exception) {
                Log.e("nnn", "${ex.message}")
            }
        }

    }

}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 1280,
    heightDp = 720,
    name = "Landscape Preview"
)
@Composable
private fun RowPreview() {
    val ls = listOf(
        RelatedContentItemDto(
            image = "",
            id = "",
            slug = "",
            title = "The Last Ride",
            description = ""
        ),

        RelatedContentItemDto(
            image = "",
            id = "",
            slug = "",
            title = "The Last Ride",
            description = ""
        ),
        RelatedContentItemDto(
            image = "",
            id = "",
            slug = "",
            title = "The Last Ride",
            description = ""
        ),
        RelatedContentItemDto(
            image = "",
            id = "",
            slug = "",
            title = "The Last Ride",
            description = ""
        ),
        RelatedContentItemDto(
            image = "",
            id = "",
            slug = "",
            title = "The Last Ride",
            description = ""
        ),
    )

    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize() // Optional: makes sure it takes available space
        , contentAlignment = Alignment.Center
    ) {
        PlayerRelatedContentRow(
            playerRelatedContentRowDto = PlayerRelatedContentRowDto(
                title = "Next Up...", rowList = ls
            ), onItemClick = {

            }
        )
    }


}