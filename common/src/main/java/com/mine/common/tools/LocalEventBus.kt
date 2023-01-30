package com.mine.common.tools

import kotlinx.coroutines.flow.MutableSharedFlow

object LocalEventBus {

    private val sharedFlow = MutableSharedFlow<Event>()

    suspend fun post(value: Event) {
        sharedFlow.emit(value)
    }

    suspend fun observe(block: (Event) -> Unit) {
        sharedFlow.collect {
            block(it)
        }
    }

}


data class Event(val what: Int) {
    var data: Any? = null
}
