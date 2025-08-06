package com.faithForward.media.ui.user_profile.create_profile

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.login.CustomTextField
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.unFocusMainColor
import com.faithForward.media.ui.theme.whiteMain

@Composable
fun EditProfile(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    errorText: String? = null,
    isEditProfile: Boolean = false,
    titleText: String = "Profile Name",
    deleteFocusRequester: FocusRequester = FocusRequester(),
    onSaveClick: (isKids: Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
) {

    var isChecked by remember { mutableStateOf(false) }
    var isSaveFocus by remember { mutableStateOf(false) }
    var isCancelFocus by remember { mutableStateOf(false) }

    val saveFocusRequester = remember { FocusRequester() }
    val cancelFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        // Title
        Text(
            text = titleText,
            fontSize = 24.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Column {
            CustomTextField(value = textFieldValue,
                onValueChange = { value ->

                },
                placeholder = "Enter profile name",
                keyboardType = KeyboardType.Text,
                onNext = {

                },
                isTextFieldFocused = false,
                modifier = Modifier
                    .width(310.dp)
                    .height(52.dp)
                    .focusable(enabled = false)
                    .focusProperties {
                        canFocus = false
                    }
            )

            if (!errorText.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.width(310.dp),
                    text = errorText,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W400,
                    color = Color.Red
                )
            }
        }
//        if (isEditProfile) {
//            RemoveProfileColumn(
//                onDeleteClick = onDeleteClick,
//                deleteFocusRequester = deleteFocusRequester
//            )
//        } else {
//            IsKids(
//                isChecked = isChecked,
//                onIsCheckChange = { boolean ->
//                    isChecked = boolean
//                }
//            )
//        }


        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomButton(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                buttonText = "SAVE",
                isFocused = isSaveFocus,
                buttonFocusRequester = saveFocusRequester,
                onButtonClick = {
                    onSaveClick.invoke(isChecked)
                },
                onButtonFocusChange = { boolean ->
                    isSaveFocus = boolean
                }
            )

            CustomButton(
                modifier = Modifier
                    .width(110.dp)
                    .height(50.dp),
                buttonText = "CANCEL",
                isFocused = isCancelFocus,
                buttonFocusRequester = cancelFocusRequester,
                onButtonClick = {
                    onCancelClick.invoke()
                },
                onButtonFocusChange = { boolean ->
                    isCancelFocus = boolean
                }
            )

        }

    }

}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    isFocused: Boolean = false,
    buttonFocusRequester: FocusRequester,
    onButtonClick: () -> Unit,
    onButtonFocusChange: (Boolean) -> Unit,
) {

    Button(
        onClick = { onButtonClick.invoke() },
        modifier = modifier
            .border(
                width = 0.7.dp,
                color = if (!isFocused) whiteMain.copy(alpha = 0.35f) else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .focusRequester(buttonFocusRequester)
            .onFocusChanged {
                onButtonFocusChange.invoke(it.hasFocus)
            }
            .focusable(),
        shape = RoundedCornerShape(4.dp),
        colors = buttonColors(
            containerColor = if (isFocused) focusedMainColor
            else unFocusMainColor
        )
    ) {
        Text(
            text = buttonText,
            color = if (isFocused) Color.White else whiteMain.copy(alpha = 0.60f),
            fontSize = 14.sp,
            lineHeight = 14.sp,
            fontWeight = FontWeight.W800,
        )
    }
}