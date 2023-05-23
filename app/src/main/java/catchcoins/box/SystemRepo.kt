package catchcoins.box

interface SystemRepo {
    suspend fun getDeepLink(): String?

    fun vpnActive(): Boolean

    fun getBatteryLevel(): Int

    fun checkedInternetConnection(): Boolean

    fun getDataFromSharedPreferences(): String

    fun apsUid(): String
}