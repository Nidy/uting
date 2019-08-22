package com.uting.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.uting.R
import com.uting.ui.home.entity.ChapterInfo
import com.uting.util.DateUtils
import kotlinx.android.synthetic.main.item_chapter.view.*

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayListAdapter(
    data: MutableList<ChapterInfo>
) :
    BaseAdapter<ChapterInfo>(data, R.layout.item_chapter) {

    private var mListener: ChapterPlayClickListener? = null
    private var mCurrentPlayIndex: Int = 0

    fun setItemClickListener(listener: ChapterPlayClickListener) {
        mListener = listener
    }

    fun updateSelectedItem(pos: Int) {
        mCurrentPlayIndex = pos
    }

    override fun render(itemView: View, data: ChapterInfo, position: Int) {

        with(data) {
            itemView.tv_title.text = title
            itemView.tv_duration.text = DateUtils.formatSecToStr(duration)
            if (position == mCurrentPlayIndex) {
                itemView.tv_title.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark))
                itemView.tv_duration.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryDark))
            } else {
                itemView.tv_title.setTextColor(ContextCompat.getColor(itemView.context, R.color.textColorPrimary))
                itemView.tv_duration.setTextColor(ContextCompat.getColor(itemView.context, R.color.textColorSecondary))

            }
        }

        itemView.setOnClickListener {
            val args = Bundle()
            args.putSerializable("chapterInfo", data)
            args.putSerializable("index", position)
            mListener?.onItemClick(data, position)
            notifyDataSetChanged()
        }
    }

    interface ChapterPlayClickListener {
        fun onItemClick(chapterInfo: ChapterInfo, position: Int)
    }
}