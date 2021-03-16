package com.jackson.repositoriestest.viewModel

import com.jackson.repositoriestest.base.BaseViewModel
import com.jackson.repositoriestest.base.ParamsInfo
import com.jackson.repositoriestest.http.GithubApiService
import com.jackson.repositoriestest.model.RepositoriesData
import com.jackson.repositoriestest.utils.NotNullMutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GithubViewModel(private val api: GithubApiService): BaseViewModel() {

    private val _totalCount: NotNullMutableLiveData<Int> = NotNullMutableLiveData(0)
    val totalCount: NotNullMutableLiveData<Int>
        get() = _totalCount

    private val _incompleteResults: NotNullMutableLiveData<Boolean> = NotNullMutableLiveData(false)
    val incompleteResults: NotNullMutableLiveData<Boolean>
        get() = _incompleteResults

    private val _items: NotNullMutableLiveData<List<RepositoriesData>> = NotNullMutableLiveData(arrayListOf())
    val items: NotNullMutableLiveData<List<RepositoriesData>>
        get() = _items

    fun searchRepositories(query: String = "mvvm", page: Int = 1, pageNum: Int = 30) {
        hashMapOf<String, Any?>().apply {
            put(ParamsInfo.KEY_SEARCH_QUERY, query)
            put(ParamsInfo.KEY_SEARCH_PAGE, "$page")
            put(ParamsInfo.KEY_SEARCH_PER_PAGE, "$pageNum")
        }.let { params ->

            addDisposable(api.searchRepositories(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    totalCount.value = it.totalCount
                    incompleteResults.value = it.incompleteResults
                    items.value = it.items
                    println("result : ${it.items.size}")
                }, {
                    println("Throwable: ${it.message}")
                }))
        }
    }

}