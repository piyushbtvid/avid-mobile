package com.faithForward.media.ui.epg

import android.text.SpannableString
import com.egeniq.androidtvprogramguide.entity.ProgramGuideSchedule

fun EpgUiModel.toNewObj(): EpgFragment.NewObj {
    val newChannels = channelWithProgramsUiModels.map { channelWithPrograms ->
        EpgFragment.SimpleChannel(
            id = channelWithPrograms.channelUiModel.channelName, // Ideally a unique ID
            name = SpannableString(channelWithPrograms.channelUiModel.channelName),
            imageUrl = channelWithPrograms.channelUiModel.channelImage
        )
    }

    val newChannelEntries: Map<String, List<ProgramGuideSchedule<EpgFragment.SimpleProgram>>> =
        channelWithProgramsUiModels.associate { channelWithPrograms ->
            val channelId = channelWithPrograms.channelUiModel.channelName

            val schedules = channelWithPrograms.programs.mapIndexed { index, program ->
                val simpleProgram = EpgFragment.SimpleProgram(
                    id = index.toString(), // or program-specific ID if available
                    title = program.programName,
                    description = program.programTimeString, // You might replace with actual desc
                    metadata = "Starts: ${program.programStartTime}, Ends: ${program.programEndTime}",
                    thumbnail = null, // Fill if you have thumbnails
                    videoUrl = null    // Fill if you have video URLs
                )

                ProgramGuideSchedule(
                    id = index.toLong(),
                    startsAtMillis = program.programStartTime,
                    endsAtMillis = program.programEndTime,
                    originalTimes = ProgramGuideSchedule.OriginalTimes(
                        startsAtMillis = program.programStartTime,
                        endsAtMillis = program.programEndTime
                    ),
                    isClickable = true,
                    displayTitle = program.programName,
                    program = simpleProgram
                )
            }

            channelId to schedules
        }

    return EpgFragment.NewObj(
        newChannels = newChannels,
        newChannelEntries = newChannelEntries
    )
}
