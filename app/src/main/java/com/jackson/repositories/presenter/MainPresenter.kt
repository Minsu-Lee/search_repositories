package com.jackson.repositories.presenter

import com.jackson.repositories.ParamsInfo
import com.jackson.repositories.R
import com.jackson.repositories.base.AbstractPresenter
import com.jackson.repositories.base.AppConst
import com.jackson.repositories.http.NetworkManager
import com.jackson.repositories.model.RepositoriesResponse
import com.jackson.repositories.utils.DLog
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainPresenter: AbstractPresenter<MainConstract.View>(), MainConstract.Presenter {

    override var adapterView: RepositoriesAdapterConstract.View? = null
    override var adapterModel: RepositoriesAdapterConstract.Model? = null

    override fun searchRepositories(query: String, page: Int, row: Int) {
        if (page == AppConst.PAGE_FIRST_VALUE) view?.onVisibleProgress()
        NetworkManager.defaultParams().apply {
            put(ParamsInfo.KEY_SEARCH_QUERY, query)
            put(ParamsInfo.KEY_SEARCH_PAGE, "$page")
            put(ParamsInfo.KEY_SEARCH_PER_PAGE, "$row")
        }.let { params ->
//            addDisposable(
//                NetworkManager.service.searchRepositories(params)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe({
//                        if (page == AppConst.PAGE_FIRST_VALUE) initRepositoriesAdapter(it, page)
//                        else moreRepositoriesAdapter(it, page)
//                        view?.onInvisibleProgress()
//                    }, {
//                        it.message?.indexOf("HTTP 422 Unprocessable Entity")
//                        handleError(it)
//                    })
//            )

            NetworkManager.service.searchRepositories(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object: Observer<RepositoriesResponse> {
                    override fun onComplete() { }
                    override fun onSubscribe(d: Disposable) = addDisposable(d)
                    override fun onNext(response: RepositoriesResponse) {
                        DLog.e("Test", "onNext")
                        if (page == AppConst.PAGE_FIRST_VALUE) initRepositoriesAdapter(response, page)
                        else moreRepositoriesAdapter(response, page)
                        view?.onInvisibleProgress()
                    }
                    override fun onError(e: Throwable) {
                        (e as? HttpException)?.response()?.let {
                            when (it.code()) {
                                // 권한문제 또는 token 만료
                                401 -> view?.toast(R.string.api_unauthorized_str)
                                // 짧은 기간 내에 잘못된 자격 증명이있는 여러 요청을 감지
                                403 -> view?.toast(R.string.api_max_req_str)
                                // 422 Unprocessable Entity, 1000건 이상 결과를 호출하는 경우
                                422 -> view?.toast(R.string.api_limit_str)
                                else -> view?.toast("${it.code()} " + it.message())
                            }
                            DLog.e("TEST", it.message())
                        }
                        handleError(e)
                    }
                })
        }
    }

    private fun initRepositoriesAdapter(data: RepositoriesResponse, page: Int) {
        adapterModel?.initData(data.items)
        view?.responseRepositoriesData(data, page)
    }

    private fun moreRepositoriesAdapter(data: RepositoriesResponse, page: Int) {
        adapterModel?.addAllData(data.items)
        view?.responseRepositoriesData(data, page)
    }

}