package com.jackson.repositoriestest

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.jackson.repositoriestest.base.safeViewLock
import com.jackson.repositoriestest.presenter.MainConstract
import com.jackson.repositoriestest.presenter.MainPresenter
import com.jackson.repositoriestest.utils.CommonUtils
import com.jackson.repositoriestest.utils.DLog
import com.jackson.repositoriestest.utils.DeviceUtils
import com.jackson.repositoriestest.view.main.ui.MainUI
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.toast

class MainActivity : BaseActivity<MainConstract.View, MainPresenter>(), MainConstract.View, View.OnClickListener, TextView.OnEditorActionListener {

    companion object {
        val TAG: String = javaClass.simpleName
    }

    var page: Int = AppConst.PAGE_FIRST_VALUE
    var rows: Int = AppConst.PER_PAGE_MAX_VALUE
    var query: String = ""

    override var layout: AnkoComponent<Activity> = MainUI()

    override fun onCreatePresenter(): MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(layout as MainUI) {
            mSearchEt.setOnEditorActionListener(this@MainActivity)
            mSearchBtn.setOnClickListener(this@MainActivity)
        }
    }

    /**
     * 검색버튼 클릭 or 엔터
     */
    override fun clickSearchIcon() {
        with(layout as MainUI) {
            mSearchEt.text.toString().let { inputStr ->
                if (inputStr.isNotEmpty()) {
                    mSearchBtn.safeViewLock(true)
                    query = CommonUtils.makeQueryParams(inputStr)
                    page = AppConst.PAGE_FIRST_VALUE
                    rows = AppConst.PER_PAGE_MAX_VALUE
                    presenter?.searchRepositories(query, page, rows)
                } else {
                    toast(R.string.empty_search_keyword)
                }
            }
        }
    }

    /**
     * Repositories API 응답부
     */
    override fun initRepositoriesAdapter(data: ArrayList<Any>?) {
        with(layout as MainUI) {
            // recyclerView adapter setData 호출
            DLog.e(TAG, "response repositories data: $query")
            DeviceUtils.hideKeyboard(this@MainActivity, mSearchEt)
            mSearchBtn.safeViewLock(false)

            mSearchBtn.postDelayed({
                onInvisibleProgress()
            }, 1000)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.repositories_search_btn -> clickSearchIcon()  // search btn
                else -> { }
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> clickSearchIcon()
            else -> return false // 기본 엔터키 동작
        }
        return true
    }

}