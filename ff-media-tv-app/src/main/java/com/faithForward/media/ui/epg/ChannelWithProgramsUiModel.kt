package com.faithForward.media.ui.epg

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.faithForward.media.ui.epg.channel.ChannelItem
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramItem
import com.faithForward.media.ui.epg.program.ProgramUiModel

data class ChannelWithProgramsUiModel(
    val channelUiModel: ChannelUiModel,
    val programs: List<ProgramUiModel>,
)

@Composable
fun ChannelWithPrograms(
    modifier: Modifier = Modifier,
    channelWithProgramsUiModel: ChannelWithProgramsUiModel,
    horizontalScrollState: ScrollState = rememberScrollState(), // <- ScrollState instead of LazyListState
    isFirstRow: Boolean = false,
) {
    var focusedIndex by remember { mutableIntStateOf(-1) }
    val firstItemFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        if (isFirstRow) {
            try {
                firstItemFocusRequester.requestFocus()
            }catch (_: Exception){}
        }
    }

    Row(
        modifier = modifier.onFocusChanged {
            if (!it.hasFocus) {
                focusedIndex = -1
            }
        },
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ChannelItem(channelUiModel = channelWithProgramsUiModel.channelUiModel)

        Row(
            modifier = Modifier.horizontalScroll(horizontalScrollState),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            channelWithProgramsUiModel.programs.fastForEachIndexed { index, program ->
                val programModifier = if (isFirstRow && index == 0) {
                    Modifier
                        .focusRequester(firstItemFocusRequester)
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = index
                        }
                        .focusable()
                } else {
                    Modifier
                        .onFocusChanged {
                            if (it.isFocused) focusedIndex = index
                        }
                        .focusable()
                }

                ProgramItem(
                    programUiModel = program,
                    isFocused = focusedIndex == index,
                    modifier = programModifier,
                )
            }
            Spacer(Modifier.width(16.dp))

        }
    }
}

@Preview
@Composable
private fun ChannelWithProgramsPreview() {
    ChannelWithPrograms(
        channelWithProgramsUiModel = ChannelWithProgramsUiModel(
            channelUiModel = ChannelUiModel(
                channelImage = "",
                channelName = ""
            ),
            programs = List(20) {
                ProgramUiModel(
                    programName = "Drive Thru History Holiday Special",
                    programTimeString = "8:00AM - 9:00AM"
                )
            }
        ))
}