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
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: SystemRepo) : ViewModel() {
    private val _showData = MutableLiveData<MainState>(MainState.Loading)
    val showData = _showData
    private val remoteConfig = Firebase.remoteConfig

    init {
        //getFromLocal()
    }
    fun getFromLocal(
        deeplink: String?,
        advId: String?
    ) {
        val pathUrl = repo.getDataFromSharedPreferences()
        val checkInternet = repo.checkedInternetConnection()
        Log.d("AAAAA", "pathUrl $pathUrl")
        Log.d("AAAAA", "checkInternet $checkInternet")
        if (pathUrl != "") {
            if (checkInternet) {
                _showData.value = MainState.Success(url = pathUrl)

            } else {
                _showData.value = MainState.NoInternet
            }
        } else {
            if (checkInternet) {
                viewModelScope.launch {
                    val deeplinkAndAdvId = repo.myDeepLink()
                    val configSettings = remoteConfigSettings {
                        minimumFetchIntervalInSeconds = 3600
                    }
                    //val deeplink = deeplinkAndAdvId.first
                    //val advId = deeplinkAndAdvId.second
                    remoteConfig.setConfigSettingsAsync(configSettings)
                    println("deeplink $deeplink")
                    val apsUid = repo.apsUid()
                    remoteConfig.fetchAndActivate()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                //val isCheckedVpn = false//remoteConfig.getBoolean("to")
                                val resultUrl1 = "https://ya.ru/"//remoteConfig.getString("url1")
                                val resultUrl2 = "https://www.google.com/"//remoteConfig.getString("url2")
                                println("Url1 $resultUrl1")
                                println("Url2 $resultUrl2")

                                println("apsUid $apsUid")
                                println("advId $advId")
                                _showData.value = MainState.Success("advId $advId deeplink $deeplink")
                                /*if ((resultUrl1.isEmpty() && resultUrl2.isEmpty())) {
                                    _showData.value = MainState.Mock
                                } else {
                                    //val deeplink = repo.getDeepLink()

                                    if (deeplink==null) {
                                        _showData.value = MainState.Success(url = resultUrl1)
                                    } else {
                                        val subIds = parseSub(deeplink)
                                        val link = "${resultUrl2}?key=${subIds["5"]}&sub1=${subIds["1"]}&sub2=${subIds["2"]}&sub3=${subIds["3"]}&sub4=${subIds["4"]}&adv_id=${advId}&apps_id=${apsUid}"
                                        Log.d("AAAAA", "link $link")
                                        println("resultLink $link")
                                        _showData.value = MainState.Success(url = link)
                                    }
                                }*/
                                /*//val vpnActive = repo.vpnActive()
                                //val batteryLevel = repo.getBatteryLevel()
                                Log.d("AAAAA", "isCheckedVpn $isCheckedVpn")
                                Log.d("AAAAA", "resultUrl $resultUrl1")
                                Log.d("AAAAA", "resultUrl2 $resultUrl2")
                                //Log.d("AAAAA", "vpnActive $vpnActive")
                                //Log.d("AAAAA", "batteryLevel $batteryLevel")
                                if (isCheckedVpn) {
                                    viewModelScope.launch {
                                        //Log.d("AAAAA", "checkIsEmu ${checkIsEmu()}")
                                        if (*//*checkIsEmu() ||*//* (resultUrl1.isEmpty() && resultUrl2.isEmpty()) *//*|| vpnActive || batteryLevel > 99*//*) {
                                            _showData.value = MainState.Mock
                                        } else {
                                            val deeplink = repo.getDeepLink()
                                            val apsUid = repo.apsUid()
                                            val advId = repo.getAdvertisingId()
                                            Log.d("AAAAA", "deeplink $deeplink")
                                            Log.d("AAAAA", "apsUid $apsUid")
                                            Log.d("AAAAA", "advId $advId")
                                            println("deeplink $deeplink")
                                            println("apsUid $apsUid")
                                            println("advId $advId")
                                            if (deeplink==null||advId==null) {
                                                _showData.value = MainState.Success(url = resultUrl1)
                                            } else {
                                                val subIds = parseSub(deeplink)
                                                val link = "${resultUrl2}?key=${subIds["5"]}&sub1=${subIds["1"]}&sub2=${subIds["2"]}&sub3=${subIds["3"]}&sub4=${subIds["4"]}&adv_id=${advId}&apps_id=${apsUid}"
                                                Log.d("AAAAA", "link $link")
                                                println("link $link")
                                                _showData.value = MainState.Success(url = link)
                                            }
                                        }
                                    }
                                } else {
                                    viewModelScope.launch {
                                        //Log.d("AAAAA", "checkIsEmu ${checkIsEmu()}")
                                        if (*//*checkIsEmu() ||*//* (resultUrl1.isEmpty() && resultUrl2.isEmpty()) *//*|| batteryLevel > 99*//*) {
                                            _showData.value = MainState.Mock
                                        } else {
                                            val deeplink = repo.getDeepLink()
                                            val apsUid = repo.apsUid()
                                            val advId = repo.getAdvertisingId()
                                            println("deeplink $deeplink")
                                            println("apsUid $apsUid")
                                            println("advId $advId")
                                            Log.d("AAAAA", "deeplink $deeplink")
                                            Log.d("AAAAA", "apsUid $apsUid")
                                            Log.d("AAAAA", "advId $advId")
                                            if (deeplink==null||advId==null) {
                                                _showData.value = MainState.Success(url = resultUrl1)
                                            } else {
                                                val subIds = parseSub(deeplink)
                                                val link = "${resultUrl2}?key=${subIds["5"]}&sub1=${subIds["1"]}&sub2=${subIds["2"]}&sub3=${subIds["3"]}&sub4=${subIds["4"]}&adv_id=${advId}&apps_id=${apsUid}"
                                                Log.d("AAAAA", "link $link")
                                                println("link $link")
                                                _showData.value = MainState.Success(url = link)
                                            }
                                        }
                                    }
                                }*/

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