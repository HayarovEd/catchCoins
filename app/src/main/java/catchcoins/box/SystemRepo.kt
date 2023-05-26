package catchcoins.box

interface SystemRepo {

    fun myDeepLink(): Pair<String?, String?>
    //fun getDeepLink(callback: (String?) -> Unit)

    fun vpnActive(): Boolean

    fun getBatteryLevel(): Int

    fun checkedInternetConnection(): Boolean

    fun getDataFromSharedPreferences(): String

    fun apsUid(): String

    //fun getAdvertisingId(): String?
}