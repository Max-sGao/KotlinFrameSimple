package com.mine.common.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mine.common.constant.Constants
import com.mine.common.tools.LocalEventBus
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    init {
        viewModelScope.launch {
            LocalEventBus.observe {
                when (it.what) {
                    Constants.TOKEN_EXPIRE -> {
                        tokenExpire.execute()
                    }
                }
            }
        }
    }

    val loading: SingleEventLiveData<Any> = SingleEventLiveData()

    val dismissLoading: SingleEventLiveData<Any> = SingleEventLiveData()

    val toast:SingleEventLiveData<String> = SingleEventLiveData()

    val tokenExpire: SingleEventLiveData<Any> = SingleEventLiveData()
}