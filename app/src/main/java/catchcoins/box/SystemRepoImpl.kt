package catchcoins.box

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.io.IOException
import javax.inject.Inject

class SystemRepoImpl @Inject constructor(private val application: Application) : SystemRepo {

    override fun myDeepLink(): Pair<String?, String?> {
        var deeplinkAndAdvId: Pair<String?, String?> = Pair(null, null)
        getDeepLink { deepLink ->
            val advId = getAdvertisingId()
            deeplinkAndAdvId= Pair(deepLink, advId)
        }
        return deeplinkAndAdvId
    }

    private fun getDeepLink(callback: (String?) -> Unit) {
        AppLinkData.fetchDeferredAppLinkData(application) { appLinkData ->
            if (appLinkData != null) {
                val targetUri = appLinkData.targetUri
                val deepLink = targetUri.toString()
                callback(deepLink)
            } else {
                callback(null)
            }
        }
    }

    override fun vpnActive(): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (caps != null) {
                return caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.type == ConnectivityManager.TYPE_VPN
            }
        }
        return false
    }

     override fun getBatteryLevel(): Int {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = application.applicationContext.registerReceiver(null, iFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        return (level ?: 0) * 100 / (scale ?: 0)
    }

    override fun checkedInternetConnection(): Boolean {
        var result = false
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    override fun getDataFromSharedPreferences(): String {
        val sharedPref =
            application.getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)
        return sharedPref.getString(URL, "")?:""
    }

    override fun apsUid(): String {
        return AppsFlyerLib.getInstance().getAppsFlyerUID(application).toString()
    }

    private fun getAdvertisingId(): String? {
        var advertisingId: String? = null

        val thread = Thread {
            try {
                val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(application)
                advertisingId = advertisingIdInfo.id
            } catch (e: IOException) {
                e.printStackTrace()
                System.err.println(e)
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
                System.err.println(e)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
                System.err.println(e)
            }


        }

        thread.start()
        thread.join() // Дождитесь завершения потока, если вам нужно получить результат

        return advertisingId
    }

}
