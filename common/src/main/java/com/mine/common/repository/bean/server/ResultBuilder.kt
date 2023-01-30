package com.mine.common.repository.bean.server

class ResultBuilder<T> {

    var onStart: () -> Unit = {}

    var onFailed: (code: String?, msg: String?) -> Unit = { code, msg -> }

    var onSuccess: (data: T?) -> Unit = {}

    var onComplete: () -> Unit = {}

    var onError: (e: Throwable?) -> Unit = {}
}