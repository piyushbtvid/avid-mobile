package com.faithForward.media.ui.user_profile

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.faithForward.media.R
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.util.FocusState


data class UserProfileUiItem(
    val id: Int,
    val currentAvatarImg: String,
    val avatarId: Int,
    val name: String,
    val isDefault: Boolean,
)


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AllProfileScreenRow(
    modifier: Modifier = Modifier,
    userProfileList: List<UserProfileUiItem>,
    onItemClick: (UserProfileUiItem) -> Unit,
    shouldShowAddProfileButton: Boolean = false,
    onAddProfileClick: () -> Unit,
) {

    var rowFocusedIndex by rememberSaveable { mutableStateOf(-1) }

    val focusRequester = remember { FocusRequester() }

    var isAddProfileFocused by remember { mutableStateOf(false) }

    LazyRow(
        modifier = modifier.focusRestorer {
            focusRequester
        },
        contentPadding = PaddingValues(start = 56.dp, end = 40.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(userProfileList) { index, item ->

            val uiState = when (index) {
                rowFocusedIndex -> FocusState.FOCUSED
                else -> FocusState.UNFOCUSED
            }

            val itemModifier = Modifier
                .then(if (index == 0) Modifier.focusRequester(focusRequester) else Modifier)
                .onFocusChanged {
                    rowFocusedIndex = if (it.hasFocus) index else -1
                }
                .clickable(
                    interactionSource = null, indication = null
                ) {
                    onItemClick.invoke(item)
                }
                .focusable()

            UserProfileItem(
                modifier = itemModifier,
                imgSrc = item.currentAvatarImg,
                uiState = uiState,
                title = item.name
            )
        }

        if (shouldShowAddProfileButton) {
            item {

                val uiState = when (isAddProfileFocused) {
                    true -> FocusState.FOCUSED
                    else -> FocusState.UNFOCUSED
                }

                AddProfileItem(uiState = uiState, modifier = Modifier
                    .onFocusChanged {
                        isAddProfileFocused = it.hasFocus
                    }
                    .clickable(
                        interactionSource = null, indication = null
                    ) {
                        onAddProfileClick.invoke()
                    }
                    .focusable())
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            Log.e("FOCUS", "request focus")
            focusRequester.requestFocus()
        } catch (ex: Exception) {
            Log.e("FOCUS", "Failed to request focus with ${ex.printStackTrace()}")
        }
    }

}

@Composable
fun UserProfileItem(
    modifier: Modifier = Modifier,
    imgSrc: String,
    title: String,
    uiState: FocusState,
) {

    val borderColor = when (uiState) {
        FocusState.SELECTED -> Color.White
        FocusState.FOCUSED -> focusedMainColor
        FocusState.UNFOCUSED, FocusState.UNDEFINED -> Color.Transparent
    }

    val titleColor = when (uiState) {
        FocusState.SELECTED, FocusState.FOCUSED -> Color.White
        FocusState.UNFOCUSED, FocusState.UNDEFINED -> Color.White.copy(alpha = 0.70f)
    }

    val scaleFactor by animateFloatAsState(
        targetValue = if (uiState == FocusState.FOCUSED) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scaling avatar"
    )

    Column(
        modifier = Modifier.scale(scaleFactor),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .size(85.dp)
                .clip(shape = RoundedCornerShape(150.dp))
                .border(
                    border = BorderStroke(
                        width = 2.dp, color = borderColor
                    ), shape = RoundedCornerShape(150.dp)
                )
                .padding(3.dp), contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                modifier = Modifier
                    .size(83.dp)
                    .clip(shape = RoundedCornerShape(150.dp)),
                model = ImageRequest.Builder(LocalContext.current).data(imgSrc).crossfade(true)
                    .build(),
                contentDescription = "user avatar",
                contentScale = ContentScale.Crop,
            )

        }


        TitleText(
            text = title, textSize = 13, lineHeight = 13, color = titleColor
        )

    }


}

@Composable
fun AddProfileItem(
    modifier: Modifier = Modifier,
    uiState: FocusState,
) {

    val borderColor = when (uiState) {
        FocusState.SELECTED -> Color.White
        FocusState.FOCUSED -> focusedMainColor
        FocusState.UNFOCUSED, FocusState.UNDEFINED -> Color.White.copy(alpha = 0.70f)
    }

    val scaleFactor by animateFloatAsState(
        targetValue = if (uiState == FocusState.FOCUSED) 1.1f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "scaling add  Profile"
    )


    Column(
        modifier = Modifier.scale(scaleFactor),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .size(85.dp)
                .clip(shape = RoundedCornerShape(150.dp))
                .border(
                    border = BorderStroke(
                        width = 2.dp, color = borderColor
                    ), shape = RoundedCornerShape(150.dp)
                )
                .padding(3.dp), contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.add_new), colorFilter = ColorFilter.tint(
                    color = borderColor
                ), contentDescription = null
            )


        }


        TitleText(
            text = "Add Profile", textSize = 13, lineHeight = 13, color = borderColor
        )


    }


}


@Preview
@Composable
private fun AddProfileItemPreview() {

    AddProfileItem(
        uiState = FocusState.UNFOCUSED
    )

}