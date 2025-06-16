package com.faithForward.media.theme

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
    color = cardShadowColor
)

val tv_02 = baseTextStyle.copy(
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
    color = cardShadowColor
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