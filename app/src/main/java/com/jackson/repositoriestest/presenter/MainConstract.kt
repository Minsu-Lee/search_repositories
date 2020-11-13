package com.jackson.repositoriestest.presenter

import com.jackson.repositoriestest.base.BasePresenter
import com.jackson.repositoriestest.base.BaseView
import com.jackson.repositoriestest.model.RepositoriesResponse

interface MainConstract {

    interface View: BaseView {

        /**
         * 검색 버튼 클릭, Query 조합
         */
        fun clickSearchIcon()

        /**
         * RecyclerView 데이터 갱신
         */
        fun initRepositoriesAdapter(data: RepositoriesResponse)

    }

    interface Presenter: BasePresenter<View> {

        /**
         * Repositoiries 조회 api
         * row ( per_page ) : 분당 최대 조회 갯수 100개 ( 인증시 )
         */
        fun searchRepositories(query: String, page: Int, row: Int = 100)

    }

}