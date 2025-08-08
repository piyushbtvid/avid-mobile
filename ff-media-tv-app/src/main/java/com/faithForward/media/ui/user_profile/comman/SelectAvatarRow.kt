package com.faithForward.media.ui.user_profile.comman

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import com.faithForward.media.R
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.util.FocusState

data class AvatarItem(
    val id: Int,
    val imgSrc: String,
)

@Composable
fun SelectAvatarRow(
    modifier: Modifier = Modifier,
    onSelectProfileClick: (Int) -> Unit,
    defaultSelectedIndex: Int = -1,
    avatarList: List<AvatarItem>,
) {

    var rowFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    var selectedIndex by rememberSaveable { mutableStateOf(defaultSelectedIndex) }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 56.dp, end = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        itemsIndexed(avatarList) { index, item ->


            val uiState = when (index) {
                selectedIndex -> FocusState.SELECTED
                rowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }


            val itemModifier = Modifier
                .onFocusChanged {
                    rowFocusedIndex = if (it.hasFocus) index else -1
                }
                .clickable(
                    interactionSource = null, indication = null
                ) {
                    selectedIndex = index
                    onSelectProfileClick.invoke(item.id)
                }
                .focusable()

            AvatarUiItem(
                modifier = itemModifier,
                imgSrc = item.imgSrc,
                uiState = uiState
            )

        }

    }

}


@Composable
fun AvatarUiItem(
    modifier: Modifier = Modifier,
    imgSrc: String,
    uiState: FocusState,
) {

    val borderColor = when (uiState) {
        FocusState.FOCUSED -> focusedMainColor
        FocusState.SELECTED -> Color.White
        FocusState.UNFOCUSED, FocusState.UNDEFINED -> Color.Transparent
    }

    val scaleFactor by animateFloatAsState(
        targetValue = if (uiState == FocusState.FOCUSED || uiState == FocusState.SELECTED) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scaling avatar"
    )


    Box(
        modifier = modifier
            .size(85.dp)
            .clip(shape = RoundedCornerShape(150.dp))
            .border(
                border = BorderStroke(
                    width = 2.dp, color = borderColor
                ), shape = RoundedCornerShape(150.dp)
            )
            .scale(scaleFactor)
            .padding(3.dp), contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            modifier = Modifier
                .size(83.dp)
                .clip(shape = RoundedCornerShape(150.dp)),
            model = ImageRequest.Builder(LocalContext.current).data(imgSrc)
                .error(R.drawable.test_avatar).crossfade(true).build(),
            contentDescription = "user avatar",
            contentScale = ContentScale.Crop,
        )

    }


}