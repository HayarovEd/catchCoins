package com.edurda77.catchcoins

interface SystemRepo {
    suspend fun getDeepLink(): String?

    fun vpnActive(): Boolean

    fun getBatteryLevel(): Int

    fun checkedInternetConnection(): Boolean

    fun getDataFromSharedPreferences(): String
}