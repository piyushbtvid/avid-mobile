package com.faithForward.media.ui.epg

import android.os.Bundle
import android.text.Spanned
import android.text.SpannedString
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.leanback.widget.VerticalGridView
import androidx.lifecycle.lifecycleScope
import com.egeniq.androidtvprogramguide.ProgramGuideFragment
import com.egeniq.androidtvprogramguide.entity.ProgramGuideChannel
import com.egeniq.androidtvprogramguide.entity.ProgramGuideSchedule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.random.Random

@AndroidEntryPoint
class EpgFragment : ProgramGuideFragment<EpgFragment.SimpleProgram>() {


    var epgFragmentListener: EpgFragmentListener? = null

    override val CAN_FOCUS_CHANNEL = false
    override val SCROLL_SYNCING = true
    override val DISPLAY_TIMEZONE: ZoneId = ZoneOffset.ofHoursMinutes(5, 30)
    private var newObj : NewObj? = null
    fun setNewObj(newObj: NewObj){
        this.newObj = newObj
    }


    data class NewObj(
        val newChannels: List<ProgramGuideChannel>,
        val newChannelEntries: Map<String, List<ProgramGuideSchedule<SimpleProgram>>>,
    )

    data class SimpleChannel(
        override val id: String,
        override val name: Spanned?,
        override val imageUrl: String?,
    ) : ProgramGuideChannel

    // You can put your own data in the program class
    data class SimpleProgram(
        val id: String,
        val title: String,
        val description: String,
        val metadata: String,
        val thumbnail: String? = null,
        val videoUrl: String? = null,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun isTopMenuVisible(): Boolean {
        return false
    }

    override fun requestingProgramGuideFor(localDate: LocalDate) {
        setState(State.Loading)

        if (newObj?.newChannels!=null && newObj?.newChannelEntries!=null){
            setData(
                newChannels = newObj?.newChannels!!,
                newChannelEntries = newObj?.newChannelEntries!!,
                selectedDate = localDate
            )
        }

        setState(State.Content)
        updateCurrentTimeIndicator()
        autoScrollToBestProgramme(

        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (newObj != null) {
//            requestingProgramGuideFor(LocalDate.now())
//        }
    }

    override fun requestRefresh() {
    requestingProgramGuideFor(LocalDate.now())
    }

    override fun onScheduleClicked(programGuideSchedule: ProgramGuideSchedule<SimpleProgram>) {
        val innerSchedule = programGuideSchedule.program
        if (innerSchedule == null) {
            Log.w(TAG, "Unable to open schedule!")
            return
        }
        if (programGuideSchedule.program?.videoUrl.isNullOrBlank()) {
            return
        }

        val currentTimeMillis = System.currentTimeMillis()

    }

    override fun onScheduleSelected(programGuideSchedule: ProgramGuideSchedule<SimpleProgram>?) {
        epgFragmentListener?.onSelect(programGuideSchedule?.program)
    }

    override fun onChannelSelected(channel: ProgramGuideChannel) {
        super.onChannelSelected(channel)
    }

    override fun onChannelClicked(channel: ProgramGuideChannel) {
        super.onChannelClicked(channel)
        Toast.makeText(context, "Channel clicked: ${channel.name}", Toast.LENGTH_LONG).show()
    }


    interface EpgFragmentListener {
        fun onSelect(simpleProgram: SimpleProgram?)
    }
}