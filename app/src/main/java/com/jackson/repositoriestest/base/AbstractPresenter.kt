package com.jackson.repositoriestest.base

import com.jackson.repositoriestest.utils.DLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AbstractPresenter<VIEW: BaseView>: BasePresenter<VIEW> {

    override var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    protected var view: VIEW? = null

    override fun attachView(view: VIEW) {
        this.view = view
    }

    override fun detachView() {
        view = null
        clearDisposable()
    }

    override fun handleError(error: Throwable) {
        error.printStackTrace()
        view?.onInvisibleProgress()
        DLog.e("handleError", "error: ${error.message}")
    }

    fun addDisposable(disposable: Disposable?) {
        disposable?.let { mCompositeDisposable.add(it) }
    }

    fun removeDisposable(disposable: Disposable?) {
        disposable?.let { mCompositeDisposable.dispose() }
    }

    fun clearDisposable() = mCompositeDisposable.let { if (it.size() > 0) it.clear() }

}