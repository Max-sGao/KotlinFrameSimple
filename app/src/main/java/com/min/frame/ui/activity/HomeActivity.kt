package com.min.frame.ui.activity

import android.content.Intent
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.min.frame.R
import com.min.frame.databinding.ActivityHomeBinding
import com.min.frame.model.HomeViewModel
import com.min.frame.ui.fragment.HomeFragment
import com.min.frame.ui.fragment.MineFragment
import com.min.frame.ui.fragment.QaFragment
import com.min.frame.ui.fragment.SystemFragment
import com.mine.frame.base.BaseActivity
import java.io.File

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    override fun onDestroy() {
        mBinding.unbind()
        super.onDestroy()
    }

    override fun initView() {
        val f = File("${getExternalFilesDir(null)?.absoluteFile}${File.separator}custom")
        f.mkdirs()
        mBinding.viewPager.isUserInputEnabled = true
        mBinding.viewPager.adapter = HomeFragmentAdapter(supportFragmentManager, lifecycle, listOf<Fragment>(HomeFragment(), QaFragment(), SystemFragment(), MineFragment()))
        mBinding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnv_1 -> {
                    mBinding.viewPager.setCurrentItem(0, false)
                    true
                }
                R.id.bnv_2 -> {
                    mBinding.viewPager.setCurrentItem(1, false)
                    true
                }
                R.id.bnv_3 -> {
                    mBinding.viewPager.setCurrentItem(2, false)
                    true
                }
                R.id.bnv_4 -> {
                    mBinding.viewPager.setCurrentItem(3, false)
                    true
                }
                else -> false
            }
        }
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { it.setType("image/*")
            it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun getContentLayoutResId() = R.layout.activity_home

    override fun initViewObservable() {
        mViewModel?.loginLivaData?.observe(this) {
            //login success
        }
    }


    inner class HomeFragmentAdapter(
        manager: FragmentManager,
        lifecycle: Lifecycle,
        private val fragments: List<Fragment>
    ) : FragmentStateAdapter(manager, lifecycle) {

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment = fragments[position]

    }
}