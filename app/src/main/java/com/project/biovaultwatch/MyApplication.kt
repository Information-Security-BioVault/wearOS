package com.project.biovaultwatch

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var preferences: PreferenceUtil

        var progress = 0.01f
        var status = "register"
    }

    override fun onCreate() {
        preferences = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}