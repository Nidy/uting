package com.uting.ui.home.contract

import com.uting.base.BasePresenter
import com.uting.base.BaseView
import com.uting.ui.home.entity.ChapterInfo

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
interface PlayContract {
    interface View : BaseView<Presenter> {
        fun onRefresh(chapters: List<ChapterInfo>, isEnd: Boolean)

        fun onChaptersLoaded(chapters: List<ChapterInfo>, isEnd: Boolean)

        fun onLoadError(msg: String?)
    }

    interface Presenter : BasePresenter {

        fun refresh()

        fun loadMore()
    }
}