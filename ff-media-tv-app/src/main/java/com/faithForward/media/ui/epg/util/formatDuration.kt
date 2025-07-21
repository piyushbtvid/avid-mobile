package com.faithForward.media.ui.epg.util

fun formatDuration(durationMillis: Long): String {
    val totalSeconds = (durationMillis / 1000).toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> String.format("%dh %02dm", hours, minutes)
        minutes > 0 -> String.format("%d min %02d sec", minutes, seconds)
        else -> String.format("%d sec", seconds)
    }
}


fun formatDurationInReadableFormat(seconds: Int?): String {
    if (seconds == null || seconds <= 0) return ""

    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60

    return buildString {
        if (h > 0) append("${h}h ")
        if (m > 0) append("${m}m ")
        if (s > 0 || (h == 0 && m == 0)) append("${s}s")
    }.trim()
}
