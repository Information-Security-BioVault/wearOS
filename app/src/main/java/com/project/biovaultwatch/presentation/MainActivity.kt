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
import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.google.android.material.snackbar.Snackbar
import com.project.biovaultwatch.MyApplication
import com.project.biovaultwatch.PreferenceUtil
import com.project.biovaultwatch.R
import com.project.biovaultwatch.api.ApiClient
import com.project.biovaultwatch.api.model.request.AuthenticationRequestModel
import com.project.biovaultwatch.api.model.request.EnrollRequestModel
import com.project.biovaultwatch.api.model.response.AuthenticationResponseModel
import com.project.biovaultwatch.api.model.response.EnrollResponseModel
import com.project.biovaultwatch.presentation.theme.BioVaultWatchTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {

    var TAG = "심박수"

    var heartRateList = mutableListOf<Float>()

    var deviceId = ""

    lateinit var navController: NavHostController

    private lateinit var wakeLock: PowerManager.WakeLock

//    var list = listOf(listOf(83.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 90.0, 90.0, 91.0, 91.0, 92.0, 93.0, 94.0, 93.0, 95.0, 94.0, 94.0, 94.0, 92.0, 91.0, 92.0, 92.0, 91.0, 92.0, 94.0, 94.0, 93.0, 91.0, 92.0, 91.0, 92.0, 93.0, 92.0, 92.0, 93.0, 92.0, 91.0, 91.0, 90.0, 90.0, 90.0, 89.0, 90.0, 90.0, 91.0, 91.0, 92.0, 93.0, 93.0, 93.0, 93.0, 93.0, 92.0, 91.0, 90.0, 89.0, 89.0, 88.0, 88.0, 89.0, 88.0, 89.0, 89.0, 89.0, 88.0, 87.0, 86.0, 85.0, 84.0, 83.0, 82.0, 81.0, 81.0, 82.0, 84.0, 85.0, 83.0, 81.0, 79.0, 78.0, 79.0, 78.0, 78.0, 77.0, 76.0, 76.0, 77.0, 77.0, 78.0, 79.0, 81.0, 82.0, 81.0, 82.0),
//        listOf(83.0, 83.0, 82.0, 81.0, 82.0, 81.0, 81.0, 81.0, 82.0, 82.0, 82.0, 82.0, 80.0, 80.0, 78.0, 77.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 77.0, 77.0, 76.0, 76.0, 76.0, 77.0, 77.0, 77.0, 77.0, 78.0, 79.0, 80.0, 81.0, 82.0, 83.0, 83.0, 83.0, 82.0, 82.0, 81.0, 80.0, 80.0, 79.0, 80.0, 81.0, 81.0, 81.0, 83.0, 83.0, 83.0, 85.0, 86.0, 86.0, 87.0, 87.0, 86.0, 86.0, 85.0, 85.0, 85.0, 83.0, 82.0, 81.0, 80.0, 80.0, 79.0, 79.0, 78.0, 78.0, 78.0, 77.0, 77.0, 77.0, 78.0, 79.0, 78.0, 77.0, 77.0, 78.0, 79.0, 80.0, 82.0, 83.0, 84.0, 85.0, 85.0, 85.0, 84.0, 83.0, 83.0, 82.0, 81.0, 81.0, 80.0, 79.0, 79.0),
//        listOf(79.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 79.0, 79.0, 79.0, 80.0, 80.0, 80.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 80.0, 80.0, 79.0, 78.0, 77.0, 77.0, 76.0, 76.0, 76.0, 76.0, 76.0, 77.0, 77.0, 78.0, 79.0, 80.0, 80.0, 80.0, 79.0, 79.0, 80.0, 80.0, 81.0, 81.0, 81.0, 80.0, 80.0, 81.0, 81.0, 81.0, 82.0, 83.0, 84.0, 84.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 82.0, 82.0, 83.0, 83.0, 82.0, 81.0, 81.0, 81.0, 80.0, 81.0, 82.0, 82.0, 82.0, 82.0, 82.0, 81.0, 81.0, 80.0, 80.0, 81.0, 81.0, 81.0, 80.0, 80.0, 79.0, 79.0, 80.0, 81.0, 81.0, 82.0, 83.0, 83.0, 82.0, 81.0, 81.0, 80.0, 80.0, 80.0, 81.0, 82.0),
//        listOf(82.0, 82.0, 81.0, 80.0, 79.0, 79.0, 79.0, 78.0, 78.0, 79.0, 80.0, 79.0, 78.0, 77.0, 77.0, 77.0, 76.0, 76.0, 76.0, 75.0, 75.0, 74.0, 75.0, 77.0, 79.0, 81.0, 80.0, 79.0, 79.0, 78.0, 78.0, 78.0, 77.0, 77.0, 77.0, 77.0, 79.0, 79.0, 80.0, 79.0, 79.0, 78.0, 78.0, 78.0, 79.0, 77.0, 77.0, 77.0, 78.0, 79.0, 80.0, 79.0, 79.0, 78.0, 79.0, 79.0, 80.0, 79.0, 79.0, 79.0, 78.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 80.0, 80.0, 81.0, 81.0, 82.0, 83.0, 84.0, 85.0, 86.0, 87.0, 89.0, 90.0, 91.0, 93.0, 92.0, 91.0, 91.0, 92.0, 94.0, 96.0, 95.0, 95.0, 95.0, 94.0, 94.0, 93.0, 92.0, 91.0, 90.0, 89.0, 90.0, 91.0, 91.0),
//        listOf(91.0, 90.0, 89.0, 88.0, 87.0, 86.0, 85.0, 85.0, 85.0, 85.0, 84.0, 86.0, 86.0, 85.0, 86.0, 88.0, 90.0, 91.0, 93.0, 95.0, 93.0, 95.0, 95.0, 94.0, 93.0, 92.0, 90.0, 88.0, 88.0, 86.0, 83.0, 80.0, 79.0, 78.0, 77.0, 76.0, 76.0, 77.0, 77.0, 77.0, 78.0, 78.0, 80.0, 80.0, 79.0, 79.0, 78.0, 78.0, 78.0, 79.0, 80.0, 81.0, 81.0, 82.0, 82.0, 82.0, 83.0, 83.0, 83.0, 81.0, 80.0, 79.0, 77.0, 77.0, 76.0, 78.0, 79.0, 80.0, 81.0, 81.0, 81.0, 80.0, 78.0, 78.0, 78.0, 79.0, 80.0, 80.0, 81.0, 82.0, 83.0, 85.0, 86.0, 88.0, 89.0, 90.0, 91.0, 91.0, 91.0, 90.0, 88.0, 87.0, 86.0, 86.0, 85.0, 84.0, 84.0, 84.0, 84.0, 84.0))


//    lateinit var client1: PyObject

    // 센서를 통해 심박수 데이터 받아오기
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private val heartRateListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                val heartRate = event.values[0]
                // 심박수 데이터를 처리합니다.
                heartRateList.add(heartRate)
                MyApplication.progress += 0.01f
                Log.d(TAG, "heartRate list : ${heartRateList}")
                Log.d(TAG, "heartRate : ${heartRate}")
                Log.d(TAG, "heartRate : ${heartRateList.size}")
                if (MyApplication.status == "register") {
                    // 정보 등록 서버 통신 (심박수 데이터 등록)
                    if (heartRateList.size == 500) {
                        Log.d(TAG, "heartRate list size 500")
                        // 100개씩 나누어 5개의 리스트로 분할
                        var chunkedLists = heartRateList.chunked(100)

                        // 결과 출력
                        chunkedLists.forEachIndexed { index, list ->
                            Log.d(TAG, "List ${index + 1}: $list")
                        }

                        enroll(chunkedLists)
                        heartRateList.clear()
                    }
                } else {
                    // 잠금 해제 서버 통신 (심박수 데이터 전송)
                    if (heartRateList.size == 100) {
                        Log.d(TAG, "heartRate list size 100")
                        authenticate()
                    }
                }
                // 파이썬 코드 실행을 위한 Chaquopy 플러그인 추가
                /*  val encryptFunction = client1.callAttr("encrypt")
                        val encryption = encryptFunction.toString()
                        Log.d(TAG, "encryption value : ${encryption}") */
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

        MyApplication.preferences = PreferenceUtil(applicationContext)

        deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

        Log.d(TAG,"deviceInformation 확인 : ${deviceId}")

        // WearOS 전원 상태 관리
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MyApp::MyWakelockTag")

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

    override fun onResume() {
        super.onResume()
        // WakeLock을 얻기
        wakeLock.acquire()
    }

    override fun onPause() {
        super.onPause()
        // WakeLock 해제
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        fun MainScreen() {
        BioVaultWatchTheme {
            navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "HomeScreen"
            ) {
                composable("HomeScreen") {
                    Box(
//                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background),
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
                                navController.navigate("SelectingScreen")
                            },
                            enabled = true,
                            modifier = Modifier
                                .padding(vertical = 85.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "본인 인증"
                            )
                        }

                        Button(
                            onClick = {
                                Log.d(TAG, "설정 버튼 클릭")

                                navController.navigate("SettingScreen")
                            },
                            enabled = true,
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.Gray
                            ),
                        ) {
                            Text(
                                text = "설정"
                            )
                        }
                    }
                }
                composable("SelectingScreen") {
                    Box(
                        //        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                            .padding(10.dp),
                    ) {
                        TimeText()
                        Button(
                            onClick = {
                                Log.d(TAG, "해제 버튼 클릭")
                                MyApplication.status = "authentication"

                                sensorManager =
                                    getSystemService(Context.SENSOR_SERVICE) as SensorManager
                                heartRateSensor =
                                    sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

                                heartRateSensor?.also { sensor ->
                                    sensorManager.registerListener(
                                        heartRateListener,
                                        sensor,
                                        SensorManager.SENSOR_DELAY_NORMAL
                                    )
                                }

                                navController.navigate("LoadingScreen")
                            },
                            enabled = true,
                            modifier = Modifier
                                .padding(vertical = 70.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "잠금 해제"
                            )
                        }

                        Button(
                            onClick = {
                                Log.d(TAG, "등록 버튼 클릭")
                                MyApplication.status = "register"

                                sensorManager =
                                    getSystemService(Context.SENSOR_SERVICE) as SensorManager
                                heartRateSensor =
                                    sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

                                heartRateSensor?.also { sensor ->
                                    sensorManager.registerListener(
                                        heartRateListener,
                                        sensor,
                                        SensorManager.SENSOR_DELAY_NORMAL
                                    )
                                }

                                navController.navigate("LoadingScreen")
                            },
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = MaterialTheme.colors.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .align(Alignment.BottomCenter),
                        ) {
                            Text(
                                text = "등록"
                            )
                        }
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

                composable("SettingScreen"){
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
                            text = stringResource(R.string.setting_information, "Android"),
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 90.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurfaceVariant,
                            text = deviceId
                        )

                        Image (
                            alignment = Alignment.Center,
                            painter = painterResource(id = R.drawable.close),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 150.dp)
                                .pointerInteropFilter {
                                    when (it.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            Log.d(TAG, "Pressed")
                                            navController.navigate("HomeScreen")
                                        }
                                        else -> false
                                    }
                                    true
                                },
                            contentDescription = "setting",
                        )
                    }
                }
            }
        }
    }


    // 심박수 등록 API 연동
    fun enroll(list: List<List<Float>>) {
        var apiClient = ApiClient(this)

        var enrollmentData = EnrollRequestModel(list, deviceId)

        sensorManager.unregisterListener(heartRateListener)

        apiClient.apiService.enroll(enrollmentData)?.enqueue(object : Callback<EnrollResponseModel> {
            override fun onResponse(call: Call<EnrollResponseModel>, response: Response<EnrollResponseModel>) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    var result: EnrollResponseModel? = response.body()
                    Log.d("##", "onResponse 성공: " + result?.toString())
                    Toast.makeText(this@MainActivity, "등록 성공", Toast.LENGTH_SHORT).show()
                    MyApplication.preferences.setString("isFirst", "false")
                    navController.navigate("HomeScreen")
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    var result: EnrollResponseModel? = response.body()
                    Log.d("##", "onResponse 실패")
                    Log.d("##", "onResponse 실패: " + response.code())
                    Log.d("##", "onResponse 실패: " + response.body())
                    val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                    Log.d("##", "Error Response: $errorBody")

                    Toast.makeText(this@MainActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    navController.navigate("HomeScreen")
                }
            }

            override fun onFailure(call: Call<EnrollResponseModel>, t: Throwable) {
                // 통신 실패
                Log.d("##", "onFailure 에러: " + t.message.toString());
            }
        })
    }

    // 잠금 해제 API 연동
    fun authenticate() {
        var apiClient = ApiClient(this)

        var authenticationData = AuthenticationRequestModel(deviceId, heartRateList)

        apiClient.apiService.authenticate(authenticationData)?.enqueue(object : Callback<AuthenticationResponseModel> {
            override fun onResponse(call: Call<AuthenticationResponseModel>, response: Response<AuthenticationResponseModel>) {
                if (response.isSuccessful) {
                    // 정상적으로 통신이 성공된 경우
                    var result: AuthenticationResponseModel? = response.body()
                    Log.d("##", "onResponse 성공: " + result?.toString())
                    Toast.makeText(this@MainActivity, "인증 성공", Toast.LENGTH_SHORT).show()
                    // 심박수 측정 센서 정지
                    sensorManager.unregisterListener(heartRateListener)
                    navController.navigate("CompleteScreen")
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    var result: AuthenticationResponseModel? = response.body()
                    Log.d("##", "onResponse 실패")
                    Log.d("##", "onResponse 실패: " + response.code())
                    Log.d("##", "onResponse 실패: " + response.body())
                    val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                    Log.d("##", "Error Response: $errorBody")

                    Toast.makeText(this@MainActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthenticationResponseModel>, t: Throwable) {
                // 통신 실패
                Log.d("##", "onFailure 에러: " + t.message.toString());
            }
        })
    }
}
