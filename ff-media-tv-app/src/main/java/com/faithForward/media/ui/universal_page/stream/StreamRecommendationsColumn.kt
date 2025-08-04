package com.faithForward.media.ui.universal_page.stream

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState

data class StreamRecommendationsUiItem(
    val image: Int = R.drawable.test_poster,
)

@Composable
fun StreamRecommendationsColumn(
    modifier: Modifier = Modifier,
    list: List<StreamRecommendationsUiItem>,
) {

    var lastFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    Column(
        modifier = modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.spacedBy(13.5.dp)
    ) {

        TitleText(
            text = "Recommendations",
            color = whiteMain,
            textSize = 15,
            lineHeight = 15,
            fontWeight = FontWeight.W600
        )


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
        ) {

            itemsIndexed(list) { index, item ->

                val uiState = when (index) {
                    lastFocusedIndex -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }


                StreamRecommendationsItem(
                    modifier = Modifier
                        .onFocusChanged {
                            if (it.hasFocus) {
                                lastFocusedIndex = index
                            }
                        }
                        .focusable(),
                    uiItem = item,
                    focusState = uiState
                )

            }

        }

    }


}


@Composable
fun StreamRecommendationsItem(
    modifier: Modifier = Modifier,
    uiItem: StreamRecommendationsUiItem,
    focusState: FocusState,
) {

    val scale by animateFloatAsState(
        targetValue = when (focusState) {
            FocusState.SELECTED, FocusState.FOCUSED -> 1.13f
            else -> 1f
        }, animationSpec = tween(300), label = ""
    )

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .scale(scale)
            .zIndex(
                when (focusState) {
                    FocusState.SELECTED, FocusState.FOCUSED -> 1f
                    else -> 0f
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {

        Column(
            modifier = modifier
                .width(135.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Image(
                painter = painterResource(uiItem.image),
                contentDescription = "Poster Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable(interactionSource = null, indication = null, onClick = {
                        // onItemClick.invoke()
                    }
                    )
            )
        }

    }

}