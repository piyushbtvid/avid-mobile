package com.faithForward.media


import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.ui.theme.sideBarFocusedTextColor

@Composable
fun SideBarUiItem(
    modifier: Modifier = Modifier,
    txt: String,
    focusedBackGroundColor: Color = sideBarFocusedBackgroundColor,
    focusedColor: Color = sideBarFocusedTextColor,
    unFocusedColor: Color = focusedMainColor,
    focusState: FocusState,
    focusedSideBarItem: Int,
    img: Int
) {


    ConstraintLayout(
        modifier = modifier
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
        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
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
                if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) focusedColor else unFocusedColor
            )
        )

        androidx.compose.animation.AnimatedVisibility(visible = focusedSideBarItem != -1,
            enter = slideInHorizontally(animationSpec = tween(
                800, easing = LinearOutSlowInEasing
            ), initialOffsetX = { -it / 4 }),
            exit = slideOutHorizontally(animationSpec = tween(
                100, easing = FastOutLinearInEasing
            ), targetOffsetX = { -it / 4 }),
            modifier = Modifier.constrainAs(textRef) {
                start.linkTo(iconRef.end, margin = 8.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }) {
            Text(
                text = txt,
                color = if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) focusedColor else unFocusedColor,
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