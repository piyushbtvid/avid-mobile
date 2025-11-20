package com.faithForward.media.ui.signup

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.R
import com.faithForward.media.ui.login.CustomTextField
import com.faithForward.media.ui.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.ui.theme.sideBarFocusedTextColor
import com.faithForward.media.util.Util
import com.faithForward.media.viewModel.SignupViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    signupViewModel: SignupViewModel,
    onSignup: () -> Unit,
) {
    val signupState by signupViewModel.signupState.collectAsStateWithLifecycle()
    val isSignedUp by signupViewModel.isSignedUp.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val buttonFocusRequester = remember { FocusRequester() }
    var isNameFocused by rememberSaveable { mutableStateOf(false) }
    var isEmailFocused by rememberSaveable { mutableStateOf(false) }
    var isPasswordFocused by rememberSaveable { mutableStateOf(false) }
    var isButtonFocused by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(signupState) {
        Log.e("GOING_TO_HOME", "signup state effect called with ${signupState.isSignedUp}")
        if (signupState.isSignedUp) {
            Log.e("GOING_TO_HOME", "Is Signed Up  ${signupState.isSignedUp}")
            signupViewModel.onEvent(SignupEvent.ResetIsSignedUp(false))
            onSignup.invoke()
        }
    }

    LaunchedEffect(Unit) {
        try {
            nameFocusRequester.requestFocus()
        } catch (ex: Exception) {
            Log.e("EX", "${ex.message}")
        }
    }

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
        Image(
            painter = painterResource(R.drawable.tvid_logo_ic),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .padding(top = 30.dp, start = 37.5.dp)
                .width(80.5.dp)
                .height(51.dp),
            contentDescription = "App Logo"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign up",
                color = sideBarFocusedBackgroundColor,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CustomTextField(value = signupState.name,
                onValueChange = {
                    signupViewModel.onEvent(SignupEvent.NameChanged(it))
                },
                keyboardController = keyboardController,
                placeholder = "Enter name",
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text,
                onNext = {
                    keyboardController?.hide()
                    scope.launch {
                        delay(200)
                        emailFocusRequester.requestFocus()
                    }
                },
                isTextFieldFocused = isNameFocused,
                modifier = Modifier
                    .width(389.5.dp)
                    .height(52.dp)
                    .focusRequester(nameFocusRequester)
                    .onFocusChanged {
                        isNameFocused = it.hasFocus
                    })

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(value = signupState.email,
                onValueChange = {
                    signupViewModel.onEvent(SignupEvent.EmailChanged(it))
                },
                keyboardController = keyboardController,
                placeholder = "Enter email",
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
                onNext = {
                    keyboardController?.hide()
                    scope.launch {
                        delay(200)
                        passwordFocusRequester.requestFocus()
                    }
                },
                isTextFieldFocused = isEmailFocused,
                modifier = Modifier
                    .width(389.5.dp)
                    .height(52.dp)
                    .focusRequester(emailFocusRequester)
                    .onFocusChanged {
                        isEmailFocused = it.hasFocus
                    })

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(value = signupState.password,
                keyboardController = keyboardController,
                onValueChange = {
                    signupViewModel.onEvent(SignupEvent.PasswordChanged(it))
                },
                placeholder = "Password",
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
                onNext = {
                    scope.launch {
                        delay(200)
                        Log.e("KEY_BOARD", "keyboard controler is $keyboardController")
                        buttonFocusRequester.requestFocus()
                    }
                },
                isTextFieldFocused = isPasswordFocused,
                modifier = Modifier
                    .width(389.5.dp)
                    .height(52.dp)
                    .focusRequester(passwordFocusRequester)
                    .onFocusChanged {
                        isPasswordFocused = it.hasFocus
                    })

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                scope.launch {
                    val deviceId = withContext(Dispatchers.IO) {
                        if (Util.isFireTv(context)) {
                            Util.getFireTvId(context).toString()
                        } else {
                            Util.getId(context)
                        }
                    }

                    val platform = if (Util.isFireTv(context)) {
                        "fire_tv"
                    } else {
                        "fire_tv"
                    }

                    Log.e("SIGNUP_SUBMIT", "on Sign up Click is called in SignupScreen")

                    signupViewModel.onEvent(
                        SignupEvent.SubmitSignup(
                            deviceType = platform, deviceId = deviceId
                        )
                    )
                }
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
                    else Color.White.copy(alpha = 0.33f)
                )) {
                Text(
                    text = "Sign up",
                    color = sideBarFocusedTextColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .height(20.dp),
                contentAlignment = Alignment.Center
            ) {
                if (signupState.errorMessage != null) {
                    Text(
                        text = signupState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@PreviewScreenSizes
@Preview(showSystemUi = true)
@Composable
fun SignupPreview(modifier: Modifier = Modifier) {
    // SignupScreen()
}

