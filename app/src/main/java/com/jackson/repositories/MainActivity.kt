package com.jackson.repositories

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.jackson.repositories.presenter.MainConstract
import com.jackson.repositories.presenter.MainPresenter
import com.jackson.repositories.utils.CommonUtils
import com.jackson.repositories.utils.DLog
import com.jackson.repositories.utils.DeviceUtils
import com.jackson.repositories.base.safeViewLock
import com.jackson.repositories.model.RepositoriesResponse
import com.jackson.repositories.view.ui.MainUI
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.toast

class MainActivity: BaseActivity<MainConstract.View, MainPresenter>(), MainConstract.View, View.OnClickListener, TextView.OnEditorActionListener {

    companion object {
        val TAG: String = javaClass.simpleName
    }

    var query: String = ""
    var page: Int = AppConst.PAGE_FIRST_VALUE
    var rows: Int = AppConst.PER_PAGE_MAX_VALUE

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
     * View ) 검색버튼 클릭 or 엔터
     */
    override fun clickSearchIcon() {
        with(layout as MainUI) {
            mSearchEt.text.toString().let { inputStr ->
                if (CommonUtils.checkQueryLength(inputStr)) {
                    mSearchBtn.safeViewLock(true)
                    query = CommonUtils.makeQueryParams(inputStr)
                    page = AppConst.PAGE_FIRST_VALUE
                    rows = AppConst.PER_PAGE_DEFAULT

                    mSearchBtn.safeViewLock(false)
                    presenter?.searchRepositories(query, page, rows)
                } else {
                    toast(R.string.empty_search_keyword)
                }
            }
        }
    }

    /**
     * View ) Repositories API 응답부
     */
    override fun initRepositoriesAdapter(data: RepositoriesResponse) {
        with(layout as MainUI) {
            // recyclerView adapter setData 호출
            DLog.e(TAG, "response repositories [$query]")
            DeviceUtils.hideKeyboard(this@MainActivity, mSearchEt)
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
            else -> return false
        }
        return true
    }

}