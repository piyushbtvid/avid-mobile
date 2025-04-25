package com.faithForward.media


import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
                color = if (focusState == FocusState.FOCUSED) focusedBackGroundColor else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .width(114.dp)
            .height(38.dp)
    // optional: give fixed height to keep text truly centered
    ) {
        val (iconRef, textRef) = createRefs()

        // Icon (left of text)
        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .constrainAs(iconRef) {
                    end.linkTo(textRef.start, margin = 8.dp)
                    top.linkTo(textRef.top)
                    bottom.linkTo(textRef.bottom)
                },
            colorFilter = ColorFilter.tint(
                if (focusState == FocusState.FOCUSED) focusedColor else unFocusedColor
            )
        )

        // Text (centered in parent)
        androidx.compose.animation.AnimatedVisibility(
            visible = focusedSideBarItem != -1,
            enter = slideInHorizontally(
                animationSpec = tween(500, easing = LinearOutSlowInEasing),
                initialOffsetX = { -it / 3 }
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(100, easing = FastOutLinearInEasing),
                targetOffsetX = { -it / 3 }
            ),
            modifier = Modifier.constrainAs(textRef) {
                centerTo(parent)
            }
        ) {
            Text(
                text = txt,
                color = if (focusState == FocusState.FOCUSED) focusedColor else unFocusedColor,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        // Invisible placeholder
        if (focusedSideBarItem == -1) {
            Text(
                text = txt,
                color = Color.Transparent,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(createRef()) {
                    centerTo(parent)
                },
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