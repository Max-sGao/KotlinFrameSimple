package com.mine.common.repository.local.db.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class ILocalSource {
    suspend inline fun <T> runIO(crossinline block: () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }
}