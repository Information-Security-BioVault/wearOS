/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.project.biovaultwatch.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.chaquo.python.PyException
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.project.biovaultwatch.MyApplication
import com.project.biovaultwatch.R
import com.project.biovaultwatch.presentation.theme.BioVaultWatchTheme


class MainActivity : ComponentActivity() {

    var TAG = "심박수"

    var heartRateList1 = mutableListOf<Float>()
    var heartRateList2 = mutableListOf<Float>()
    var heartRateList3 = mutableListOf<Float>()
    var heartRateList4 = mutableListOf<Float>()
    var heartRateList5 = mutableListOf<Float>()

//    lateinit var client1: PyObject

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private val heartRateListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                val heartRate = event.values[0]
                if(heartRateList1.size < 100) {
                    // 심박수 데이터를 처리합니다.
                    heartRateList1.add(heartRate)
                    MyApplication.progress += 0.01f
                    Log.d(TAG, "heartRate list : ${heartRateList1}")
                    Log.d(TAG, "heartRate : ${heartRate}")
                    if(heartRateList1.size == 100) {
                        Log.d(TAG, "heartRate list size 100")
                        // 처음이 아닐 경우 서버 통신 (심박수 데이터 전송)
                    }

                    if(heartRateList1.size == 500) {
                        Log.d(TAG, "heartRate list size 500")
                    }
                    // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
                    /*  val encryptFunction = client1.callAttr("encrypt")
                        val encryption = encryptFunction.toString()
                        Log.d(TAG, "encryption value : ${encryption}") */
                }
    }
}

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // 정확도 변화 처리
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            MainScreen()
        }

        // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
        /*         // Python 인터프리터 초기화
               if (!Python.isStarted()) {
                    Python.start(AndroidPlatform(this@MainActivity))
                }

                val deviceId = Settings.Secure.getString(
                    contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                Log.d(TAG,"deviceInformation 확인 : ${deviceId}")

                val py = Python.getInstance()
                val pyModule = py.getModule("client")

                // 클라이언트 생성
                client1 = pyModule.callAttr("Client", deviceId)

                // 키 생성 및 로드
                client1.callAttr("create_keys")
                client1.callAttr("load_keys")

                // 인자 설정
                client1.callAttr("set_args")  */
        }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        fun MainScreen() {
        BioVaultWatchTheme {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "HomeScreen"
            ) {
                composable("HomeScreen") {
                    Box(
                        //        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                            .padding(10.dp),
                    ) {
                        TimeText()

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            text = stringResource(R.string.main_information, "Android"),
                        )

                        Button(
                            onClick = {
                                Log.d(TAG, "버튼 클릭")
                                sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                                heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

                                heartRateSensor?.also { sensor ->
                                    sensorManager.registerListener(heartRateListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                                }
                                navController.navigate("LoadingScreen")
                            },
                            enabled = true,
                            modifier = Modifier
                                .padding(vertical = 70.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "심박수 측정"
                            )
                        }

                        Image (
                            painter = painterResource(id = R.drawable.setting),
                            modifier = Modifier
                                .padding(vertical = 100.dp)
                                .pointerInteropFilter {
                                    when (it.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            Log.d(TAG, "Pressed")
                                        }
                                        else -> false
                                    }
                                    true
                                },
                            contentDescription = "setting",
                        )
                    }
                }
                composable("LoadingScreen"){
                    Box(
                        //        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                            .padding(10.dp),
                    ) {
                        TimeText()
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            text = stringResource(R.string.loading_information, "Android"),
                        )
                        CircularProgressIndicator(
                            progress = MyApplication.progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                        )
                    }
                }
                composable("CompleteScreen"){
                    Box(
                        //        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                            .padding(10.dp),
                    ) {
                        TimeText()
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            text = stringResource(R.string.complete_information, "Android"),
                        )
                    }
                }
            }
        }
    }
}
