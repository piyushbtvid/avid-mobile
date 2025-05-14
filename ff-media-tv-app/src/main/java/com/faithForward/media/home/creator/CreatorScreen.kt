package com.faithForward.media.home.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faithForward.media.theme.unFocusMainColor

@Composable
fun CreatorScreen(
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = unFocusMainColor),
        contentAlignment = Alignment.Center
    ) {

        Text(text = "Creator Screen")

    }

}