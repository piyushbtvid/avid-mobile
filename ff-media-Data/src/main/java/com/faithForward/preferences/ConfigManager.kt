package com.faithForward.preferences

import android.util.Log
import com.faithForward.network.dto.ConfigData

/**
 * Simple Singleton class to store ConfigData globally throughout the app.
 * ConfigData is stored only in memory and loaded fresh from API on each app launch.
 */
object ConfigManager {
    private const val TAG = "ConfigViewModel"

    @Volatile
    private var configData: ConfigData? = null

    /**
     * Get the current ConfigData synchronously.
     * Returns null if config hasn't been loaded yet.
     */
    fun getConfigData(): ConfigData? = configData

    /**
     * Set ConfigData in the singleton.
     * This should be called after successfully fetching from API.
     */
    fun setConfigData(data: ConfigData) {
        configData = data
        Log.e(TAG, "ConfigData set successfully")
    }

    /**
     * Clear stored ConfigData (useful for logout or reset scenarios).
     */
    fun clearConfigData() {
        configData = null
        Log.e(TAG, "ConfigData cleared")
    }
}
