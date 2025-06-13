package com.faithForward.media.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.theme.placeHolderTextColor
import com.faithForward.media.theme.sideBarFocusedBackgroundColor

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardController: SoftwareKeyboardController? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onNext: () -> Unit = {}, // Change onSearch to onNext for clarity
    imeAction: ImeAction = ImeAction.Next,
    placeholder: String? = null,
    isTextFieldFocused: Boolean = false,
    enabled: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(
                color = Color.White.copy(alpha = .33f),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = if (isTextFieldFocused) sideBarFocusedBackgroundColor else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            ),
        enabled = enabled,
        textStyle = TextStyle(
            color = sideBarFocusedBackgroundColor,
            fontSize = 20.sp,
            lineHeight = 20.sp
        ),
        placeholder = {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    color = placeHolderTextColor,
                    fontSize = 10.sp
                )
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            autoCorrectEnabled = false,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                Log.e("NEXT", "on next click is called in text field")
                onNext()
            },
            onDone = {
                Log.e("NEXT", "on done click is called in text field")
                keyboardController?.hide()
                onNext()
            },
        ),
        colors = TextFieldDefaults.colors(
            cursorColor = sideBarFocusedBackgroundColor,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmailTextField() {

    CustomTextField(
        value = "hbd,kbsvkbvs",
        onValueChange = {

        },
        onNext = {

        }
    )

}