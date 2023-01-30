package com.min.frame.model

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mine.common.repository.bean.request.LoginRequest
import com.mine.common.repository.bean.server.LoginResBean
import com.mine.common.repository.remote.http.lunchNetSource
import com.mine.common.repository.remote.http.net.ApiProxy
import com.mine.common.vm.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val loginLivaData = MutableLiveData<LoginResBean>()

    fun login(userName: String, passWord: String) {
        viewModelScope.lunchNetSource(
            before = {loading.execute()},
            async = { ApiProxy.instance.apiService.login(LoginRequest(userName, passWord)) },
            success = {
                loginLivaData.postValue(it)
            },
            fail = { code, msg -> },
            complete = {dismissLoading.execute()})
    }

}