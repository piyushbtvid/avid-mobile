package com.faithForward.media.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)


val baseTextStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 9.sp
)


val creatorTitleStyle = baseTextStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 30.sp,
    color = whiteMain
)

val tv_02 = baseTextStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
    color = whiteMain
)

val metaDataTextStyle = TextStyle(
    fontSize = 10.sp,
    lineHeight = 10.sp,
    color = whiteMain
)

val titleTextStyle = TextStyle(
    fontSize = 28.sp,
    lineHeight = 29.sp,
    color = whiteMain
)

val pillTextFocusedStyle = TextStyle(
    fontSize = 15.sp,
    color = pillButtonTextColor
)

val pillTextUnFocusedStyle = TextStyle(
    fontSize = 15.sp,
    color = pillButtonTextUnFocusColor
)

val topBarTextFocusedStyle = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 10.sp,
    color = focusedMainColor
)

val topBarTextUnFocusedStyle = TextStyle(
    fontSize = 10.sp,
    lineHeight = 10.sp,
    fontWeight = FontWeight.Normal,
    color = pillButtonTextUnFocusColor
)

val watchNowTextStyle = TextStyle(
    fontSize = 10.sp,
    color = pillButtonTextUnFocusColor
)

val detailNowTextStyle = TextStyle(
    fontSize = 15.sp,
    color = whiteMain
)

val detailNowUnFocusTextStyle = TextStyle(
    fontSize = 15.sp,
    color = focusedTextColor
)


val descriptionTextStyle = TextStyle(
    fontSize = 12.sp,
    lineHeight = 13.sp,
    color = whiteMain
)

val SearchBasicTextFieldFocusedTextStyle = TextStyle(
    fontSize = 18.sp,
    lineHeight = 16.sp,
    color = Color.White
)

val SearchBasicTextFieldUnFocusedTextStyle = TextStyle(
    fontSize = 18.sp,
    lineHeight = 16.sp,
    color = Color.White.copy(alpha = 0.5f)
)

val SearchHereStyle = TextStyle(
    color = Color.White.copy(alpha = 0.6f),
    fontSize = 18.sp,
    lineHeight = 16.sp
)