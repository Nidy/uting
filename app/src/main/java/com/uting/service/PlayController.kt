package com.uting.service

import android.media.MediaPlayer
import com.uting.ui.home.entity.ChapterInfo


/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayController private constructor() {
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
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    fun play() {
        if (mPlayState != STATUS_PLAYING) {
            mPlayer.start()
            mPlayState = STATUS_PLAYING
        }
    }

    fun pause() {
        if (mPlayState == STATUS_PLAYING) {
            mPlayer.pause()
            mPlayState = STATUS_PAUSE
        }
    }
}