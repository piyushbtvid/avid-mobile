package com.faithForward.media.ui.epg.program

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.faithForward.media.ui.theme._70
import com.faithForward.media.ui.theme.programTitleStyle
import com.faithForward.media.ui.theme.whiteMain
import kotlin.random.Random
import kotlin.random.nextInt

data class ProgramUiModel(
    val programName: String,
    val programTimeString: String,
    val programWidth: Int,
)

@Composable
fun ProgramItem(
    modifier: Modifier = Modifier,
    programUiModel: ProgramUiModel,
    isFocused: Boolean = false,
) {
    val width = remember { (50 until 300).random()*1.dp }
    Column(
        modifier = modifier
            .width(width)
            .height(75.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = _70)
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) whiteMain else Color.Transparent,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(vertical = 15.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = programUiModel.programTimeString,
            style = programTitleStyle.copy(fontWeight = FontWeight.Normal),
            maxLines = 1
        )
        Text(text = programUiModel.programName, style = programTitleStyle, maxLines = 2)
    }
}

@Preview
@Composable
private fun ProgramItemPreview() {
    ProgramItem(
        programUiModel = ProgramUiModel(
            programName = "Drive Thru History Holiday Special",
            programWidth = 23,
            programTimeString = "8:00AM - 9:00AM"
        )
    )
}
@Preview
@Composable
private fun ProgramItemFocusedPreview() {
    ProgramItem(
        programUiModel = ProgramUiModel(
            programName = "Drive Thru History Holiday Special",
            programWidth = 23,
            programTimeString = "8:00AM - 9:00AM"
        ),
        isFocused = true
    )
}