package com.mine.common.repository.remote

import com.mine.common.repository.bean.server.ApiResponse
import com.mine.common.repository.bean.server.ResultBuilder

open class BaseNetProxy {

    private suspend fun <T> runHttp(
        block: suspend () -> ApiResponse<T>
    ): ApiResponse<T> {
        runCatching { block() }.onSuccess { return parseData(it) }
            .onFailure { return parseException(it) }
        return parseException(null)
    }

    protected suspend fun <T> fetchSource(
        block: suspend () -> ApiResponse<T?>, resultBlock: (ResultBuilder<T>.() -> Unit)?
    ) {
        val resBuilder = ResultBuilder<T>()
        if (resultBlock != null) {
            resBuilder.resultBlock()
        }
        resBuilder.onStart()
        val resp = runHttp { block() }
        when (resp) {
            is ApiResponse.ApiSuccessResponse -> resBuilder.onSuccess(resp.data)
            is ApiResponse.ApiFailedResponse -> resBuilder.onFailed(resp.errorCode, resp.errorMsg)
            is ApiResponse.ApiErrorResponse -> resBuilder.onError(resp.e)
        }
        resBuilder.onComplete()

    }

    private fun <T> parseData(resp: ApiResponse<T>): ApiResponse<T> {
        return when (resp.errorCode) {
            "0", "200" -> ApiResponse.ApiSuccessResponse<T>().also {
                it.errorCode = resp.errorCode
                it.data = resp.data
                it.errorMsg = resp.errorMsg
            }
            else -> ApiResponse.ApiFailedResponse<T>().also {
                it.errorCode = resp.errorCode
                it.data = resp.data
                it.errorMsg = resp.errorMsg
            }
        }
    }

    private fun <T> parseException(error: Throwable?): ApiResponse<T> {
        return ApiResponse.ApiErrorResponse<T>(error)
    }
}