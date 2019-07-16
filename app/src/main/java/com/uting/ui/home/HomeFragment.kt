package com.uting.ui.home

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.uting.R
import com.uting.base.BaseFragment
import com.uting.ui.adapter.ChaptersAdapter
import com.uting.ui.adapter.LoadMoreListener
import com.uting.ui.adapter.holder.LoadingHolder
import com.uting.ui.home.contract.HomePageContract
import com.uting.ui.home.entity.ChapterInfo
import com.uting.ui.home.presenter.HomePagePresenter

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class HomeFragment : BaseFragment(), HomePageContract.View {

    override lateinit var mPresenter: HomePageContract.Presenter

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mChaptersAdapter: ChaptersAdapter
    private var mChapterList: MutableList<ChapterInfo> = mutableListOf()

    override fun getContentViewLayout(): Int {
        return R.layout.fragment_home
    }

    override fun setUpView(view: View) {
        super.setUpView(view)
        mPresenter = HomePagePresenter(this)

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        mRecyclerView = view.findViewById(R.id.recyclerview)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        mChaptersAdapter = ChaptersAdapter(mChapterList)
        mRecyclerView.adapter = mChaptersAdapter

        mChaptersAdapter.loadMoreListener = object : LoadMoreListener {
            override fun loadMore() {
                mPresenter.loadMore()
            }
        }

        mRecyclerView.post {
            mSwipeRefreshLayout.isRefreshing = true
            mPresenter.start()
        }
    }

    override fun registerListeners() {
        super.registerListeners()
        mSwipeRefreshLayout.setOnRefreshListener {
            mSwipeRefreshLayout.isRefreshing = true
            mPresenter.start()
        }
    }

    private fun refreshCompleted() {
        mRecyclerView.post {
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onChaptersLoaded(chapters: List<ChapterInfo>, isEnd: Boolean) {
        this.mChapterList.addAll(chapters)
        this.mChaptersAdapter.loadMoreCompleted()
        isDataEnd(isEnd)
        this.mChaptersAdapter.notifyDataSetChanged()
    }

    override fun onRefresh(chapters: List<ChapterInfo>, isEnd: Boolean) {
        this.mChapterList.clear()
        this.mChapterList.addAll(chapters)
        this.mChaptersAdapter.notifyDataSetChanged()
        refreshCompleted()
    }

    override fun onLoadError(msg: String?) {
        refreshCompleted()
    }

    private fun isDataEnd(isEnd: Boolean) {
        if (isEnd) {
            mChaptersAdapter.loadingStatus = LoadingHolder.STATUS_END
        } else {
            mChaptersAdapter.loadingStatus = LoadingHolder.STATUS_LOADING
        }
    }
}