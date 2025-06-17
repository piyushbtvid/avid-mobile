package com.faithForward.media.commanComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.extensions.shadow
import com.faithForward.media.theme.btnShadowColor
import com.faithForward.media.theme.focusedMainColor
import com.faithForward.media.theme.pillButtonTextColor
import com.faithForward.media.theme.pillButtonTextUnFocusColor
import com.faithForward.media.theme.pillButtonUnFocusColor
import com.faithForward.media.theme.textUnFocusColor
import com.faithForward.media.theme.unFocusMainColor
import com.faithForward.media.util.FocusState


data class CategoryComposeDto(
    val btnText: String,
    val id: String,
)

@Composable
fun CategoryCompose(
    modifier: Modifier = Modifier,
    backgroundFocusedColor: Color = focusedMainColor,
    backgroundUnFocusedColor: Color = pillButtonUnFocusColor,
    textFocusedColor: Color = pillButtonTextColor,
    textUnFocusedColor: Color = pillButtonTextUnFocusColor,
    categoryComposeDto: CategoryComposeDto,
    onCategoryItemClick: (String) -> Unit,
    focusState: FocusState,
) {
    val containerColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> backgroundFocusedColor
        FocusState.UNFOCUSED -> backgroundUnFocusedColor
        FocusState.UNDEFINED -> backgroundUnFocusedColor // Default to unfocused color for UNDEFINED
    }

    val textColor = when (focusState) {
        FocusState.SELECTED, FocusState.FOCUSED -> textFocusedColor
        FocusState.UNFOCUSED -> textUnFocusedColor
        FocusState.UNDEFINED -> textUnFocusedColor // Default to unfocused text color for UNDEFINED
    }

    val buttonModifier =
        if (focusState == FocusState.FOCUSED || focusState == FocusState.SELECTED) {
            modifier.shadow(
                color = backgroundFocusedColor.copy(alpha = .56f),
                borderRadius = 20.dp,
                blurRadius = 15.dp,
                offsetY = 4.dp,
                offsetX = 0.dp,
                spread = 4.dp
            )
        } else {
            modifier.shadow(
                color = btnShadowColor,
                borderRadius = 20.dp,
                blurRadius = 10.dp,
                offsetY = 3.dp,
                offsetX = 0.dp,
                spread = 1.dp
            )
        }

    Button(
        onClick = {
            onCategoryItemClick.invoke(categoryComposeDto.id)
        },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(50),
        modifier = buttonModifier.height(38.dp)
    ) {
        Text(
            categoryComposeDto.btnText,
            color = textColor,
            fontSize = 15.sp
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ButtonPreview() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CategoryCompose(
            focusState = FocusState.UNFOCUSED,
            categoryComposeDto = CategoryComposeDto("Poscasts", id = "vf"),
            onCategoryItemClick = {

            }
        )
    }
}


