package com.uting.ui.adapter

import android.view.View
import com.uting.R
import com.uting.ui.home.entity.ChapterInfo
import com.uting.util.DateUtils
import kotlinx.android.synthetic.main.item_chapter.view.*

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class ChaptersAdapter(
    data: MutableList<ChapterInfo>
) :
    BaseAdapter<ChapterInfo>(data, R.layout.item_chapter) {

    override fun render(itemView: View, data: ChapterInfo) {
        itemView.tv_title.text = data.title
        itemView.tv_duration.text = DateUtils.formatSecToStr(data.duration)
    }
}