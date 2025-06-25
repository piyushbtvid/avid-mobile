package com.faithForward.media.util

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
