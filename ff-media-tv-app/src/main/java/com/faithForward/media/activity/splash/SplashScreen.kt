package com.faithForward.media.activity.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.faithForward.media.R

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.splash_background_img),
            contentDescription = "splash screen"
        )

    }

}


@Preview(device = "id:tv_1080p")
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}