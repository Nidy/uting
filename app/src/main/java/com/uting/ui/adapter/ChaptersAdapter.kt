package com.uting.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
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

    private var mListener: ChapterPlayClickListener? = null

    fun setItemClickListener(listener: ChapterPlayClickListener) {
        mListener = listener
    }

    override fun render(itemView: View, data: ChapterInfo, position: Int) {

        with(data) {
            itemView.tv_title.text = title
            itemView.tv_duration.text = DateUtils.formatSecToStr(duration)
        }

        itemView.setOnClickListener {
            val args = Bundle()
            args.putSerializable("chapterInfo", data)
            args.putSerializable("index", position)
            Navigation.findNavController(itemView).navigate(R.id.play_dest, args)
        }
    }

    interface ChapterPlayClickListener {
        fun onItemClick(chapterInfo: ChapterInfo, position: Int)
    }
}