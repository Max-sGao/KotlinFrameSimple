package com.mine.frame.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.BarUtils
import com.mine.common.R
import com.mine.common.vm.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var mBinding: T

    protected var mViewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        getBundleParams()
        initViewDataBinding()
        initView()
        initViewObservable()
        initDefaultObservable()
    }

    internal fun initStatusBar() {
        BarUtils.setStatusBarColor(this, resources.getColor(android.R.color.transparent))
        BarUtils.setStatusBarLightMode(this, true)
    }

    fun getBundleParams() {

    }

    private fun initViewDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, getContentLayoutResId())
        mBinding.setLifecycleOwner(this)
        mViewModel = initViewModel()
        if (mViewModel == null) {
            val type = this::class.java.genericSuperclass
            val cls = if (type is ParameterizedType)
                type.actualTypeArguments[1] as Class<BaseViewModel>
            else
                BaseViewModel::class.java
            mViewModel = createViewModel(this, cls) as VM
        }

    }

    abstract fun initView()

    abstract fun getContentLayoutResId(): Int

    protected fun initViewModel(): VM? {
        return null
    }

    abstract fun initViewObservable()

    fun showLoadingDialog() {
    }

    fun dismissLoadingDialog() {
    }

    protected fun <V : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<V>) =
        ViewModelProvider(activity).get(cls)


    private fun initDefaultObservable() {
        mViewModel?.loading?.observe(
            this
        ) { showLoadingDialog() }
        mViewModel?.dismissLoading?.observe(this) { dismissLoadingDialog() }
        mViewModel?.tokenExpire?.observe(this) {
            //TODO User Login need try once
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.unbind()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}