package com.faithForward.media.ui.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.theme.placeHolderTextColor
import com.faithForward.media.ui.theme.sideBarFocusedBackgroundColor

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
    enabled: Boolean = true,
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
        visualTransformation = if (keyboardType == KeyboardType.Password)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Login",
    placeholder: String = "Enter your Login",
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}


@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password",
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Check,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                Icons.Default.Person,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
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