package com.faithForward.media.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.provider.Settings
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.io.IOException

object Util {
    fun Context.isTvDevice(): Boolean {
        return packageManager.hasSystemFeature("android.software.leanback")
    }


    fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }



    suspend fun getId(context: Context): String {
        return try {
            AdvertisingIdClient.getAdvertisingIdInfo(context).id ?: "123456"
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Logging", "AdvertisingIdClient:IOException:${e.stackTrace.toString()}")
            "123456789-01234567890"
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Log.e("Logging", "AdvertisingIdClient:IllegalStateException:${e.stackTrace.toString()}")
            "123456789-01234567890"
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
            Log.e(
                "Logging",
                "AdvertisingIdClient:GooglePlayServicesNotAvailableException:${e.stackTrace}"
            )
            "123456789-01234567890"
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
            Log.e("Logging", "AdvertisingIdClient:Exception:${e.stackTrace}")
            "123456789-01234567890"
        }
    }

    fun isFireTv(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("amazon.hardware.fire_tv")
    }

    fun getFireTvId(context: Context): String? {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
    }

}