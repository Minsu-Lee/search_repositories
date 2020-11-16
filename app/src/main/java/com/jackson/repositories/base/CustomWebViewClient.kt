package com.jackson.repositories.base

import android.graphics.Bitmap
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient(private val onTimeout: ((Int) -> Unit)? = null) : WebViewClient() {

    companion object {
        const val PAGE_TIME_OUT = 0
        const val PAGE_LOAD = 1
        const val PAGE_LOAD_COMPLETE = 2
        const val READ_TIMEOUT: Long = 15
    }

    private var mTimeOut: Boolean = false

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        mTimeOut = true

        onTimeout?.invoke(PAGE_LOAD)

        Handler().postDelayed({
            if (mTimeOut) {
                //onTimeout?.invoke(mTimeOut)
                onTimeout?.invoke(PAGE_TIME_OUT)
            }
        }, READ_TIMEOUT * 1000)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        mTimeOut = false
        onTimeout?.invoke(PAGE_LOAD_COMPLETE)
    }

}