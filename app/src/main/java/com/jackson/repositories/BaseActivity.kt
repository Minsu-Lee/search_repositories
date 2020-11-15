package com.jackson.repositories

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jackson.repositories.base.BasePresenter
import com.jackson.repositories.base.BaseView
import com.jackson.repositories.view.component.NetworkProgressDialog
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

abstract class BaseActivity<in VIEW: BaseView, P: BasePresenter<VIEW>>: AppCompatActivity(), BaseView {

    private val DEFAULT_PROGRESS_TIME: Long = 1000L

    private var mProgressView: AlertDialog? = null

    abstract var layout: AnkoComponent<Activity>

    protected var presenter: P? = null

    abstract fun onCreatePresenter(): P?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = onCreatePresenter()
        presenter?.attachView(this as VIEW)
        mProgressView = NetworkProgressDialog.getInstance(this@BaseActivity)
        with(layout) {
            setContentView(createView(AnkoContext.create(this@BaseActivity, this@BaseActivity)))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun onVisibleProgress() {
        mProgressView?.show()
    }

    override fun onInvisibleProgress() {
        mProgressView?.let {
            Handler().postDelayed({
                it.dismiss()
            }, DEFAULT_PROGRESS_TIME)
        }
    }

}