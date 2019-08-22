package com.uting.service

import android.media.MediaPlayer
import com.uting.aidl.Chapter
import com.uting.ui.home.entity.ChapterInfo


/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayController private constructor() {
    //未知错误
    val ERROR_UNKNOWN = -1

    val ERROR_INVALID = -2

    //歌曲文件解码错误
    val ERROR_DECODE = -3

    //没有指定歌曲
    val ERROR_NO_RESOURCE = -4

    //正在播放
    val STATUS_PLAYING = 10
    //播放结束
    val STATUS_COMPLETE = 11

    //开始播放
    val STATUS_START = 12

    //播放暂停
    val STATUS_PAUSE = 13

    //停止
    val STATUS_STOP = 14

    private var mPlayer: MediaPlayer = MediaPlayer()
    private var mPlayState =  STATUS_STOP
    private lateinit var mChapterInfo: ChapterInfo
    private var mPlayList: MutableList<Chapter> = mutableListOf()
    private var mCurrentIndex: Int = -1
    private var hasMediaPlayerInit = false

    private var mNotifyStatusChanged: NotifyStatusChanged? = null
    private var mNotifyPlayListChanged: NotifyPlayListChanged? = null
    private var mNotifySongChanged: NotifySongChanged? = null

    interface NotifyStatusChanged {
        fun notify(cahpter: Chapter?, index: Int, status: Int)
    }

    interface NotifySongChanged {
        fun notify(cahpter: Chapter?, index: Int)
    }

    interface NotifyPlayListChanged {
        fun notify(current: Chapter?, index: Int)
    }

    companion object {

        private var INSTANCE: PlayController? = null
            get() {
                if (field == null) {
                    field = PlayController()
                }

                return field
            }

        @Synchronized
        fun get(): PlayController {

            return INSTANCE!!
        }
    }

    fun setChapter(chapterInfo: ChapterInfo) {
        mChapterInfo = chapterInfo

        if (mPlayState == STATUS_PLAYING || mPlayState == STATUS_PAUSE) {
            mPlayer.stop()
        }

        if (mPlayState != STATUS_PLAYING) {
            mPlayer.reset()
            try {
                mPlayer.setDataSource(mChapterInfo.playUrl32)
                mPlayer.prepareAsync()
                hasMediaPlayerInit = true
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    fun setPlayList(list: MutableList<Chapter>, currentPos: Int): Chapter {
        mPlayList.clear()
        mPlayList.addAll(list)

        mCurrentIndex = currentPos
        changeChapter()

        val currChapter: Chapter = list.get(currentPos)
        mNotifyPlayListChanged?.notify(currChapter, currentPos)
        return  currChapter
    }

    fun getPlayList(): MutableList<Chapter> {
        return this.mPlayList
    }

    fun resume(): Int {
        if (mPlayState != STATUS_PLAYING) {
            mPlayer.start()
            mPlayState = STATUS_PLAYING

            mNotifyStatusChanged?.notify(getCurrentChapter(), mCurrentIndex, STATUS_START)
        }

        return mPlayState
    }

    fun pause(): Int {
        if (mPlayState == STATUS_PLAYING) {
            mPlayer.pause()
            mPlayState = STATUS_PAUSE

            mNotifyStatusChanged?.notify(getCurrentChapter(), mCurrentIndex, STATUS_STOP)
        }

        return mPlayState
    }

    fun  play(info: Chapter) : Int {
        return play(getChapterIndexOf(info))
    }

    fun play(index: Int) : Int {
        var result = ERROR_INVALID
        if (index != -1) { //列表中有该歌曲
            if (mCurrentIndex != index) { //不是当前歌曲
                mCurrentIndex = index
                if (mPlayState != STATUS_PLAYING) {
                    mPlayState = STATUS_PLAYING //切换并播放
                }
                result = changeChapter()
            } else if (mPlayState != STATUS_PLAYING) { // 是但没在播放
                mPlayState = STATUS_PAUSE
                resume()//播放
            } else {
                // 是且已经在播放
                return 1
            }
        } else {
            return ERROR_NO_RESOURCE
        }

        return result
    }

    fun prepare(chapter: Chapter) : Int {
        var result = ERROR_INVALID
        val index = mPlayList.indexOf(chapter)
        if (-1 != index) {
            if (mCurrentIndex != index) { //不是当前歌曲
                mCurrentIndex = index
                if (mPlayState != STATUS_PLAYING) {
                    mPlayState = STATUS_PLAYING //切换并播放
                }
                result = changeChapter()
            }
        } else return ERROR_NO_RESOURCE
        return result
    }

    fun pre() : Chapter? {
        if (mCurrentIndex == 0) {
            mCurrentIndex = mPlayList.size - 1
        } else{
            mCurrentIndex--
        }

        changeChapter()

        return getCurrentChapter()
    }

    fun next(): Chapter? {
        if (mCurrentIndex == mPlayList.size -1) {
            mCurrentIndex = 0
        } else {
            mCurrentIndex++
        }

        changeChapter()

        return getCurrentChapter()
    }

    private fun changeChapter(): Int {
        if (mPlayState == STATUS_PLAYING || mPlayState == STATUS_PAUSE) {
            mPlayer.stop()
        }

        if (hasMediaPlayerInit) {
            mPlayer.reset()
        }
        if (mPlayList.size == 0) {
            mCurrentIndex = 0
            return ERROR_NO_RESOURCE
        } else{
            try {
                mPlayer.setDataSource(mPlayList[mCurrentIndex].path)
                mPlayer.prepareAsync()
                hasMediaPlayerInit = true
            } catch (e: Exception) {
                e.stackTrace
                return ERROR_DECODE
            }
        }

        if (mPlayState == STATUS_PLAYING) {
            mPlayer.start()
        }

        return 1
    }

    //获得播放状态
    fun getPlayState(): Int {
        return mPlayState
    }

    fun getCurrentSongIndex(): Int {
        return mCurrentIndex
    }

    //当前正在播放曲目
    fun getCurrentChapter(): Chapter? {
        return if (mPlayList.size == 0) null else mPlayList[mCurrentIndex]
    }

    fun setNotifyStatusChanged(notifyStatusChanged: NotifyStatusChanged) {
        this.mNotifyStatusChanged = notifyStatusChanged
    }

    fun setNotifyPlayListChanged(notifyPlayListChanged: NotifyPlayListChanged) {
        this.mNotifyPlayListChanged = notifyPlayListChanged
    }

    fun setNotifySongChanged(notifySongChanged: NotifySongChanged) {
        this.mNotifySongChanged = notifySongChanged
    }

    private fun getChapterIndexOf(chapter: Chapter): Int {
        for (i in mPlayList.indices) {
            val item: Chapter = mPlayList[i]
            if (item.id == chapter.id && item.path.equals(chapter.path)) {
                return i
            }
        }
        return -1
    }
}