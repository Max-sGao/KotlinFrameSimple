package com.min.frame.ui.fragment

import com.min.frame.R
import com.min.frame.databinding.FragmentHomeBinding
import com.min.frame.model.QaViewModel
import com.mine.common.ui.BaseFragment

class QaFragment: BaseFragment<FragmentHomeBinding, QaViewModel>() {

    var init: Boolean = false

    override fun initView() {
    }

    override fun initViewObservable() {
    }

    override fun getContentLayoutResId(): Int = R.layout.fragment_qa

}