package com.faithForward.media.ui.epg

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramUiModel

data class EpgUiModel(
    val channelWithProgramsUiModels: List<ChannelWithProgramsUiModel>,
    val currentTime: Long = System.currentTimeMillis(),
)

@Composable
fun Epg(
    modifier: Modifier = Modifier,
    epgUiModel: EpgUiModel
) {
    val sharedScrollState = rememberScrollState()

    Column(modifier = modifier.background(color = Color.LightGray)) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(7.5.dp)
        ) {
            itemsIndexed(epgUiModel.channelWithProgramsUiModels) { index, channelWithProgram ->
                ChannelWithPrograms(
                    channelWithProgramsUiModel = channelWithProgram,
                    isFirstRow = index== 0,
                    horizontalScrollState = sharedScrollState
                )
            }
        }
    }
}

@Preview(device = "id:tv_1080p")
@Composable
private fun EpgPreview() {
    Epg(
        epgUiModel = EpgUiModel(
            channelWithProgramsUiModels = List(10) {
                ChannelWithProgramsUiModel(
                    channelUiModel = ChannelUiModel(
                        channelImage = "",
                        channelName = ""
                    ),
                    programs = List(20) {
                        ProgramUiModel(
                            programName = "Drive Thru History Holiday Special",
                            programWidth = 23,
                            programTimeString = "8:00AM - 9:00AM"
                        )
                    }
                )
            }
        )
    )
}