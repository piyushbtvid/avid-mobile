package com.faithForward.media.ui.sections.my_account

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionGrid
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.theme.whiteMain

data class WatchSectionUiModel(
    val title: String,
    val items: List<WatchSectionItemDto>?,
)


@Composable
fun WatchableGridSection(
    modifier: Modifier = Modifier,
    onItemClick: (WatchSectionItemDto) -> Unit,
    watchSectionUiModel: WatchSectionUiModel,
) {

    var focusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val focusRequesters = remember(watchSectionUiModel.items?.size ?: 0) {
        List(watchSectionUiModel.items?.size ?: 0) { FocusRequester() }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        TitleText(
            modifier = Modifier.padding(start = 10.dp),
            text = watchSectionUiModel.title,
            color = whiteMain,
            textSize = 18,
            lineHeight = 18,
            fontWeight = FontWeight.ExtraBold
        )

        WatchSectionGrid(
            focusRequesterList = focusRequesters,
            focusedIndex = focusedIndex, onFocusedIndexChange = { int ->
                focusedIndex = int
            },
            onItemClick = onItemClick,
            watchSectionItemDtoList = watchSectionUiModel.items ?: emptyList()
        )

    }

    LaunchedEffect(Unit) {
        try {
            Log.e("PROFILE", "grid first focus request")
            focusRequesters[0].requestFocus()
        } catch (_: Exception) {

        }
    }

}