package com.faithForward.media.ui.universal_page.live.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.epg.Epg
import com.faithForward.media.viewModel.UniversalViewModel

@Composable
fun GuidePage(
    modifier: Modifier = Modifier,
    universalViewModel: UniversalViewModel,
) {

    val categoryList = universalViewModel.categoryButtonList.collectAsState()
    val epgData = universalViewModel.guideEpgUiModel.collectAsState()
    val liveFirstUrl = universalViewModel.liveVideo

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 71.dp, start = 11.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 23.dp, end = 34.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            GuideTitleWithDescription(
                title = "Better Together",
                description = "Join us for better together Ever Day is New Day and a New beging join us join us join us"
            )

            if (liveFirstUrl.value.isNotEmpty()) {
                GuideSmallPlayer(urlList = liveFirstUrl.value)
            }

        }


        if (categoryList.value.data != null) {
            GuideCategoryRow(
                list = categoryList.value.data!!
            )
        }

        if (epgData.value.data != null) {
            Epg(
                modifier = Modifier
                    .background(color = Color.Transparent),
                epgUiModel = epgData.value.data!!
            )
        }


    }


}