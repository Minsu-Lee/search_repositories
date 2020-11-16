package com.jackson.repositories.presenter

import com.jackson.repositories.base.BaseAdapterView
import com.jackson.repositories.model.RepositoriesData

interface RepositoriesAdapterConstract {

    interface View: BaseAdapterView {
        fun onViewClickListener(data: RepositoriesData, position: Int)
    }

    interface Model {
        fun initData(list: ArrayList<RepositoriesData>)
        fun addAllData(list: ArrayList<RepositoriesData>)
        fun clear(): Boolean
    }

}