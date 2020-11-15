package com.jackson.repositoriestest.view.activity.ui

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.jackson.repositoriestest.R
import org.jetbrains.anko.*

class GithubWebUI: AnkoComponent<Activity> {

    lateinit var mBackBtn: LinearLayout
    lateinit var mCloseBtn: LinearLayout
    lateinit var mWebView: WebView

    override fun createView(ui: AnkoContext<Activity>) = with(ui) {
        verticalLayout {

            relativeLayout {
                backgroundColor = Color.WHITE

                /**
                 * 뒤로가기
                 */
                mBackBtn = verticalLayout {
                    id = R.id.webview_back_btn
                    gravity = Gravity.CENTER
                    imageView(R.drawable.ic_go_back) {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }.lparams(width= dip(24), height= dip(24))
                }.lparams(width= dip(55), height= dip(55)) {
                    alignParentTop()
                    alignParentLeft()
                }

                /**
                 * 닫기
                 */
                mCloseBtn = verticalLayout {
                    id = R.id.webview_close_btn
                    gravity = Gravity.CENTER
                    imageView(R.drawable.ic_close_browser) {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }.lparams(width= dip(24), height= dip(24))
                }.lparams(width= dip(55), height= dip(55)) {
                    alignParentTop()
                    alignParentRight()
                }

            }.lparams(width= matchParent, height= dip(55))

            linearLayout {
                backgroundColor = ContextCompat.getColor(ctx, R.color.listDividerColor)
            }.lparams(width= matchParent, height= dip(1))

            mWebView = webView {
                backgroundColor = Color.WHITE
            }.lparams(width= matchParent, height= 0, weight= 1f)

        }
    }
}