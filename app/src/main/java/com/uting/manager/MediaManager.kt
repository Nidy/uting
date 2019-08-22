package com.uting.manager

import com.uting.aidl.Chapter
import com.uting.ui.home.entity.ChapterInfo

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
object MediaManager {

    fun convertChapter(chapterInfo: ChapterInfo): Chapter {
        return Chapter(chapterInfo.id, chapterInfo.playUrl64)
    }

    fun convertChapterList(infoList: MutableList<ChapterInfo>): MutableList<Chapter> {
        val chapterList: MutableList<Chapter> = mutableListOf()
        for (chapterInfo in infoList) {
            chapterList.add(Chapter(chapterInfo.id, chapterInfo.playUrl64))
        }

        return chapterList
    }
}