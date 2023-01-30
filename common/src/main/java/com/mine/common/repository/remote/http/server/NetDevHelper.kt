package com.mine.common.repository.remote.http.server

import com.mine.common.BuildConfig
import com.mine.common.repository.local.sp.SPSource

class NetDevHelper private constructor() {

    object ApiUrls {
        const val RELEASE_BASE = "http://"
        const val DEV_BASE = "http://"
        const val TEST_BASE = "https://www.wanandroid.com/"

    }

    object ImagePrefixUrls {
        const val RELEASE_IMAGE = "http://"
        const val DEV_IMAGE = "http://"
        const val TEST_IMAGE = "http://"
    }

    companion object {

        private const val CURRENT_SERVER = "current_server"

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetDevHelper()
        }

    }

    private var currentServer: TargetServer? = null

    fun getTargetServer(): TargetServer {
        return currentServer ?: readTargetServer().also { currentServer = it }
    }

    fun saveTargetServer(code: Int) = SPSource.instance.saveInt(CURRENT_SERVER, code)

    fun readTargetServer(): TargetServer =
        TargetServer.findBy(SPSource.instance.getInt(CURRENT_SERVER))
            ?: if (BuildConfig.DEBUG) TargetServer.getDefaultServer() else TargetServer.RELEASE
}