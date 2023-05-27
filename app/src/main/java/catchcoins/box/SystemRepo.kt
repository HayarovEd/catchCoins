package catchcoins.box

interface SystemRepo {

    suspend fun myDeepLink(): Pair<String?, String?>
    //fun getDeepLink(callback: (String?) -> Unit)

    suspend fun getFacebookDeepLink(): String?

    fun vpnActive(): Boolean

    fun getBatteryLevel(): Int

    fun checkedInternetConnection(): Boolean

    fun getDataFromSharedPreferences(): String

    fun apsUid(): String

    //fun getAdvertisingId(): String?
}