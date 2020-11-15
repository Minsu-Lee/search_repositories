package com.jackson.repositories.presenter

import com.jackson.repositories.ParamsInfo
import com.jackson.repositories.base.AbstractPresenter
import com.jackson.repositories.http.NetworkManager
import com.jackson.repositories.model.RepositoriesResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter: AbstractPresenter<MainConstract.View>(), MainConstract.Presenter {

    override fun searchRepositories(query: String, page: Int, row: Int) {
        view?.onVisibleProgress()
        NetworkManager.defaultParams().apply {
            put(ParamsInfo.KEY_SEARCH_QUERY, query)
            put(ParamsInfo.KEY_SEARCH_PAGE, "$page")
            put(ParamsInfo.KEY_SEARCH_PER_PAGE, "$row")
        }.let { params ->
            addDisposable(
                NetworkManager.service.searchRepositories(params)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::initRepositoriesAdapter, this::handleError))
        }
    }
    private fun initRepositoriesAdapter(data: RepositoriesResponse) {
        view?.initRepositoriesAdapter(data)
        view?.onInvisibleProgress()
    }

}