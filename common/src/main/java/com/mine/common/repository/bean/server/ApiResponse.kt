package com.mine.common.repository.bean.server

open class ApiResponse<T> : BaseResponse() {
    var data: T? = null

    class ApiFailedResponse<T> : ApiResponse<T>()

    class ApiSuccessResponse<T> : ApiResponse<T>()

    data class ApiErrorResponse<T>(val e:Throwable?): ApiResponse<T>()
}