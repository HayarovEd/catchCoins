package catchcoins.box

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

        AppsFlyerLib.getInstance().init("EFvkQNfBR8Yip9uHWXUd3F", null, this.applicationContext)
        //AppsFlyerLib.getInstance().start(this.applicationContext)

    }
}