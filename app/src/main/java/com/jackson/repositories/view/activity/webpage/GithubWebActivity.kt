package com.jackson.repositories.view.activity.webpage

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import com.jackson.repositories.base.AppConst
import com.jackson.repositories.BaseActivity
import com.jackson.repositories.R
import com.jackson.repositories.base.AbstractPresenter
import com.jackson.repositories.base.BaseView
import com.jackson.repositories.base.CustomWebViewClient
import com.jackson.repositories.view.activity.ui.GithubWebUI
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.toast

class GithubWebActivity: BaseActivity<BaseView, AbstractPresenter<BaseView>>(), View.OnClickListener {

    var webUrl: String = ""
    override var layout: AnkoComponent<Activity> = GithubWebUI()
    override fun onCreatePresenter(): AbstractPresenter<BaseView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.act_slide_right_in, R.anim.stay)
        webUrl = intent.getStringExtra(AppConst.GITHUB_URL)
        with(layout as GithubWebUI) {

            mWebView.apply {

                webViewClient = CustomWebViewClient {
                    when(it) {
                        CustomWebViewClient.PAGE_TIME_OUT -> toast(R.string.check_network_msg)
                        else -> { }
                    }
                }

                webChromeClient = WebChromeClient()

                settings.run {
                    // 자바스크립트 사용 허용
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    allowUniversalAccessFromFileURLs = true
                    domStorageEnabled = true
                }

            }.let { it.loadUrl(webUrl) }

            mBackBtn.setOnClickListener(this@GithubWebActivity)
            mCloseBtn.setOnClickListener(this@GithubWebActivity)

        }
    }

    override fun onClick(view: View?) {
        with((layout as GithubWebUI).mWebView) {
            view?.let {
                when (it.id) {
                    R.id.webview_back_btn -> onBackPressed()
                    R.id.webview_close_btn -> {
                        clearHistory()
                        onBackPressed()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        with((layout as GithubWebUI).mWebView) {
            if (canGoBack()) goBack()
            else {
                super.onBackPressed()
                overridePendingTransition(R.anim.stay, R.anim.act_slide_right_out)
            }
        }
    }
}