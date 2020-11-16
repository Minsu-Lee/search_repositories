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
                            if (it.code() == 422 && it.message().indexOf("Unprocessable Entity") >= 0) {
                                DLog.e("TEST", it.message()) // 1000까지가 리밋
                                view?.toast(R.string.api_limit_str)
                            }
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