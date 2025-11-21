package com.faithForward.media

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    
    override val workManagerConfiguration: Configuration by lazy {
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }
    
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this) // <-- Required!
        // Initialize WorkManager with our configuration to ensure PendingIntent flags are set correctly
        try {
            WorkManager.initialize(this, workManagerConfiguration)
        } catch (e: IllegalStateException) {
            // WorkManager might already be initialized, which is fine
            // The Configuration.Provider will be used for subsequent initializations
        }
    }
}