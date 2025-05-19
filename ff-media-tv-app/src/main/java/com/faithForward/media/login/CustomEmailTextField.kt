package com.faithForward.media.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.theme.placeHolderTextColor
import com.faithForward.media.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.theme.sideBarFocusedTextColor

@Composable
fun CustomEmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = .33f), // Gray background
                shape = RoundedCornerShape(24.dp) // Rounded corners
            )
            .border(
                width = 1.dp,
                color = sideBarFocusedBackgroundColor,
                shape = RoundedCornerShape(24.dp)
            ),
        enabled = enabled,
        textStyle = TextStyle(
            color = sideBarFocusedBackgroundColor, // Orange text color
            fontSize = 18.sp
        ),
        placeholder = {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    color = placeHolderTextColor, // Orange placeholder with lower opacity
                    fontSize = 10.sp
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent, // Remove underline
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp) // Ensure the shape is applied
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmailTextField() {

    CustomEmailTextField(
        value = "hbd,kbsvkbvs",
        onValueChange = {

        }
    )

}