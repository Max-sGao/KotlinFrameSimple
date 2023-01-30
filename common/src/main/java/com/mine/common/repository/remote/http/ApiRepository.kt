package com.mine.common.repository.remote.http

import com.mine.common.BuildConfig
import com.mine.common.repository.bean.server.ApiResponse

open class ApiRepository<T> {

    var before: () -> Unit = { log("before") }

    lateinit var async: suspend () -> ApiResponse<out T?>

    var success: (data: T?) -> Unit = { data -> log("success") }

    var fail: (code: String, msg: String?) -> Unit =
        { code, msg -> log("fail -> code = ${code}, msg = $msg") }

    var complete: () -> Unit = { log("complete") }

    private fun log(msg: String) {
        if (BuildConfig.DEBUG) {
            android.util.Log.e(this.javaClass.simpleName, msg)
        }
    }
}