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

    override fun render(itemView: View, data: ChapterInfo) {
        itemView.tv_title.text = data.title
        itemView.tv_duration.text = DateUtils.formatSecToStr(data.duration)

        itemView.setOnClickListener {
            val args = Bundle()
            args.putSerializable("chapterInfo", data)
            Navigation.findNavController(itemView).navigate(R.id.play_dest, args)
        }
    }
}