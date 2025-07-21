package com.faithForward.media.ui.epg.program

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.metaDataTextStyle
import com.faithForward.media.ui.theme.pillButtonTextUnFocusColor
import com.faithForward.media.ui.theme.programTitleStyle
import com.faithForward.media.ui.theme.white96

data class Program(
    val programName: String,
    val programTimeString: String,
    val programWidth: Int,
)

@Composable
fun ProgramItem(
    modifier: Modifier = Modifier,
    program: Program,
) {
    Column(
        modifier = modifier
            .width(136.5.dp)
            .height(75.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(color = white96)
            .padding(vertical = 15.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = program.programTimeString, style = programTitleStyle.copy(fontWeight = FontWeight.Normal))
        Text(text = program.programName, style = programTitleStyle)
    }
}

@Preview
@Composable
private fun ProgramItemPreview() {
    ProgramItem(
        program = Program(
            programName = "Drive Thru History Holiday Special",
            programWidth = 23,
            programTimeString = "8:00AM - 9:00AM"
        )
    )
}