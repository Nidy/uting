package com.uting.ui.widget

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uting.R
import com.uting.aidl.IPlayControl
import com.uting.manager.MediaManager
import com.uting.ui.adapter.LoadMoreListener
import com.uting.ui.adapter.PlayListAdapter
import com.uting.ui.adapter.holder.LoadingHolder
import com.uting.ui.home.contract.PlayContract
import com.uting.ui.home.entity.ChapterInfo
import com.uting.ui.home.presenter.PlayPresenter
import com.uting.util.DisplayUtils

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayListBottomSheetDialog constructor(
    context: Context,
    control: IPlayControl
) : BottomSheetDialog(context), PlayContract.View {
    override var mPresenter: PlayContract.Presenter = PlayPresenter(this)

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mChaptersAdapter: PlayListAdapter
    private var mChapterList: MutableList<ChapterInfo> = mutableListOf()
    private var mBehavior: BottomSheetBehavior<View>

    private var mControl: IPlayControl = control
    private var mCurrentPos = 0

    init {
        val view = layoutInflater.inflate(R.layout.dialog_play_list, null)
        initView(view)
        setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        (view.parent as View).setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        mBehavior.peekHeight = (DisplayUtils.getScreenHeight(context) * 0.6).toInt()
    }

    private fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.rv_play_list)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        mChaptersAdapter = PlayListAdapter(mChapterList)
        mChaptersAdapter.setItemClickListener(PlayListListListener())
        mRecyclerView.adapter = mChaptersAdapter

        mChaptersAdapter.loadMoreListener = object : LoadMoreListener {
            override fun loadMore() {
                mPresenter.loadMore()
            }
        }

        mRecyclerView.post {
            mPresenter.start()
        }
    }

    override fun show() {
        super.show()
        mBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun refreshCompleted() {
        mRecyclerView.post {
//            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onRefresh(chapters: List<ChapterInfo>, isEnd: Boolean) {
        this.mChapterList.clear()
        this.mChapterList.addAll(chapters)
        this.mChaptersAdapter.loadMoreCompleted()
        this.mChaptersAdapter.notifyDataSetChanged()
        mControl.setPlayList(MediaManager.convertChapterList(mChapterList), mCurrentPos)
        refreshCompleted()
    }

    override fun onChaptersLoaded(chapters: List<ChapterInfo>, isEnd: Boolean) {
        this.mChapterList.addAll(chapters)
        this.mChaptersAdapter.loadMoreCompleted()
        isDataEnd(isEnd)
        this.mChaptersAdapter.notifyDataSetChanged()
        mControl.setPlayList(MediaManager.convertChapterList(mChapterList), mCurrentPos)
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

    fun setSelectPos(pos: Int) {
        mChaptersAdapter.updateSelectedItem(pos)
    }

    inner class PlayListListListener : PlayListAdapter.ChapterPlayClickListener {
        override fun onItemClick(chapterInfo: ChapterInfo, position: Int) {
            mControl.play(MediaManager.convertChapter(chapterInfo))
            mCurrentPos = position
        }

    }

}