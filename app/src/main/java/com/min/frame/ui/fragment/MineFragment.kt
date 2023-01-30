package com.min.frame.ui.fragment

import com.min.frame.R
import com.min.frame.databinding.FragmentHomeBinding
import com.min.frame.model.MineViewModel
import com.mine.common.ui.BaseFragment

class MineFragment: BaseFragment<FragmentHomeBinding, MineViewModel>() {

    var init: Boolean = false

    override fun initView() {
    }

    override fun initViewObservable() {
    }

    override fun getContentLayoutResId(): Int = R.layout.fragment_mine

}