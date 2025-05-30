package com.faithForward.media.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.commanComponents.TitleText
import com.faithForward.media.theme.focusedMainColor

data class RelatedContentRowDto(
    val heading: String,
    val relatedContentDto: List<PosterCardDto>,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RelatedContent(
    modifier: Modifier = Modifier,
    onRelatedUpClick: () -> Unit,
    relatedContentColor: Color = Color.White,
    relatedContentRowDto: RelatedContentRowDto,
) {

    var relatedRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var currentFocusedItem by remember { mutableStateOf<PosterCardDto?>(null) }
    var isRelatedTextFocused by remember { mutableStateOf(false) }

    LaunchedEffect(relatedRowFocusedIndex) {
        if (relatedRowFocusedIndex > -1) {
            currentFocusedItem = relatedContentRowDto.relatedContentDto.get(relatedRowFocusedIndex)
            Log.e("CURRENT_FOCUS", "current focus item is ${currentFocusedItem?.title}")
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        // Related Items Text Heading
        TitleText(text = relatedContentRowDto.heading,
            color = if (isRelatedTextFocused) focusedMainColor else Color.Black,
            fontWeight = if (isRelatedTextFocused) FontWeight.W600 else FontWeight.Normal,
            modifier = Modifier
                .padding(start = 25.dp)
                .onFocusChanged {
                    isRelatedTextFocused = it.hasFocus
                }

        )

        // Related Items Row
        RelatedContentRow(
            relatedContentRowDto = relatedContentRowDto,
            modifier = Modifier
                .fillMaxWidth()
                .focusRestorer(),
            relatedRowFocusedIndex = relatedRowFocusedIndex,
            onRelatedUpClick = onRelatedUpClick,
            onRelatedRowFocusedIndexChange = { index ->
                relatedRowFocusedIndex = index
            },
        )

        //Related Item Content Meta Data
        RelatedContentInfoBlock(
            modifier = Modifier.padding(top = 20.dp),
            currentFocusedItem = currentFocusedItem,
            relatedContentColor = relatedContentColor,
        )
    }

}