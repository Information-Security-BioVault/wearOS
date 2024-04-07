package com.project.biovaultwatch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.health.services.client.data.PassiveMonitoringUpdate

class FitnessDataReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check that the Intent is for passive data
        if (intent?.action != PassiveMonitoringUpdate.ACTION_DATA) {
            return
        }
        val update = PassiveMonitoringUpdate.fromIntent(intent) ?: return

        // List of available data points
        val dataPoints = update.dataPoints

        // List of available user state info
        val userActivityInfoList = update.userActivityInfoUpdates
    }
}