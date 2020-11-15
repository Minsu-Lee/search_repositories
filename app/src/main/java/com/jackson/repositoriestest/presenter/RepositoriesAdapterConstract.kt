package com.jackson.repositoriestest.presenter

import com.jackson.repositoriestest.base.BaseAdapterView
import com.jackson.repositoriestest.model.RepositoriesData

interface RepositoriesAdapterConstract {

    interface View: BaseAdapterView {
        fun onViewClickListener(data: RepositoriesData, position: Int)
    }

    interface Model {
        fun initData(list: ArrayList<RepositoriesData>)
        fun addAllData(list: ArrayList<RepositoriesData>)
        fun clear()
    }

}