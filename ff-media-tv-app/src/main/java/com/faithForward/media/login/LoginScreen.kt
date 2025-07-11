//package com.faithForward.media.login
//
//import android.util.Log
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.tooling.preview.PreviewScreenSizes
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.faithForward.media.R
//import com.faithForward.media.theme.focusedMainColor
//import com.faithForward.media.theme.pageBlackBackgroundColor
//import com.faithForward.media.theme.sideBarFocusedBackgroundColor
//import com.faithForward.media.viewModel.LoginViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//@Composable
//fun LoginScreen(
//    modifier: Modifier = Modifier,
//    loginViewModel: LoginViewModel,
//    onLogin: () -> Unit,
//) {
//    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
//    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
//    val scope = rememberCoroutineScope()
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val context = LocalContext.current
//
//    val passwordFocusRequester = remember { FocusRequester() }
//    val buttonFocusRequester = remember { FocusRequester() }
//    val emailFocusRequester = remember { FocusRequester() }
//    var isEmailFocused by rememberSaveable { mutableStateOf(false) }
//    var isPasswordFocused by rememberSaveable { mutableStateOf(false) }
//    var isButtonFocused by rememberSaveable { mutableStateOf(false) }
//
//    LaunchedEffect(loginState) {
//        if (loginState.isLoggedIn) {
//            onLogin.invoke()
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        try {
//            emailFocusRequester.requestFocus()
//            keyboardController?.show()
//            Log.e("FOCUS", "Initial focus requested on email and keyboard shown")
//        } catch (ex: Exception) {
//            Log.e("FOCUS", "Failed to set initial focus: ${ex.message}")
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(pageBlackBackgroundColor)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.app_logo),
//            contentDescription = "App Logo",
//            modifier = Modifier
//                .align(Alignment.TopStart)
//                .padding(top = 30.dp, start = 37.5.dp)
//                .width(80.5.dp)
//                .height(51.dp)
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .padding(horizontal = 32.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Login",
//                color = Color.White,
//                fontSize = 32.sp,
//                modifier = Modifier.padding(bottom = 24.dp)
//            )
//
//            OutlinedTextField(
//                value = loginState.email,
//                onValueChange = { loginViewModel.onEvent(LoginEvent.EmailChanged(it)) },
//                label = { Text("Email", color = Color.White) },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                keyboardActions = KeyboardActions(onDone = {
//                    scope.launch {
//                        delay(200)
//                        passwordFocusRequester.requestFocus()
//                        keyboardController?.show()
//                        Log.e("FOCUS", "Focus moved to password and keyboard shown")
//                    }
//                    Log.e("FOCUS", "onDone in Email")
//                }),
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedContainerColor = Color.Gray.copy(alpha = 0.5f),
//                    unfocusedContainerColor = Color.Gray.copy(alpha = 0.5f),
//                    focusedIndicatorColor = focusedMainColor,
//                    unfocusedIndicatorColor = Color.Gray,
//                    cursorColor = Color.White
//                ),
//                shape = RoundedCornerShape(20.dp),
//                modifier = Modifier
//                    .width(400.dp)
//                    .padding(bottom = 16.dp)
//                    .focusRequester(emailFocusRequester)
//                    .onFocusChanged {
//                        isEmailFocused = it.isFocused
//                        if (it.isFocused) {
//                            keyboardController?.show()
//                            Log.e("FOCUS", "Keyboard shown for email (manual focus)")
//                        }
//                    }
//                    .focusable()
//            )
//
//            OutlinedTextField(
//                value = loginState.password,
//                onValueChange = { loginViewModel.onEvent(LoginEvent.PasswordChanged(it)) },
//                label = { Text("Password", color = Color.White) },
//                singleLine = true,
//                visualTransformation = PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                keyboardActions = KeyboardActions(onDone = {
//                    scope.launch {
//                        delay(200)
//                        buttonFocusRequester.requestFocus()
//                        keyboardController?.hide()
//                        Log.e("FOCUS", "Focus moved to button and keyboard hidden")
//                    }
//                    Log.e("FOCUS", "onDone in Password")
//                }),
//                colors = TextFieldDefaults.colors(
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    focusedContainerColor = Color.Gray.copy(alpha = 0.5f),
//                    unfocusedContainerColor = Color.Gray.copy(alpha = 0.5f),
//                    focusedIndicatorColor = focusedMainColor,
//                    unfocusedIndicatorColor = Color.Gray,
//                    cursorColor = Color.White
//                ),
//                shape = RoundedCornerShape(20.dp),
//                modifier = Modifier
//                    .width(400.dp)
//                    .padding(bottom = 24.dp)
//                    .focusRequester(passwordFocusRequester)
//                    .onFocusChanged {
//                        isPasswordFocused = it.isFocused
//                        if (it.isFocused) {
//                            keyboardController?.show()
//                            Log.e("FOCUS", "Keyboard shown for password (manual focus)")
//                        }
//                    }
//                    .focusable()
//            )
//
//            Button(
//                onClick = { loginViewModel.onEvent(LoginEvent.SubmitLogin) },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = if (isButtonFocused) sideBarFocusedBackgroundColor else Color.Gray,
//                    contentColor = focusedMainColor
//                ),
//                shape = RoundedCornerShape(24.dp),
//                modifier = Modifier
//                    .width(200.dp)
//                    .height(48.dp)
//                    .focusRequester(buttonFocusRequester)
//                    .onFocusChanged {
//                        isButtonFocused = it.isFocused
//                        if (it.isFocused) {
//                            keyboardController?.hide()
//                            Log.e("FOCUS", "Keyboard hidden for button")
//                        }
//                    }
//                    .focusable()
//            ) {
//                Text("Next", fontSize = 18.sp)
//            }
//
//            Box(
//                modifier = Modifier
//                    .padding(top = 10.dp)
//                    .fillMaxWidth()
//                    .height(20.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                if (loginState.errorMessage != null) {
//                    Text(
//                        text = loginState.errorMessage!!,
//                        color = MaterialTheme.colors.error,
//                        style = MaterialTheme.typography.body2,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@PreviewScreenSizes
//@Preview(showSystemUi = true)
//@Composable
//fun LoginPreview(modifier: Modifier = Modifier) {
//    // LoginScreen()
//}


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
import com.faithForward.media.theme.sideBarFocusedBackgroundColor
import com.faithForward.media.theme.sideBarFocusedTextColor
import com.faithForward.media.util.Util
import com.faithForward.media.viewModel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    onLogin: () -> Unit,
) {
    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    val passwordFocusRequester = remember { FocusRequester() }
    val buttonFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    var isEmailFocused by rememberSaveable { mutableStateOf(false) }
    var isPasswordFocused by rememberSaveable { mutableStateOf(false) }
    var isButtonFocused by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(loginState) {
        Log.e("GOING_TO_HOME", "login state effect called with ${loginState.isLoggedIn}")
        if (loginState.isLoggedIn) {
            Log.e("GOING_TO_HOME", "Is Loged in  ${loginState.isLoggedIn}")
            loginViewModel.onEvent(LoginEvent.ResetIsLogin(false))
            onLogin.invoke()
//            navController.navigate(Routes.Home.route) {
//                popUpTo(Routes.Login.route) { inclusive = true }
//            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            emailFocusRequester.requestFocus()
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
            painter = painterResource(R.drawable.app_logo),
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
                text = "Login",
                color = sideBarFocusedBackgroundColor,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CustomTextField(value = loginState.email,
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

            CustomTextField(value = loginState.password,
                keyboardController = keyboardController,
                onValueChange = {
                    loginViewModel.onEvent(LoginEvent.PasswordChanged(it))
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
                            Util.getId(context) // should also use withContext(IO) inside
                        }
                    }

                    val platform = if (Util.isFireTv(context)) {
                        "fire_tv"
                    } else {
                        "fire_tv" // or update this later
                    }

                    Log.e("GOING_TO_HOME", "on Next Click is called in LoginScreen")

                    loginViewModel.onEvent(
                        LoginEvent.SubmitLogin(
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
                    text = "Next",
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
                if (loginState.errorMessage != null) {
                    Text(
                        text = loginState.errorMessage!!,
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
fun LoginPreview(modifier: Modifier = Modifier) {
    // LoginScreen()
}