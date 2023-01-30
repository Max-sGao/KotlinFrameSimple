package com.mine.common.repository.remote.http.net

import com.mine.common.repository.remote.BaseNetProxy
import com.mine.common.repository.bean.request.LoginRequest
import com.mine.common.repository.bean.server.ResultBuilder
import com.mine.common.repository.bean.server.LoginResBean
import com.mine.common.repository.remote.HttpManager

class ApiProxy private constructor() : BaseNetProxy() {

    var apiService: ApiService = HttpManager.getInstance().create(ApiService::class.java)

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiProxy()
        }
    }

    suspend fun login(body: LoginRequest, result: (ResultBuilder<LoginResBean>.() -> Unit)?) {
        fetchSource({apiService.login(body)}, result)
    }
}