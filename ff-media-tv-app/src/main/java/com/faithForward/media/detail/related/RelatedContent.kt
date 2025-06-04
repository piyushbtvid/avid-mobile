package com.faithForward.media.detail.related

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
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


@Composable
fun RelatedContent(
    modifier: Modifier = Modifier,
    onRelatedUpClick: () -> Boolean,
    contentRowModifier: Modifier = Modifier,
    relatedContentRowDto: RelatedContentRowDto,
    onItemClick: (PosterCardDto, List<PosterCardDto>) -> Unit,
    lastFocusedItemIndex: Int,
    onLastFocusedIndexChange: (Int) -> Unit,
    isRelatedContentMetaDataVisible: Boolean = false,
    seasonsNumberRow: (@Composable () -> Unit)? = null,
) {
    var relatedRowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }
    var currentFocusedItem by remember { mutableStateOf<PosterCardDto?>(null) }
    var isRelatedTextFocused by remember { mutableStateOf(false) }

    val focusRequesters = remember { mutableMapOf<Int, FocusRequester>() }

    val listState = rememberLazyListState()

    val targetAlpha by animateFloatAsState(
        targetValue = if (isRelatedContentMetaDataVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    LaunchedEffect(relatedRowFocusedIndex) {
        if (relatedRowFocusedIndex > -1) {
            currentFocusedItem =
                relatedContentRowDto.relatedContentDto.getOrNull(relatedRowFocusedIndex)
            Log.e("CURRENT_FOCUS", "current focus item is ${currentFocusedItem?.title}")
        }
    }

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TitleText(text = relatedContentRowDto.heading,
                color = if (isRelatedTextFocused) focusedMainColor else Color.Black,
                fontWeight = if (isRelatedTextFocused) FontWeight.W600 else FontWeight.Normal,
                modifier = Modifier.onFocusChanged {
                    isRelatedTextFocused = it.hasFocus
                })

            seasonsNumberRow?.invoke()
        }

        RelatedContentRow(relatedContentRowDto = relatedContentRowDto.relatedContentDto,
            modifier = contentRowModifier,
            relatedRowFocusedIndex = relatedRowFocusedIndex,
            onRelatedUpClick = onRelatedUpClick,
            onItemClick = { item ->
                onItemClick.invoke(item, relatedContentRowDto.relatedContentDto)
            },
            onRelatedRowFocusedIndexChange = { index ->
                relatedRowFocusedIndex = index
            },
            focusRequesters = focusRequesters,
            lastFocusedItem = lastFocusedItemIndex,
            lazyListState = listState,
            onItemFocused = { newFocus ->
                onLastFocusedIndexChange.invoke(newFocus)
            })

        RelatedContentInfoBlock(
            modifier = Modifier
                .padding(top = 20.dp)
                .alpha(targetAlpha),
            currentFocusedItem = currentFocusedItem,
        )
    }

    LaunchedEffect(Unit) {
        Log.e("LAST_FOCUSED_INDEX", "last focused index is $lastFocusedItemIndex")
        try {
            if (lastFocusedItemIndex > 0 && lastFocusedItemIndex < relatedContentRowDto.relatedContentDto.size) {
                listState.scrollToItem(lastFocusedItemIndex)
                focusRequesters[lastFocusedItemIndex]?.requestFocus()
            }
        } catch (_: Exception) {

        }

    }

}