package com.mine.common.repository.local.sp

import android.content.Context
import android.content.SharedPreferences
import com.mine.common.tools.AppUtils

class SPSource private constructor() {

    private var sp: SharedPreferences

    init {
        sp = AppUtils.getApplication().getSharedPreferences("", Context.MODE_PRIVATE)
    }

    companion object {

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SPSource()
        }

    }

    fun saveString(key: String, content: String, isCommit: Boolean = false) {
        if (isCommit)
            sp.edit().putString(key, content).commit()
        else
            sp.edit().putString(key, content).apply()
    }

    fun saveInt(key: String, content: Int, isCommit: Boolean = false) {
        if (isCommit)
            sp.edit().putInt(key, content).commit()
        else
            sp.edit().putInt(key, content).apply()
    }

    fun getString(key: String, default: String): String? = sp.getString(key, default)

    fun getString(key: String): String? = getString(key, "")

    fun getInt(key: String, default: Int): Int = sp.getInt(key, default)

    fun getInt(key: String): Int = getInt(key, -1)
}