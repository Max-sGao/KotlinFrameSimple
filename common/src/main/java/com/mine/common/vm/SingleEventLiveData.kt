package com.mine.common.vm

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleEventLiveData<T> : MutableLiveData<T>() {

    val pending: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { if (pending.compareAndSet(true, false)) observer.onChanged(it) }
    }

    override fun postValue(value: T?) {
        pending.set(true)
        super.postValue(value)
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun execute() {
        value = null
    }

    @MainThread
    fun execute(v: T?) {
        value = v
    }
}