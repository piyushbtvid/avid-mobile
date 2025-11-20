package com.faithForward.media.ui.universal_page.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.epg.Epg
import com.faithForward.media.ui.universal_page.UniversalPlayer
import com.faithForward.media.viewModel.UniversalViewModel

@Composable
fun LivePage(
    modifier: Modifier = Modifier,
    universalViewModel: UniversalViewModel,
) {
    val epgUiModel = universalViewModel.epgUiModel.collectAsState()

    val liveFirstUrl = universalViewModel.liveVideo.collectAsState()


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        if (liveFirstUrl.value.isNotEmpty()) {
            UniversalPlayer(
                videoUrlList = liveFirstUrl.value.filterNotNull()
            )
        }

        if (epgUiModel.value.data != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp) // optional: shift down to avoid top bar overlap
                    .align(Alignment.CenterStart)
            ) {
                Epg(
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(start = 30.dp, end = 15.dp),
                    epgUiModel = epgUiModel.value.data!!
                )
            }
        }

    }

}


@Preview
@Composable
private fun LivePagePreview() {
    // LivePage()
}