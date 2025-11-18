package com.faithForward.media

import android.app.Application
import com.faithForward.repository.IapRepository
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import org.conscrypt.Conscrypt
import java.security.Security
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var iapRepository: IapRepository
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this) // <-- Required!
        iapRepository
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
    }
}