package com.wangxingxing.mycomposeapp

import android.app.Application
import com.blankj.utilcode.util.Utils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
    }
}

