package com.faithForward.media.sidebar


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.faithForward.media.R
import com.faithForward.media.theme.sideBarItemDefaultColor
import com.faithForward.media.theme.sideBarItemSelectedHighlightedColor
import com.faithForward.media.theme.whiteMain
import com.faithForward.media.util.FocusState

@Composable
fun SideBarUiItem(
    modifier: Modifier = Modifier,
    txt: String,
    focusedBackGroundColor: Color = sideBarItemSelectedHighlightedColor,
    focusState: FocusState,
    focusedSideBarItem: Int,
    img: Int?,
) {
    ConstraintLayout(
        modifier = Modifier
            .background(
                color = if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) focusedBackGroundColor else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .width(
                when {
                    focusedSideBarItem == -1 -> 38.dp
                    else -> 114.dp
                }
            )
            .height(38.dp)
    ) {
        val (iconRef, textRef) = createRefs()

        // Icon (left of text)
        if (img != null) {
            Image(
                painter = painterResource(id = img),
                contentDescription = null,
                modifier = modifier
                    .size(if (txt == "My Account") 1.dp else if (txt == "Log Out") 8.dp else 18.dp)
                    .constrainAs(iconRef) {
                        if (focusedSideBarItem == -1) {
                            centerTo(parent)
                        } else {
                            start.linkTo(parent.start, margin = 8.dp)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                    },
                colorFilter = ColorFilter.tint(
                    if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) whiteMain else sideBarItemDefaultColor
                )
            )
        }

        if (focusedSideBarItem != -1) {
            Text(
                modifier = Modifier.constrainAs(textRef) {
                    if (img == null) {
                        centerTo(parent)
                    } else {
                        start.linkTo(iconRef.end, margin = 8.dp)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                },
                text = txt,
                color = if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) whiteMain else sideBarItemDefaultColor,
                maxLines = 1,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview
@Composable
fun SideBarItemPreview() {
    SideBarUiItem(
        txt = "Home",
        img = R.drawable.home_ic,
        focusState = FocusState.FOCUSED,
        focusedSideBarItem = 1
    )
}