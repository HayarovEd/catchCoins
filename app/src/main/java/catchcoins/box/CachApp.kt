package catchcoins.box

import android.app.Application
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