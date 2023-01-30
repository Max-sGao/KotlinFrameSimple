package com.min.frame.ui.fragment

import com.min.frame.R
import com.min.frame.databinding.FragmentHomeBinding
import com.min.frame.model.SystemViewModel
import com.mine.common.ui.BaseFragment

class SystemFragment: BaseFragment<FragmentHomeBinding, SystemViewModel>() {

    var init: Boolean = false

    override fun initView() {
    }

    override fun initViewObservable() {
    }

    override fun getContentLayoutResId(): Int = R.layout.fragment_system

}