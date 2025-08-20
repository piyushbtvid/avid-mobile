package com.faithForward.media

import android.app.Application
import com.faithForward.repository.IapRepository
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var iapRepository: IapRepository
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this) // <-- Required!
        iapRepository
    }
}