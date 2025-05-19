package com.faithForward.media.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.R
import com.faithForward.media.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.theme.sideBarFocusedTextColor

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNextClick: () -> Unit = {} // Callback for the Next button
) {
    var email by remember { mutableStateOf("Bunker@gmail.com") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF141414),
                        Color(0xFF2A2A2A),
                        Color(0xFF141414)
                    )
                )
            )
    ) {
        // Logo at the top-left
        Image(
            painter = painterResource(R.drawable.app_logo),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp), // Adjust size as needed
            contentDescription = "App Logo"
        )

        // Centered content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "Login" title
            Text(
                text = "Login",
                color = sideBarFocusedBackgroundColor, // Orange text
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Email TextField
            CustomEmailTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Enter email",
                modifier = Modifier
                    .width(389.5.dp)
                    .height(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            CustomEmailTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                modifier = Modifier
                    .width(389.5.dp)
                    .height(48.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Next Button
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .width(178.5.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = sideBarFocusedBackgroundColor
                )
            ) {
                Text(
                    text = "Next",
                    color = sideBarFocusedTextColor,
                    fontSize = 20.sp,
                )
            }
        }
    }
}


@PreviewScreenSizes
@Preview(showSystemUi = true)
@Composable
fun LoginPreview(modifier: Modifier = Modifier) {
    LoginScreen()
}