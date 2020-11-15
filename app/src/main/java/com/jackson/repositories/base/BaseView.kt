package com.jackson.repositories.base

import android.os.Parcelable

interface BaseView {

    fun onBinding(data: Parcelable) { }

    fun onVisibleProgress() { }

    fun onInvisibleProgress() { }

}