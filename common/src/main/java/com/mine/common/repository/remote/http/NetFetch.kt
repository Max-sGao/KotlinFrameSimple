package com.mine.common.repository.remote.http

import com.blankj.utilcode.util.ToastUtils
import com.mine.common.repository.bean.IBean
import com.mine.common.repository.bean.server.ApiResponse
import com.mine.common.tools.NetWorkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun CoroutineScope.fetchNetData(requestBlock: suspend () -> Unit) {
    this.launch {
        requestBlock()
    }
}


fun CoroutineScope.netLunch(block: ApiRepository<IBean>.() -> Unit) {
    this.launch {
        val repository = ApiRepository<IBean>()
        repository.block()
        repository.before()

        if (!NetWorkUtils.isConnect()) {
            repository.fail("-1", "网络连接失败，请稍后重试")
            repository.complete()
            return@launch
        }
        try {
            val result = repository.async()
            when (result.errorCode) {
                "0", "200" -> repository.success(result.data)
                else -> repository.fail(result.errorCode, result.errorMsg)
            }
            repository.complete()
        } catch (e: Exception) {
            repository.fail("-1", "服务器连接失败")
            repository.complete()
        }
    }
}

fun <T> CoroutineScope.lunchNetSource(
    before: () -> Unit = {},
    async: suspend () -> ApiResponse<T>,
    success: (T?) -> Unit,
    fail: (code: String, msg: String?) -> Unit = { code, msg -> },
    complete: () -> Unit = {}
) {
    this.launch {
        before()
        if (!NetWorkUtils.isConnect()) {
            fail("-1", "网络连接失败，请稍后重试")
            complete()
            return@launch
        }
        val result: ApiResponse<T>
        try {
            result = async()
            when (result.errorCode) {
                "0", "200" -> success(result.data)
                else -> fail(result.errorCode, result.errorMsg)
            }
            complete()
        } catch (e: Throwable) {
            ToastUtils.showShort("服务器连接失败")
            fail("-1", "服务器连接失败")
            complete()
        }
    }
}
