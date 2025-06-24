package com.faithForward.media.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.theme.pageBlackBackgroundColor

@Composable
fun ExitDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onExitConfirm: () -> Unit,
) {
    if (showDialog) {
        val noButtonFocusRequester = remember { FocusRequester() }

        // Automatically request focus on "No" when dialog is shown
        LaunchedEffect(Unit) {
            noButtonFocusRequester.requestFocus()
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text("Exit App", fontSize = 20.sp, color = Color.White)
            },
            text = {
                Text("Are you sure you want to exit?", color = Color.LightGray)
            },
            confirmButton = {
                FocusableButton(
                    text = "Yes", onClick = onExitConfirm
                )
            },
            dismissButton = {
                FocusableButton(
                    text = "No", onClick = onDismiss, focusRequester = noButtonFocusRequester
                )
            },
            backgroundColor = pageBlackBackgroundColor,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(24.dp)

        )
    }
}
