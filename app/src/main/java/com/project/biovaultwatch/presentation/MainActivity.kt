/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.project.biovaultwatch.presentation

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.material.snackbar.Snackbar
import com.project.biovaultwatch.R
import com.project.biovaultwatch.presentation.theme.BioVaultWatchTheme
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    val TAG = "##"

    private val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private val fitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_HEART_POINTS, FitnessOptions.ACCESS_READ)
        .build()

    private val activityRequestCode = 100

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private val heartRateListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
                val heartRate = event.values[0]
                // 심박수 데이터를 처리합니다.
                Log.d(TAG, "heartRate : ${heartRate}")
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

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        heartRateSensor?.also { sensor ->
            sensorManager.registerListener(heartRateListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

//        checkPermissionsAndRun(GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)

        // Note: Fitness.SensorsApi.findDataSources() requires the
        // ACCESS_FINE_LOCATION permission.
//        Fitness.getSensorsClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
//            .findDataSources(
//                DataSourcesRequest.Builder()
//                    .setDataTypes(DataType.TYPE_HEART_RATE_BPM)
//                    .setDataSourceTypes(DataSource.TYPE_RAW)
//                    .build())
//            .addOnSuccessListener { dataSources ->
//                dataSources.forEach {
//                    Log.i(TAG, "Data source found: ${it.streamIdentifier}")
//                    Log.i(TAG, "Data Source type: ${it.dataType.name}")
//
//                    if (it.dataType == DataType.TYPE_HEART_RATE_BPM) {
//                        Log.i(TAG, "Data source for TYPE_HEART_RATE_BPM found!")
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e(TAG, "Find data sources request failed", e)
//            }


        setContent {
//                MainScreen()

            WearApp(greetingName = "Android")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(heartRateListener)
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

    private fun checkPermissionsAndRun(fitActionRequestCode: Int) {
        if (permissionApproved()) {
            fitSignIn(fitActionRequestCode)
        } else {
            requestRuntimePermissions(fitActionRequestCode)
        }
    }

    private fun requestPermissions() {
        GoogleSignIn.requestPermissions(
            this, // your activity
            GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
            getGoogleAccount(),
            fitnessOptions)
    }

    private fun fitSignIn(requestCode: Int) {

        if (!GoogleSignIn.hasPermissions(getGoogleAccount(), fitnessOptions)) {
            requestPermissions()
        } else {
            Log.d(TAG, "fitSignIn else")
        }
    }

    private fun getGoogleAccount() = GoogleSignIn.getAccountForExtension(this, fitnessOptions)

    private fun permissionApproved(): Boolean {
        val approved = if (runningQOrLater) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            true
        }
        return approved
    }

    private fun requestRuntimePermissions(requestCode: Int) {
        val shouldProvideRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        requestCode.let {
            if (shouldProvideRationale) {
                Log.i(ContentValues.TAG, "Displaying permission rationale to provide additional context.")
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Permission Denied",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings") {
                        // Request permission
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            requestCode)
                    }
                    .show()
            } else {
                Log.i(ContentValues.TAG, "Requesting permission")
                // Request permission. It's possible this can be auto answered if device policy
                // sets the permission in a given state or the user denied the permission
                // previously and checked "Never ask again".
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    requestCode)
            }
        }
        checkPermissionsAndRun(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> Log.d(TAG, "result OK")
                else -> {
                    // Result wasn't from Google Fit
                    Log.d(TAG, "Result wasn't from Google Fit")
                }
            }
            else -> {
                // Permission not granted
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show()
                requestPermissions()
            }
        }
    }
}
