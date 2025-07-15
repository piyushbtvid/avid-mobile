package com.faithForward.media.login.qr

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.theme.pageBlackBackgroundColor
import com.faithForward.media.util.Util
import com.faithForward.media.viewModel.QrLoginViewModel
import com.faithForward.media.viewModel.uiModels.QrLoginEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


data class LoginQrScreenDto(
    val qrImage: String?,
    val expiredTime: String?,
    val code: String?,
    val url: String?,
)

@Composable
fun LoginQrScreen(
    modifier: Modifier = Modifier,
    loginQrLoginViewModel: QrLoginViewModel,
    onLoggedIn: () -> Unit, // <- callback
    onLoginPageOpenClick: () -> Unit,
) {
    val state by loginQrLoginViewModel.state.collectAsState()
    val context = LocalContext.current

    // Trigger callback when user logs in
    LaunchedEffect(state.isLoggedIn) {
        Log.e("IS_lOGIN_QR", "is login is called with ${state.isLoggedIn}")
        if (state.isLoggedIn) {
            Log.e("IS_lOGIN_QR", "is login is called")
            onLoggedIn()
        }
    }

    LaunchedEffect(Unit) {
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
            // Change to "android" if needed when API supports it
            "fire_tv"
        }

        Log.e("CHECK_LOGIN", "onStart Login called in Unit of LoginQr")
        loginQrLoginViewModel.onEvent(
            QrLoginEvent.StartLogin(
                deviceType = platform,
                deviceId = deviceId
            )
        )
    }


    state.qrScreenDto?.let { dto ->
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(pageBlackBackgroundColor)
                .padding(start = 30.dp, end = 100.dp, top = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Image(
                    painter = painterResource(R.drawable.logo),
                    modifier = Modifier
                        .width(80.5.dp)
                        .height(51.dp),
                    contentDescription = "App Logo"
                )

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(60.dp)
                ) {

                    dto.url?.let { url ->
                        LoginSteps(url = url)
                    }

                    if (dto.url != null && dto.code != null && state.timeLeftSeconds > 0) {
                        ActivationCode(url = dto.url,
                            code = dto.code,
                            expireTime = formatTimeLeft(state.timeLeftSeconds),
                            onLoginPageOpenClick = {
                                loginQrLoginViewModel.onEvent(QrLoginEvent.StopPolling)
                                onLoginPageOpenClick.invoke()
                            })
                    }

                }
            }

            dto.qrImage?.let { qrUrl ->
                QrImage(imageUrl = qrUrl)
            }
        }
    }
}


@Preview(
    name = "Login QR Screen - 1920x1080", widthDp = 1920, heightDp = 1080, showBackground = true
)
@Composable
private fun LoginQrScreenPreview() {

//    LoginQrScreen(
//        loginQrScreenDto = LoginQrScreenDto(
//            qrImage = "",
//            expiredTime = "15:30",
//            code = "8A2QR",
//            url = "http://107.180.208.127:3000/activate",
//        )
//    )

}

fun formatTimeLeft(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
