package com.jackson.repositoriestest.base


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class ListMoreScrollListener(var allItemCount: Int = 0) : RecyclerView.OnScrollListener() {

    private var mReqItemCount = 0           // first 요청 시 전달받은 아이템 갯수 저장

    private var mPage = 1                   // 현재 호출 페이지

    private var loading = true              // 스크롤 허용 유무

    private var mReqLastItemCount = 0       // onLoadData() 호출 전, totalItemCount

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        with(recyclerView) {

            when (layoutManager?.javaClass?.simpleName) {
                /**
                 * LinearLayoutManager
                 */
                LinearLayoutManager::class.java.simpleName,
                WrapContentLayoutManager::class.java.simpleName -> {
                    (layoutManager as LinearLayoutManager).apply {

                        val visibleItemCount = childCount                                                   // 화면에 보이는 아이템 갯수
                        val loadPreparationPosition = visibleItemCount * 10
                        val totalItemCount = itemCount                                                     // 아이템 전체 갯수
                        val pastVisibleItems = findFirstCompletelyVisibleItemPosition()             // 첫번째로 표시되는 아이템 포지션
                        val lastVisibleItemPosition = findLastCompletelyVisibleItemPosition()      // 현재화면에 출력된 리스트중 마지막 View의 Position을 반환

                        if (loading) {
                            if (allItemCount <= itemCount) {
                                loading = false
                                onCompletedLoading()
                            } else if (lastVisibleItemPosition + loadPreparationPosition >= totalItemCount
                                && (totalItemCount > mReqLastItemCount)) {

                                // first 요청 시 전달받은 아이템 갯수 저장
                                if (mPage == 1) mReqItemCount = totalItemCount

                                mPage++
                                mReqLastItemCount = totalItemCount
                                onLoadData(mPage, totalItemCount)
                            }

                            // first 요청 시, 전달받은 아이템 갯수 만큼 씩 moreReqItem 호출 마다 그 갯수만큼 반환 될 것이므로,
                            // 해당갯수만큼으로 전달받지 않은 경우는 마지막인 것으로 간주
                            if (mReqItemCount > 0 && (totalItemCount % mReqItemCount != 0)) {
                                loading = false
                                onCompletedLoading()
                            }
                        }

                    }
                }

                else -> { }
            }
        }
    }

    /**
     * page 단위로 구분해서 처리할 경우가 발생할 수 있어서 page도 카운팅한다.
     */
    abstract fun onLoadData(page: Int, count: Int)

    abstract fun onCompletedLoading()

    fun setEndScroll() {
        loading = false
        onCompletedLoading()
    }
}