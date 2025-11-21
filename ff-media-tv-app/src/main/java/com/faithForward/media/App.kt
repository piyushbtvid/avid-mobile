package com.faithForward.media

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.faithForward.repository.IapRepository
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import org.conscrypt.Conscrypt
import java.security.Security
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var iapRepository: IapRepository

    override val workManagerConfiguration: Configuration by lazy {
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this) // <-- Required!
        iapRepository
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        // Initialize WorkManager with our configuration to ensure PendingIntent flags are set correctly
        try {
            WorkManager.initialize(this, workManagerConfiguration)
        } catch (e: IllegalStateException) {
            // WorkManager might already be initialized, which is fine
            // The Configuration.Provider will be used for subsequent initializations
        }
    }
}