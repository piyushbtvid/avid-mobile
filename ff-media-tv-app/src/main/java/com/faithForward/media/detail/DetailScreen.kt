package com.faithForward.media.detail

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.media.viewModel.DetailPageItem
import com.faithForward.media.viewModel.DetailViewModel
import com.faithForward.util.Resource

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    itemId: String,
    relatedList: List<PosterCardDto> = emptyList(),
    detailViewModel: DetailViewModel,
) {
    // State to control the height of RelatedContentRow
    var targetHeight by remember { mutableIntStateOf(250) }
    val btnFocusRequester = remember { FocusRequester() }

    var contentColor by remember { mutableStateOf(Color.Black) }
    var buttonUnfocusedColor by remember { mutableStateOf(Color.White) }
    var textUnfocusedColor by remember { mutableStateOf(textUnFocusColor) }
    var contentRowTint by remember { mutableStateOf(Color.White) }

    var relatedContentColor by remember { mutableStateOf(Color.White) }

    // Animate the height change
    val animatedHeight by animateDpAsState(
        targetValue = targetHeight.dp,
        animationSpec = tween(durationMillis = 500) // 500ms animation duration
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    LaunchedEffect(Unit) {
        Log.e(
            "DETAIL_SCREEN",
            "detail screen is opened with $itemId and related List size  ${relatedList.size}"
        )
        detailViewModel.getGivenCardDetail(itemId, relatedList)
    }

    val cardDetailResponse by detailViewModel.cardDetail.collectAsStateWithLifecycle()

    if (cardDetailResponse is Resource.Loading || cardDetailResponse is Resource.Error || cardDetailResponse is Resource.Unspecified) return

    val detailPageItem = cardDetailResponse.data ?: return


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (detailPageItem) {
            is DetailPageItem.CardWithRelated -> {
                // Main content (fills the screen)
                DetailContent(
                    detailDto = detailPageItem.detailDto,
                    btnFocusRequester = btnFocusRequester,
                    contentColor = contentColor,
                    textUnfocusedColor = textUnfocusedColor,
                    contentRowTint = contentRowTint,
                    buttonUnfocusedColor = buttonUnfocusedColor,
                    modifier = Modifier.fillMaxSize(),
                )

                // Related row at the bottom with animated height
                RelatedContentRow(
                    relatedContentRowDto = RelatedContentRowDto(
                        heading = "Related Movies",
                        relatedContentDto = detailPageItem.relatedList,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(animatedHeight) // Use animated height
                        .align(Alignment.BottomStart)
                        .padding(bottom = 10.dp) // adjust padding as needed
                        .onFocusChanged {
                            if (it.hasFocus) {
                                targetHeight = screenHeight
                                buttonUnfocusedColor = Color.Transparent
                                textUnfocusedColor = Color.Transparent
                                contentColor = Color.Transparent
                                contentRowTint = Color.Transparent
                                relatedContentColor = Color.White
                                Log.e("RELATED", "related content Row has Focused")
                            }
                        },
                    onRelatedUpClick = {
                        targetHeight = 250
                        buttonUnfocusedColor = Color.White
                        textUnfocusedColor = textUnFocusColor
                        contentColor = Color.Black
                        contentRowTint = Color.White
                        relatedContentColor = Color.Transparent
                        try {
                            btnFocusRequester.requestFocus()
                        } catch (ex: Exception) {
                            Log.e("LOG", "exception is ${ex.message}")
                        }
                    },
                    relatedContentColor = relatedContentColor
                )
            }
        }
    }
}