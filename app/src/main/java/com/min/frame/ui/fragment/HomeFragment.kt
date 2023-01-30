package com.min.frame.ui.fragment

import com.min.frame.R
import com.min.frame.databinding.FragmentHomeBinding
import com.min.frame.model.HomePageViewModel
import com.mine.common.ui.BaseFragment

class HomeFragment: BaseFragment<FragmentHomeBinding, HomePageViewModel>() {

    var init: Boolean = false

    override fun initView() {
    }

    override fun initViewObservable() {
    }

    override fun getContentLayoutResId(): Int = R.layout.fragment_home

}