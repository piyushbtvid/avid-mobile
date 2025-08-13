package com.faithForward.media.ui.epg

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.faithForward.media.ui.epg.channel.ChannelUiModel
import com.faithForward.media.ui.epg.program.ProgramUiModel
import com.faithForward.media.ui.epg.timeline.TimeLine
import com.faithForward.media.ui.epg.util.currentTimeOffsetDp
import com.faithForward.media.ui.epg.util.generateSampleEpgUiModel
import com.faithForward.media.ui.epg.util.generateTimelineSlots
import com.faithForward.media.ui.epg.util.toDp
import kotlinx.coroutines.delay

data class EpgUiModel(
    val channelWithProgramsUiModels: List<ChannelWithProgramsUiModel>,
    val currentTime: Long = System.currentTimeMillis(),
)

@Composable
fun Epg(
    modifier: Modifier = Modifier,
    epgUiModel: EpgUiModel,
    fragmentManager: FragmentManager,
) {
    Box(modifier = modifier.focusable(false)) {
        AndroidView(
            modifier = Modifier.fillMaxSize().focusable(false),
            factory = { context ->
                // Create a container for the fragment
                FragmentContainerView(context).apply {
                    id = View.generateViewId()


                    // Ensure TV focus can go to children
                    isFocusable = false
                    isFocusableInTouchMode = false
                    descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
                    Log.d("Logging","epgUiModel:${epgUiModel.toNewObj()}")
                    fragmentManager.beginTransaction()
                        .replace(id, EpgFragment().apply {
                            setNewObj(epgUiModel.toNewObj())
                        })
                        .setReorderingAllowed(true)
                        .commit()

                }
            },
            update = { /* You can update the fragment's view here if needed */ }
        )
    }
}


@Preview(device = "id:tv_1080p")
@Composable
private fun EpgPreview() {
//    Epg(
//        epgUiModel = generateSampleEpgUiModel()
//    )
}