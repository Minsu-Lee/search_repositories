package com.jackson.repositories.base

import android.os.Parcelable
import androidx.annotation.StringRes

interface BaseView {

    fun onBinding(data: Parcelable) { }

    fun onVisibleProgress() { }

    fun onInvisibleProgress() { }

    fun toast(msgStr: String, isLong: Boolean = false) {}
    fun toast(@StringRes msgResId: Int, isLong: Boolean = false) {}

}