package com.jackson.repositoriestest.presenter

import com.jackson.repositoriestest.base.BasePresenter
import com.jackson.repositoriestest.base.BaseView
import com.jackson.repositoriestest.model.RepositoriesData
import com.jackson.repositoriestest.model.RepositoriesResponse

interface MainConstract {

    interface View: BaseView {

        /**
         * 검색 버튼 클릭, Query 조합 및 API 호출
         */
        fun clickSearchRepositories()

        /**
         * 특정 위치로 스크롤 시, 다음 페이지 API 호출
         */
        fun moreSearchRepositories(nextPage: Int)


        /* API 응답 호출 부*/
        fun responseRepositoriesData(data: RepositoriesResponse, page: Int) { }
        fun openRepositories(data: RepositoriesData)

    }

    interface Presenter: BasePresenter<View> {

        var adapterView: RepositoriesAdapterConstract.View?
        var adapterModel: RepositoriesAdapterConstract.Model?

        /**
         * Repositoiries 조회 api
         * row ( per_page ) : 분당 최대 조회 갯수 100개 ( 인증시 )
         */
        fun searchRepositories(query: String, page: Int = 0, row: Int = 100)

    }

}