package com.faithForward.media.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.faithForward.media.util.Util.isTvDevice

/**
 * Device type enumeration
 */
enum class DeviceType {
    TV,
    MOBILE
}

/**
 * Composable that provides device type information
 * This should be used instead of calling LocalContext.current.isTvDevice() directly
 * as it caches the result and provides better performance
 */
@Composable
fun rememberDeviceType(): DeviceType {
    val context = LocalContext.current
    return remember(context) {
        if (context.isTvDevice()) DeviceType.TV else DeviceType.MOBILE
    }
}

/**
 * Composable that provides a boolean indicating if the device is a TV
 * This is a convenience function for cases where you only need the boolean
 */
@Composable
fun rememberIsTvDevice(): Boolean {
    return rememberDeviceType() == DeviceType.TV
}

/**
 * Composable that provides a boolean indicating if the device is mobile
 * This is a convenience function for cases where you only need the boolean
 */
@Composable
fun rememberIsMobileDevice(): Boolean {
    return rememberDeviceType() == DeviceType.MOBILE
}
