package com.faithForward.media.ui.sections.my_account.continue_watching

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionGrid
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun ContinueWatching(
    modifier: Modifier = Modifier,
    watchSectionItemDtoList: List<WatchSectionItemDto>,
) {

    var focusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        TitleText(
            modifier = Modifier.padding(start = 10.dp),
            text = "Continue Watching",
            color = whiteMain,
            textSize = 18,
            lineHeight = 18,
            fontWeight = FontWeight.ExtraBold
        )

        WatchSectionGrid(
            focusedIndex = focusedIndex, onFocusedIndexChange = { int ->
                focusedIndex = int
            }, watchSectionItemDtoList = watchSectionItemDtoList
        )

    }


}