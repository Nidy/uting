package com.uting.aidl

import android.content.Context
import android.os.RemoteCallbackList
import android.os.RemoteException
import com.uting.service.PlayController

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayControlImpl constructor(context: Context) : IPlayControl.Stub() {

    protected var mDataIsReadyListeners : RemoteCallbackList<IOnDataIsReadyListener> = RemoteCallbackList()
    protected var mSongChangedListeners : RemoteCallbackList<IOnSongChangedListener> = RemoteCallbackList()
    protected var mPlayStatusChangedListeners : RemoteCallbackList<IOnPlayStatusChangedListener> = RemoteCallbackList()
    protected var mPlayListChangedListeners : RemoteCallbackList<IOnPlayListChangedListener> = RemoteCallbackList()

    private var mContext: Context = context
    private var mPlayManager: PlayController = PlayController.get()

    init {

        mPlayManager.also {
            it.setNotifySongChanged(NotifySongChange())
            it.setNotifyStatusChanged(NotifyStatusChange())
            it.setNotifyPlayListChanged(NotifyPlayListChange())
        }
    }

    override fun play(which: Chapter?): Int {

        which?.run {
            if (mPlayManager.getCurrentChapter() != which) {
                mPlayManager.play(which)
            }
        }
        return mPlayManager.ERROR_UNKNOWN
    }

    override fun playByIndex(index: Int): Int {
        if (index < mPlayManager.getPlayList().size && mPlayManager.getCurrentSongIndex() != index) {
            return mPlayManager.play(index)
        }

        return mPlayManager.ERROR_UNKNOWN
    }

    override fun setCurrentSong(song: Chapter?): Int {
        song?.let {
            mPlayManager.prepare(it)
        }

        return mPlayManager.ERROR_UNKNOWN
    }

    override fun pre(): Chapter? {
        return mPlayManager.pre()
    }

    override fun next(): Chapter? {
        return mPlayManager.next()
    }

    override fun pause(): Int {
        return mPlayManager.pause()
    }

    override fun resume(): Int {
        return mPlayManager.resume()
    }

    override fun currentSong(): Chapter? {
        return mPlayManager.getCurrentChapter()
    }

    override fun currentSongIndex(): Int {
        return mPlayManager.getCurrentSongIndex()
    }

    override fun status(): Int {
        return mPlayManager.getPlayState()
    }

    override fun setPlayList(songs: MutableList<Chapter>?, current: Int): Chapter? {
        if (songs?.size!! <= 0) {
            return null
        }

        return mPlayManager.setPlayList(songs, current)
    }

    override fun setPlaySheet(sheetID: Int, current: Int): Chapter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlayList(): MutableList<Chapter> {
        return mPlayManager.getPlayList()
    }

    override fun registerOnSongChangedListener(li: IOnSongChangedListener?) {
        mSongChangedListeners.register(li)
    }

    override fun registerOnPlayStatusChangedListener(li: IOnPlayStatusChangedListener?) {
        mPlayStatusChangedListeners.register(li)
    }

    override fun registerOnPlayListChangedListener(li: IOnPlayListChangedListener?) {
        mPlayListChangedListeners.register(li)
    }

    override fun registerOnDataIsReadyListener(li: IOnDataIsReadyListener?) {
        mDataIsReadyListeners.register(li)
    }

    override fun unregisterOnSongChangedListener(li: IOnSongChangedListener?) {
        mSongChangedListeners.unregister(li)
    }

    override fun unregisterOnPlayStatusChangedListener(li: IOnPlayStatusChangedListener?) {
        mPlayStatusChangedListeners.unregister(li)
    }

    override fun unregisterOnPlayListChangedListener(li: IOnPlayListChangedListener?) {
        mPlayListChangedListeners.unregister(li)
    }

    override fun unregisterOnDataIsReadyListener(li: IOnDataIsReadyListener?) {
        mDataIsReadyListeners.unregister(li)
    }

    override fun setPlayMode(mode: Int) {
    }

    override fun getProgress(): Int {
        return -1
    }

    override fun seekTo(pos: Int): Int {
        return -1
    }

    override fun remove(song: Chapter?) {

    }

    override fun getPlayMode(): Int {
        return -1
    }

    fun onDataIsReady() {
        val N = mDataIsReadyListeners.beginBroadcast()
        for (i in 0 until N) {
            val listener = mDataIsReadyListeners.getBroadcastItem(i)
            if (listener != null) {
                try {
                    listener.dataIsReady()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }
        }
        mDataIsReadyListeners.finishBroadcast()
    }

    private inner class NotifySongChange : PlayController.NotifySongChanged {

        override fun notify(cahpter: Chapter?, index: Int) {
            val N = mSongChangedListeners.beginBroadcast()
            for (i in 0 until N) {
                val listener = mSongChangedListeners.getBroadcastItem(i)
                if (listener != null) {
                    try {
                        listener.onSongChange(cahpter, index)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }

                }
            }
            mSongChangedListeners.finishBroadcast()
        }
    }

    private inner class NotifyStatusChange : PlayController.NotifyStatusChanged {

        override fun notify(cahpter: Chapter?, index: Int, status: Int) {
            val N = mPlayStatusChangedListeners.beginBroadcast()
            for (i in 0 until N) {
                val listener = mPlayStatusChangedListeners.getBroadcastItem(i)
                if (listener != null) {
                    try {
                        when (status) {
                            PlayController.get().STATUS_START -> listener.playStart(cahpter, index, status)
                            PlayController.get().STATUS_STOP -> listener.playStop(cahpter, index, status)
                        }

                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }

                }
            }
            mPlayStatusChangedListeners.finishBroadcast()
        }
    }

    private inner class NotifyPlayListChange : PlayController.NotifyPlayListChanged {
        override fun notify(current: Chapter?, index: Int) {
            val N = mPlayListChangedListeners.beginBroadcast()
            for (i in 0 until N) {
                val listener = mPlayListChangedListeners.getBroadcastItem(i)
                if (listener != null) {
                    try {
                        listener.onPlayListChange(current, index)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }

                }
            }
            mPlayListChangedListeners.finishBroadcast()
        }
    }
}