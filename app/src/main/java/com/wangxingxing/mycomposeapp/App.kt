package com.wangxingxing.mycomposeapp

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        const val TAG = "wxx"

        lateinit var instance: App
            private set

        fun isDebug() = BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        Utils.init(instance)
        initLog()
    }

    private fun initLog() {
        Utils.init(instance)
        LogUtils.getConfig()
            .setLogSwitch(isDebug())
            .setGlobalTag(TAG)
            .setBorderSwitch(true)
    }
}

