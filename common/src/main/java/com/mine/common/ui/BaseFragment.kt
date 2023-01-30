package com.mine.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mine.common.vm.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<K : ViewDataBinding, VM : ViewModel> : Fragment() {

    lateinit var mBinding: K
    var mViewModel: VM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<K>(
            inflater, getContentLayoutResId(), container, false
        ).apply { mBinding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDataBinding()
        initView();
    }

    abstract fun initView()

    abstract fun initViewObservable()

    private fun initViewDataBinding() {
        mBinding.setLifecycleOwner(this)
        mViewModel = initViewModel()
        if (mViewModel == null) {
            val type = this::class.java.genericSuperclass
            val cls =
                if (type is ParameterizedType) type.actualTypeArguments[1] as Class<BaseViewModel>
                else BaseViewModel::class.java
            mViewModel = createViewModel(cls) as VM
        }

    }

    protected fun initViewModel(): VM? = null

    protected fun <V : ViewModel> createViewModel(cls: Class<V>) = ViewModelProvider(this).get(cls)

    abstract fun getContentLayoutResId(): Int

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }

}