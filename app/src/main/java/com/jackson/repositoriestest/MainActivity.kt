package com.jackson.repositoriestest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import com.jackson.repositoriestest.base.AppConst
import com.jackson.repositoriestest.base.ListMoreScrollListener
import com.jackson.repositoriestest.extensions.safeViewLock
import com.jackson.repositoriestest.model.RepositoriesData
import com.jackson.repositoriestest.model.RepositoriesResponse
import com.jackson.repositoriestest.presenter.MainConstract
import com.jackson.repositoriestest.presenter.MainPresenter
import com.jackson.repositoriestest.utils.CommonUtils
import com.jackson.repositoriestest.utils.DLog
import com.jackson.repositoriestest.utils.DeviceUtils
import com.jackson.repositoriestest.view.adapter.RepositoriesAdapter
import com.jackson.repositoriestest.view.component.DefaultDividerItemDecoration
import com.jackson.repositoriestest.view.activity.ui.MainUI
import com.jackson.repositoriestest.view.activity.webpage.GithubWebActivity
import com.jackson.repositoriestest.viewModel.GithubViewModel
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity: BaseActivity<MainConstract.View, MainPresenter>(), MainConstract.View, View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    companion object {
        val TAG: String = javaClass.simpleName
    }

    private var query: String = ""
    private var page: Int = AppConst.PAGE_FIRST_VALUE
    private var rows: Int = AppConst.PER_PAGE_MAX_VALUE
    lateinit var mRepoAdapter: RepositoriesAdapter

    override var layout: AnkoComponent<Activity> = MainUI()
    override fun onCreatePresenter(): MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.stay, R.anim.stay)
        with(layout as MainUI) {
            RepositoriesAdapter(this@MainActivity).let { adapter ->
                mRepoAdapter = adapter

                hashMapOf<String, Int>().apply {
                    put(DefaultDividerItemDecoration.LEFT, dip(15))
                    put(DefaultDividerItemDecoration.RIGHT, dip(15))
                }.let { dividerPaddings ->
                    rv.addItemDecoration(DefaultDividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL, dividerPaddings, R.drawable.divider))
                }
                rv.adapter = adapter
            }

            mSearchEt.setOnEditorActionListener(this@MainActivity)
            mSearchBtn.setOnClickListener(this@MainActivity)

            /* init Adapter View, Model*/
            presenter?.run {
                adapterView = mRepoAdapter
                adapterModel = mRepoAdapter
            }

            gitViewmodel.searchRepositories()

        }
    }

    val gitViewmodel: GithubViewModel = getViewModel()

    /**
     * 검색버튼 클릭 or 엔터
     */
    private fun searchRepositories(page: Int = AppConst.PAGE_FIRST_VALUE, rows: Int = AppConst.PER_PAGE_DEFAULT) {
        with(layout as MainUI) {
            mSearchEt.text.toString().let { keyword ->

                CommonUtils.makeQueryParams(keyword).let { query ->
                    if (CommonUtils.checkQueryLength(query)) {

                        // 검색 버튼 비활성화 ( 중복클릭 방지 )
                        mSearchBtn.safeViewLock(true)
                        this@MainActivity.query = query
                        this@MainActivity.page = page
                        this@MainActivity.rows = rows

                        presenter?.searchRepositories(query, page, rows)

                    } else toast(R.string.empty_search_keyword)
                }
            }
        }
    }
    override fun clickSearchRepositories() = searchRepositories()
    override fun moreSearchRepositories(nextPage: Int) = searchRepositories(page + 1)

    /**
     * View ) Repositories API 응답부
     */
    override fun responseRepositoriesData(data: RepositoriesResponse, curPage: Int) {
        with(layout as MainUI) {
            // 검색 버튼 활성화
            mSearchBtn.safeViewLock(false)
            DLog.e(TAG, "response repositories [$query]")
            if (curPage == AppConst.PAGE_FIRST_VALUE) {
                initScrollListener(data.totalCount)
                DeviceUtils.hideKeyboard(this@MainActivity, mSearchEt)
            }
        }
    }

    /**
     * 저장소 리스트, 아이템 클릭 리스너
     * 웹뷰에 저장소 링크를 호출
     */
    override fun openRepositories(data: RepositoriesData) {
        // toast(data.htmlUrl)
        Handler().postDelayed({
            Intent(this@MainActivity, GithubWebActivity::class.java).apply {
                putExtra(AppConst.GITHUB_URL, data.htmlUrl)
            }.let {  intent ->
                startActivity(intent)
            }
        }, 300)
    }

    private fun initScrollListener(allCount: Int): Unit = with((layout as MainUI).rv) {
        clearOnScrollListeners()
        if (allCount >= AppConst.PER_PAGE_DEFAULT) {
            addOnScrollListener(object : ListMoreScrollListener(allCount) {
                override fun onLoadData(page: Int, count: Int) {
                    moreSearchRepositories(count + 1)
                }

                override fun onCompletedLoading() {
                    removeOnScrollListener(this)
                }
            })
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.repositories_search_btn -> clickSearchRepositories()  // search btn
                else -> { }
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        when (actionId) {
            EditorInfo.IME_ACTION_SEARCH -> clickSearchRepositories()
            else -> return false // 기본 엔터키 동작
        }
        return true
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun afterTextChanged(p0: Editable?) {
        page = AppConst.PAGE_FIRST_VALUE
        rows = AppConst.PER_PAGE_DEFAULT
        mRepoAdapter.clear()
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        backPressHandler.onBackPressed()
    }

}