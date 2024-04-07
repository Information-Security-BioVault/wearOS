/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.project.biovaultwatch.presentation

import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.concurrent.futures.await
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveMonitoringConfig
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.google.android.gms.fitness.Fitness
import com.project.biovaultwatch.FitnessDataReceiver
import com.project.biovaultwatch.R
import com.project.biovaultwatch.presentation.theme.BioVaultWatchTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    var supportsHeartRate = false

    // 확인할 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.BODY_SENSORS_BACKGROUND,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        requestPermissions(permissionList,0)

        setContent {
//                MainScreen()

            WearApp(greetingName = "Android")
        }

        val healthClient = HealthServices.getClient(this /*context*/)
        val passiveMonitoringClient = healthClient.passiveMonitoringClient
        lifecycleScope.launchWhenCreated {
            val capabilities = passiveMonitoringClient.capabilities.await()
            // Supported types for passive data collection
            supportsHeartRate =
                androidx.health.services.client.data.DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
            // Supported types for PassiveGoals
//            supportsStepsGoal =
//                androidx.health.services.client.data.DataType.STEPS in capabilities.supportedDataTypesEvents

            Log.d("BioVault", "capability : ${capabilities}")
            Log.d("BioVault", "support heart rate : ${supportsHeartRate}")
        }
    }

    fun checkHeartRate() {
        val dataTypes = setOf(DataType.HEART_RATE_BPM)
        val config = PassiveMonitoringConfig.builder()
            .setDataTypes(dataTypes)
            .setComponentName(ComponentName(this, FitnessDataReceiver::class.java))
            // To receive UserActivityState updates, ACTIVITY_RECOGNITION permission is required.
            .setShouldIncludeUserActivityState(true)
            .build()
        lifecycleScope.launch {
            HealthServices.getClient(this@MainActivity)
                .passiveMonitoringClient
                .registerDataCallback(config)
                .await()
        }
        Log.d("BioVault", "heart rate : ${dataTypes} / ${config}")
    }

    @Composable
    fun WearApp(greetingName: String) {
        BioVaultWatchTheme {
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
                        Log.d("BioVault", "버튼 클릭")
                        checkHeartRate()
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
            }
        }
    }

//    @Composable
//    fun MainScreen() {
//        val context = LocalContext.current
//        var hasPermission by remember { mutableStateOf(false, false) }
//
//        val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                // 권한이 허용된 경우에 실행할 작업을 여기에 추가
//                hasPermission = true
////                WearApp(greetingName = "Android")
//                Log.d("BioVault", "권한 설정 완료")
//            } else {
//                // 권한이 거부된 경우에 대한 처리를 여기에 추가
//                hasPermission = false
//                // 사용자에게 권한이 필요하다는 설명을 제공할 수 있습니다.
//                Log.d("BioVault", "권한 설정 필요")
//            }
//        }
//
//        // 앱이 시작될 때 권한을 확인하고, 없는 경우 권한을 요청합니다.
//        LaunchedEffect(Unit) {
//            val permissionStatus = context.checkSelfPermission(Manifest.permission.BODY_SENSORS_BACKGROUND)
////            val permissionStatus2 = context.checkSelfPermission(Manifest.permission.BODY_SENSORS_BACKGROUND)
//            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
//                hasPermission = true
//                Log.d("BioVault", "권한 설정 완료")
//            } else {
//                requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
////                requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS_BACKGROUND)
//            }
//        }
//
//        Scaffold(
//            content = {
//                if (hasPermission) {
//                    // 권한이 허용된 경우에 원하는 화면을 표시합니다.
//                    Log.d("BioVault", "권한 설정 완료")
//                    WearApp("Android")
//                } else {
//                    // 권한이 거부된 경우에 대체 화면을 표시할 수 있습니다.
//                    Text(
//                        text = "앱을 사용하려면 심박수 권한이 필요합니다.",
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp)
////                            .align(Alignment.Center)
//                    )
//                }
//            }
//        )
//    }

    @Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
    @Composable
    fun DefaultPreview() {
        WearApp("Preview Android")
    }
}
