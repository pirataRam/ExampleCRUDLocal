package com.example.examplecrudlocal.app

import android.content.res.Configuration
import android.content.res.Resources
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExampleApp : MultiDexApplication() {

    companion object {
        lateinit var instance: ExampleApp
            private set

        fun isNightMode(resources: Resources) = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> true
        }
    }

    init {
        instance = this@ExampleApp
    }

    override fun onCreate() {
        super.onCreate()
    }
}