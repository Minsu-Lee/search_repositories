package com.jackson.repositoriestest.view.adapter

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.jackson.repositoriestest.model.RepositoriesData
import com.jackson.repositoriestest.presenter.MainConstract
import com.jackson.repositoriestest.presenter.RepositoriesAdapterConstract
import com.jackson.repositoriestest.utils.DLog
import com.jackson.repositoriestest.view.adapter.holder.EmptyItemViewHolder
import com.jackson.repositoriestest.view.adapter.holder.RepositoriesItemViewHolder
import com.jackson.repositoriestest.view.adapter.ui.EmptyItemUI
import com.jackson.repositoriestest.view.adapter.ui.RepositoriesItemUI

class RepositoriesAdapter(val view: MainConstract.View): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    RepositoriesAdapterConstract.View, RepositoriesAdapterConstract.Model {

    companion object {
        const val TAG = "BaseAdapter"
        const val TYPE_ITEM_CARD = 100
        const val TYPE_EMPTY_CARD = -100
    }

    var list: ArrayList<RepositoriesData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_ITEM_CARD -> RepositoriesItemViewHolder(parent, RepositoriesItemUI())
            else -> EmptyItemViewHolder(parent, EmptyItemUI())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_ITEM_CARD -> {
                holder as RepositoriesItemViewHolder
                holder.onBind(list[position], position, this)
            }
            else -> DLog.e(TAG, "TYPE_EMPTY_CARD")
        }
    }

    /**
     * 저장소 아이템 클릭 리스너
     */
    override fun onViewClickListener(data: RepositoriesData, position: Int) {
        DLog.e(TAG, "onViewClickListener, position: $position")
        // MainActivity의 View 호출
        view.openRepositories(data)
    }

    /**
     * ViewType 정의
     * 참조하려는 포지션이 list.size 보다 작을 경우, TYPE_EMPTY_CARD 를 반환
     */
    override fun getItemViewType(position: Int): Int
            = if (position <= list.size) TYPE_ITEM_CARD else TYPE_EMPTY_CARD

    override fun getItemCount(): Int = list.size

    override fun initData(datas: ArrayList<RepositoriesData>) {
        list.clear()
        list.addAll(datas)
        notifyAdapter()
    }

    override fun addAllData(datas: ArrayList<RepositoriesData>) {
        val beforeSize = list.size
        list.addAll(datas)
        notifyItemRangeChanged(beforeSize, list.size)
    }

    override fun clear() {
        if (list.size > 0) {
            list.clear()
            notifyAdapter()
        }
    }

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

}