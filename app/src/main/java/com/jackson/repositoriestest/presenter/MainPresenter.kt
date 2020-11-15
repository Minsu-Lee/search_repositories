package com.jackson.repositoriestest.presenter

import com.jackson.repositoriestest.base.AppConst
import com.jackson.repositoriestest.base.ParamsInfo
import com.jackson.repositoriestest.base.AbstractPresenter
import com.jackson.repositoriestest.http.NetworkManager
import com.jackson.repositoriestest.model.RepositoriesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
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
            addDisposable(
                NetworkManager.service.searchRepositories(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if (page == AppConst.PAGE_FIRST_VALUE) initRepositoriesAdapter(it, page)
                        else moreRepositoriesAdapter(it, page)
                        view?.onInvisibleProgress()
                    }, this::handleError))
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