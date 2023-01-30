package com.mine.common

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.mine.common.tools.AppUtils

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        AppUtils.init(this)
    }
}