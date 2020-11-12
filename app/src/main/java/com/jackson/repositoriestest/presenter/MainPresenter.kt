package com.jackson.repositoriestest.presenter

import com.jackson.repositoriestest.base.AbstractPresenter

class MainPresenter: AbstractPresenter<MainConstract.View>(), MainConstract.Presenter {

    override fun searchRepositories(query: String, page: Int, row: Int) {
        view?.onVisibleProgress()
        view?.initRepositoriesAdapter(arrayListOf())
    }

}