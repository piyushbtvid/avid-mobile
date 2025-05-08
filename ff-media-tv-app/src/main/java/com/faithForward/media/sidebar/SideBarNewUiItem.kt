package com.faithForward.media.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faithForward.media.util.FocusState
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.ui.theme.sideBarFocusedTextColor

@Composable
fun SideBarNewUiItem(
    modifier: Modifier = Modifier,
    txt: String,
    focusedBackGroundColor: Color = sideBarFocusedBackgroundColor,
    focusedColor: Color = sideBarFocusedTextColor,
    unFocusedColor: Color = focusedMainColor,
    focusState: FocusState,
    focusedSideBarItem: Int,
    img: Int
) {

    Box(
        modifier = modifier
            .background(
                color = if (focusState == FocusState.FOCUSED) focusedBackGroundColor else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .width(
                if (focusedSideBarItem == -1) {
                    56.dp
                } else 114.dp
            )
            .height(38.dp)
    ) {

        Row(
            modifier = Modifier
        ){

        }

    }

}