package com.faithForward.media.ui.epg

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat


//data class ChannelWithPrograms(
//    val channel: Channel,
//    val programs: List<Program>,
//)
//
//
//data class Program(
//    val programName: String,
//)
//
//@Composable
//fun EpgCompose(
//    modifier: Modifier = Modifier,
//    channelPrograms: List<ChannelWithPrograms>,
//    startTime: Long = System.currentTimeMillis(),
//) {
//    @Composable
//    fun EPGScreenWithChannelPrograms(
//
//    ) {
//        val verticalScrollState = rememberLazyListState()
//        val horizontalScrollState = rememberLazyListState()
//
//        Column {
//            // Time Grid Header
//            Row {
//                Spacer(modifier = Modifier.width(100.dp))
//                LazyRow(state = horizontalScrollState, modifier = Modifier.height(50.dp)) {
//                    items(24) { i ->
//                        val label =
//                            SimpleDateFormat("HH:mm").format(Date(startTime + i * 30 * 60 * 1000))
//                        Box(
//                            modifier = Modifier
//                                .width(120.dp)
//                                .fillMaxHeight()
//                                .background(Color.Black),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(text = label, color = Color.White)
//                        }
//                    }
//                }
//            }
//
//            Row {
//                // Channel Labels
//                LazyColumn(state = verticalScrollState, modifier = Modifier.width(100.dp)) {
//                    items(channelPrograms) { item ->
//                        Box(
//                            modifier = Modifier
//                                .height(70.dp)
//                                .background(Color.DarkGray),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(item.channel.name, color = Color.White)
//                        }
//                    }
//                }
//
//                // Program Grid
//                LazyColumn(state = verticalScrollState, modifier = Modifier.weight(1f)) {
//                    items(channelPrograms) { item ->
//                        LazyRow(state = horizontalScrollState, modifier = Modifier.height(70.dp)) {
//                            items(item.programs) { program ->
//                                val width = calculateProgramWidth(program)
//                                Box(
//                                    modifier = Modifier
//                                        .width(width)
//                                        .padding(4.dp)
//                                        .background(Color.Blue)
//                                        .focusable()
//                                        .border(2.dp, Color.Yellow),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(program.title, color = Color.White)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    @Preview
//    @Composable
//    private fun EpgComposePreview() {
//        EpgCompose()
//    }