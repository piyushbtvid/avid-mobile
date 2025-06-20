package com.faithForward.media.detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

data class SeasonsNumberDto(
    val seasonNumber: String,
)

@Composable
fun SeasonsNumberRow(
    seasonsNumberDtoList: List<SeasonsNumberDto>,
    onSeasonUpClick: () -> Boolean,
    onSeasonNumberChanged: (String) -> Unit,
    onLastSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    seasonFocusRequesters: List<FocusRequester> = List(seasonsNumberDtoList.size) { FocusRequester() },
) {
    var seasonNumberFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    LazyRow(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(seasonsNumberDtoList) { index, seasonNumberItem ->
            val uiState = when (index) {
                seasonNumberFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            TitleText(
                modifier = Modifier
                    .focusRequester(seasonFocusRequesters[index])
                    .onFocusChanged {
                        if (it.hasFocus) {
                            seasonNumberFocusedIndex = index
                            onSeasonNumberChanged.invoke(seasonNumberItem.seasonNumber)
                            onLastSelectedIndexChange.invoke(index)
                        } else {
                            if (seasonNumberFocusedIndex == index) {
                                seasonNumberFocusedIndex = -1
                            }
                        }
                    }
                    .focusable()
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.DirectionUp) {
                            Log.e("SEASON", "on Up Click is called in seasonNumberRow")
                            return@onKeyEvent onSeasonUpClick()
                        } else {
                            Log.e("SEASON", "on Up Click is called in else")
                            false
                        }
                    }
                    .clickable(interactionSource = null, indication = null, onClick = {
                        // onSeasonNumberChanged.invoke(seasonNumberItem.seasonNumber)
                    }),
                text = seasonNumberItem.seasonNumber.toString(),
                textSize = if (uiState == FocusState.FOCUSED) 18 else 15,
                color = if (uiState == FocusState.FOCUSED || uiState == FocusState.SELECTED) whiteMain else whiteMain.copy(
                    alpha = 0.5f
                ),
                fontWeight = if (uiState == FocusState.FOCUSED || uiState == FocusState.SELECTED) FontWeight.W600
                else FontWeight.W400
            )
        }
    }
}

@Preview(
    name = "Landscape Preview",
    widthDp = 640,
    heightDp = 360,
    showBackground = true
)
@Composable
private fun SeasonsNumberRowPreview() {

    val list = listOf(
        SeasonsNumberDto(
            seasonNumber = "1"
        ),
        SeasonsNumberDto(
            seasonNumber = "1"
        ),
        SeasonsNumberDto(
            seasonNumber = "1"
        ),
        SeasonsNumberDto(
            seasonNumber = "1"
        ),
        SeasonsNumberDto(
            seasonNumber = "1"
        ),
    )

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        SeasonsNumberRow(
//            seasonsNumberDtoList = list,
//            onSeasonUpClick = {
//                false
//            },
//            onSeasonNumberChanged = {
//
//            },
//            lastSelectedItemIndex = 1,
//            onLastSelectedIndexChange = {
//
//            },
//        )
//    }


}