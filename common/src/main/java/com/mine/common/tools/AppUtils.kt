package com.mine.common.tools

import android.app.Application

class AppUtils {
    companion object {

        private var application:Application? = null

        fun init(app: Application){
            application = if (application == null) app else application
        }

        fun getApplication(): Application = application!!
    }
}