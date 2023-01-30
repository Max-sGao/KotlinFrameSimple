package com.min.frame.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.min.frame.R
import com.min.frame.databinding.ActivitySplashBinding
import com.min.frame.model.HomeViewModel
import com.mine.frame.base.BaseActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, HomeViewModel>() {

    private var job: Job? = null

    override fun initView() {
        job = countDownTimer()
    }

    private fun countDownTimer(total: Int = DOWN_TIME): Job {
        return lifecycleScope.launch {
            flow {
                for (i in total downTo 0) {
                    emit(i)
                    delay(1000)
                }
            }.flowOn(Dispatchers.IO)
                .onStart {
                    mBinding.timeBg.visibility = View.VISIBLE
                    mBinding.tvTime.visibility = View.VISIBLE
                }
                .onCompletion {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }.collect {
                    mBinding.tvTime.text = it.toString()
                }
        }
    }

    override fun getContentLayoutResId(): Int = R.layout.activity_splash

    override fun initViewObservable() {
    }


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    companion object {
        private const val DOWN_TIME: Int = 3;
    }
}