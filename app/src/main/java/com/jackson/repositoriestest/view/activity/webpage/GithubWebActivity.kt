package com.jackson.repositoriestest.view.activity.webpage

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import com.jackson.repositoriestest.base.AppConst
import com.jackson.repositoriestest.BaseActivity
import com.jackson.repositoriestest.R
import com.jackson.repositoriestest.base.AbstractPresenter
import com.jackson.repositoriestest.base.BaseView
import com.jackson.repositoriestest.base.CustomWebViewClient
import com.jackson.repositoriestest.view.activity.ui.GithubWebUI
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.toast

class GithubWebActivity: BaseActivity<BaseView, AbstractPresenter<BaseView>>(), View.OnClickListener {

    var webUrl: String = ""
    override var layout: AnkoComponent<Activity> = GithubWebUI()
    override fun onCreatePresenter(): AbstractPresenter<BaseView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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