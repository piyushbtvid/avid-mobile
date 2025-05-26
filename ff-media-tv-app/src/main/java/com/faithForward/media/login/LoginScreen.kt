package com.faithForward.media.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.R
import com.faithForward.media.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.theme.sideBarFocusedTextColor
import com.faithForward.media.viewModel.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier, loginViewModel: LoginViewModel, onNextClick: () -> Unit = {}
) {
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    val passwordFocusRequester = remember { FocusRequester() }
    val buttonFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isButtonFocused by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF141414),
                        Color(0xFF474747),
                        Color(0xFF7A7A7A),
                    )
                )
            )
    ) {
        // Logo at the top-left
        Image(
            painter = painterResource(R.drawable.app_logo),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .padding(top = 30.dp, start = 37.5.dp)
                .width(80.5.dp)
                .height(51.dp),
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
                color = sideBarFocusedBackgroundColor,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Email TextField
            CustomTextField(
                value = loginState.email,
                onValueChange = {
                    loginViewModel.onEvent(LoginEvent.EmailChanged(it))
                },
                keyboardController = keyboardController,
                placeholder = "Enter email",
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                onNext = {
                    keyboardController?.hide()
                    scope.launch {
                        delay(200)
                        passwordFocusRequester.requestFocus() // Move focus to password
                    }
                },
                isTextFieldFocused = isEmailFocused,
                modifier = Modifier
                    .width(389.5.dp)
                    .height(48.dp)
                    .focusRequester(emailFocusRequester)
                    .onFocusChanged {
                        isEmailFocused = it.hasFocus
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            CustomTextField(
                value = loginState.password,
                keyboardController = keyboardController,
                onValueChange = {
                    loginViewModel.onEvent(LoginEvent.PasswordChanged(it))
                },
                placeholder = "Password",
                imeAction = ImeAction.Done, // Use Done instead of Next for the last field
                keyboardType = KeyboardType.Password,
                onNext = {
                    scope.launch {
                        delay(200)
                        Log.e("NEXT", "on next click is called in password text field")
                        buttonFocusRequester.requestFocus() // Move focus to button
                    }
                },
                isTextFieldFocused = isPasswordFocused,
                modifier = Modifier
                    .width(389.5.dp)
                    .height(48.dp)
                    .focusRequester(passwordFocusRequester)
                    .onFocusChanged {
                        isPasswordFocused = it.hasFocus
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Next Button
            Button(onClick = {
                loginViewModel.onEvent(LoginEvent.SubmitLogin)
                onNextClick.invoke()
            },
                modifier = Modifier
                    .width(178.5.dp)
                    .height(48.dp)
                    .focusRequester(buttonFocusRequester)
                    .onFocusChanged {
                        isButtonFocused = it.hasFocus
                    }
                    .focusable(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isButtonFocused) sideBarFocusedBackgroundColor
                    else {
                        Color.White.copy(alpha = .33f)
                    }
                )) {
                Text(
                    text = "Next", color = sideBarFocusedTextColor, fontSize = 20.sp
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(20.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (loginState.errorMessage != null) {
                    Text(
                        text = loginState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            emailFocusRequester.requestFocus()
        } catch (ex: Exception) {
            Log.e("EX", "${ex.message}")
        }
    }
}


@PreviewScreenSizes
@Preview(showSystemUi = true)
@Composable
fun LoginPreview(modifier: Modifier = Modifier) {
    // LoginScreen()
}