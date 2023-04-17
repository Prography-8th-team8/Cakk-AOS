package org.prography.cakk

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.prography.utility.logging.CakkDebugTree
import timber.log.Timber

@HiltAndroidApp
class CakkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(CakkDebugTree())
        }
    }
}
