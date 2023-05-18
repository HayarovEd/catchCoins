package com.edurda77.catchcoins

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _showData = MutableLiveData<MainState>(MainState.Loading)
    val showData = _showData
    //private val remoteConfig = Firebase.remoteConfig

    fun getFromLocal(
        pathUrl: String = "",
        checkedInternetConnection: Boolean,
        vpnActive: Boolean,
        batteryLevel: Int,
        deeplink: String?
    ) {
        if (pathUrl != "") {
            if (checkedInternetConnection) {
                _showData.value = MainState.Success(url = pathUrl)

            } else {
                _showData.value = MainState.NoInternet
            }
        } else {
            if (checkedInternetConnection) {
                viewModelScope.launch {
                    /*val configSettings = remoteConfigSettings {
                        minimumFetchIntervalInSeconds = 3600
                    }
                    remoteConfig.setConfigSettingsAsync(configSettings)
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val isCheckedVpn = remoteConfig.getBoolean("to")
                                val resultUrl = remoteConfig.getString("url")
                                val resultUrl2 = remoteConfig.getString("url2")
                                if (isCheckedVpn) {
                                    viewModelScope.launch {
                                        if (checkIsEmu() || resultUrl.isEmpty() || resultUrl2.isEmpty() || vpnActive || batteryLevel > 99) {
                                            _showData.value = MainState.Mock
                                        } else {
                                            if (deeplink==null) {
                                                _showData.value = MainState.Success(url = resultUrl)
                                            } else {
                                                _showData.value = MainState.Success(url = "parseSub(resultUrl2)")
                                            }
                                        }
                                    }
                                } else {
                                    viewModelScope.launch {
                                        if (checkIsEmu() || resultUrl.isEmpty() || resultUrl2.isEmpty() || batteryLevel > 99) {
                                            _showData.value = MainState.Mock
                                        } else {
                                            if (deeplink==null) {
                                                _showData.value = MainState.Success(url = resultUrl)
                                            } else {
                                                _showData.value = MainState.Success(url = "parseSub(resultUrl2)")
                                            }
                                        }
                                    }
                                }

                            } else {
                                _showData.value = MainState.NoInternet
                            }
                        }.addOnFailureListener {
                            _showData.value = MainState.NoInternet
                        }*/
                }

            } else {
                _showData.value = MainState.NoInternet
            }
        }
    }

    private fun checkIsEmu(): Boolean {
        //if (BuildConfig.DEBUG) return false
        val phoneModel = Build.MODEL
        val buildProduct = Build.PRODUCT
        val buildHardware = Build.HARDWARE
        var result = (Build.FINGERPRINT.startsWith("generic")
                || phoneModel.contains("google_sdk")
                || phoneModel.lowercase(Locale.getDefault()).contains("droid4x")
                || phoneModel.contains("Emulator")
                || phoneModel.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || buildHardware == "goldfish"
                || Build.BRAND.contains("google")
                || buildHardware == "vbox86"
                || buildProduct == "sdk"
                || buildProduct == "google_sdk"
                || buildProduct == "sdk_x86"
                || buildProduct == "vbox86p"
                || Build.BOARD.lowercase(Locale.getDefault()).contains("nox")
                || Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox")
                || buildHardware.lowercase(Locale.getDefault()).contains("nox")
                || buildProduct.lowercase(Locale.getDefault()).contains("nox"))
        if (result) return true
        result = result or (Build.BRAND.startsWith("generic") &&
                Build.DEVICE.startsWith("generic"))
        if (result) return true
        result = result or ("google_sdk" == buildProduct)
        return result
    }



    fun parseSub(url:String): Map<String, String> {
        val regex = Regex("""(?<=sub)\d+=([^&]+)""")
        val matches = regex.findAll(url)
        val subIds = mutableMapOf<String, String>()

        for (match in matches) {
            val key = match.groupValues[0][0].toString()
            val value = match.groupValues[1]
            subIds[key] = value
        }
        return subIds
    }
}