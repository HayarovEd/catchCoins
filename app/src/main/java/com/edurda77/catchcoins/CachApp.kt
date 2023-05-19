package com.edurda77.catchcoins

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CachApp:Application() {
    override fun onCreate() {
        super.onCreate()

        AppsFlyerLib.getInstance().init("M5VXFDha2tFitFYxbS4gYC", null, this.applicationContext)
        //AppsFlyerLib.getInstance().start(this.applicationContext)

    }
}