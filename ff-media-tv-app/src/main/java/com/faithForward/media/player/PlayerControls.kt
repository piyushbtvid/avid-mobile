package com.faithForward.media.player

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.faithForward.media.R
import com.faithForward.media.theme.focusedMainColor


@Composable
fun PlayerControls(
    currentPosition: Long,
    duration: Long,
    onSeekTo: (Long) -> Unit,
    onPlayPause: () -> Unit,
    onRewind: () -> Unit,
    onBackClick: () -> Unit,
    onForward: () -> Unit,
    onPrev: () -> Unit,
    focusRequester: FocusRequester,
    focusedColor: Color = focusedMainColor,
    prevVideoIc: Int = R.drawable.prev_video_ic,
    nextVideoIc: Int = R.drawable.next_video_ic,
    rewindIc: Int = R.drawable.rewind,
    forwardIc: Int = R.drawable.forword,
    playIc: Int = R.drawable.ic_play,
    pauseIc: Int = R.drawable.baseline_pause_24,
    onNext: () -> Unit,
    isPlaying: Boolean = false,
    shouldShowNextAndPrevVideo: Boolean = false,
    inControllerUp: () -> Boolean = {
        false
    },
) {
    // val focusRequester = remember { FocusRequester() }

//    android.util.Log.e("PLAYER", "PlayerControls  is opended")
//
//    BackHandler {
//        android.util.Log.e("PLAYER", "on back press is called in PlayerControlres")
//        onBackClick.invoke()
//    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // SeekBar with Focus Effect
        TvSeekBar(currentPosition = currentPosition,
            duration = duration,
            focusedColor = focusedColor,
            inControllerUp = inControllerUp,
            onSeekTo = { onSeekTo(it) },
            onRewind = { onRewind() },
            onForward = { onForward() })

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (shouldShowNextAndPrevVideo) {
                    FocusableIconButton(
                        onClick = onPrev,
                        imageResId = prevVideoIc,
                        description = "Previous",
                        focusedColor = focusedColor,
                        focusRequester = null,
                        overrideDpadLeft = true
                    )
                }
                FocusableIconButton(
                    onClick = onRewind,
                    imageResId = rewindIc,
                    focusRequester = null,
                    focusedColor = focusedColor,
                    description = "Rewind"
                )
                FocusableIconButton(
                    onClick = onPlayPause,
                    imageResId = if (isPlaying) pauseIc else playIc,
                    focusRequester = focusRequester,
                    focusedColor = focusedColor,
                    description = "Play/Pause"
                )
                FocusableIconButton(
                    onClick = onForward,
                    imageResId = forwardIc,
                    focusRequester = null,
                    focusedColor = focusedColor,
                    description = "Forward"
                )
                if (shouldShowNextAndPrevVideo) {
                    FocusableIconButton(
                        onClick = onNext,
                        imageResId = nextVideoIc,
                        focusRequester = null,
                        focusedColor = focusedColor,
                        description = "Next",
                        overrideDpadRight = true
                    )
                }
            }
            //Row with icons of player
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatTime(currentPosition),
                    color = Color.White,
                    modifier = Modifier.focusable(enabled = false)
                )

                Text(
                    text = formatTime(duration),
                    color = Color.White,
                    modifier = Modifier.focusable(enabled = false)
                )
            }
        }


    }
}


// Formatting Time (mm:ss)
fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}


@Composable
fun FocusableIconButton(
    onClick: () -> Unit,
    @DrawableRes imageResId: Int,
    description: String,
    focusedColor: Color,
    focusRequester: FocusRequester?,
    colorFilter: ColorFilter? = ColorFilter.tint(color = Color.White),
    overrideDpadRight: Boolean = false, // NEW PARAMETER
    overrideDpadLeft: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .focusRequester(focusRequester ?: FocusRequester())
            .focusable(interactionSource = interactionSource)
            .onKeyEvent { event ->
                if (isFocused && event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.DirectionRight, Key.Forward -> {
                            if (overrideDpadRight) {
                                return@onKeyEvent true // Override handled
                            }
                        }

                        Key.DirectionLeft, Key.MediaRewind -> {
                            if (overrideDpadLeft) {
                                return@onKeyEvent true // Override handled
                            }
                        }
                    }
                }
                false // Let system handle others
            }
            .wrapContentSize()
            .background(if (isFocused) focusedColor else Color.Transparent)
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        LoadImage(
            imageResId = imageResId,
            modifier = Modifier.size(28.dp),
            colorFilter = if (isFocused) ColorFilter.tint(Color.White) else colorFilter
        )
    }
}


@Composable
internal fun LoadImage(
    @DrawableRes imageResId: Int,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    colorFilter: ColorFilter? = null,
) {

    val painter = painterResource(id = imageResId)
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        colorFilter = colorFilter
    )

}

@Composable
fun TvSeekBar(
    currentPosition: Long,
    duration: Long,
    onSeekTo: (Long) -> Unit,
    focusedColor: Color,
    inControllerUp: () -> Boolean = {
        false
    },
    onRewind: () -> Unit,
    onForward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val seekBarInteractionSource = remember { MutableInteractionSource() }
    val seekIsFocused by seekBarInteractionSource.collectIsFocusedAsState()

    Box(modifier = modifier
        .fillMaxWidth()
        .focusable(interactionSource = seekBarInteractionSource)
        .focusRequester(focusRequester)
        .onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false

            return@onKeyEvent when (event.key) {
                Key.DirectionLeft -> {
                    onRewind()
                    true
                }

                Key.DirectionRight -> {
                    onForward()
                    true
                }

                Key.DirectionUp -> {
                    inControllerUp.invoke()
                }

                else -> false
            }
        }
        .border(
            width = if (seekIsFocused) 3.dp else 0.dp,
            color = Color.Transparent,
            shape = RoundedCornerShape(8.dp)
        )) {

        val sliderColor = SliderDefaults.colors(
            thumbColor = if (seekIsFocused) focusedColor else Color.White, // Color of the thumb (circle)
            activeTrackColor = focusedColor, // Color of the filled progress
            inactiveTrackColor = Color.Gray, // Color of the remaining track
            activeTickColor = Color.Transparent, inactiveTickColor = Color.Transparent
        )


        Slider(
            value = currentPosition.toFloat(),
            valueRange = 0f..duration.toFloat(),
            onValueChange = { onSeekTo(it.toLong()) },
            colors = sliderColor,
            modifier = Modifier.focusable(enabled = false),
//        enabled = false
        )
    }

}