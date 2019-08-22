package com.uting.service

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import com.uting.aidl.*

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayServiceConnection constructor(
    context: Context,
    serviceConnect: OnServiceConnect,
    playServiceCallback: PlayServiceCallback
) : ServiceConnection {

    private var mContext: Context = context
    private var mConnect: OnServiceConnect = serviceConnect
    private var mPlayServiceCallback: PlayServiceCallback = playServiceCallback
    private var mControl: IPlayControl? = null
    private var mHasConnect: Boolean = false

    private var mPlayStatusChangedListener: OnPlayStatusChangedListener = PlayStatusChangedListener()
    private val mSongChangedListener: OnSongChangedListener = SongChangedListener()
    private val mPlayListChangedListener: OnPlayListChangedListener = PlayListChangedListener()
    private val mDataIsReadyListener: OnDataIsReadyListener = DataIsReadyListener()

    override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
        mHasConnect = true
        mControl = IPlayControl.Stub.asInterface(binder)

        try {
            mControl?.run {
                registerOnDataIsReadyListener(mDataIsReadyListener)
                registerOnSongChangedListener(mSongChangedListener)
                registerOnPlayListChangedListener(mPlayListChangedListener)
                registerOnPlayStatusChangedListener(mPlayStatusChangedListener)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        mConnect.onConnected(componentName!!, binder!!)
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        mHasConnect = false
        mConnect.disConnected(componentName!!)
    }

    fun unRegisterListeners() {
        mControl?.run {
            unregisterOnDataIsReadyListener(mDataIsReadyListener)
            unregisterOnSongChangedListener(mSongChangedListener)
            unregisterOnPlayListChangedListener(mPlayListChangedListener)
            unregisterOnPlayStatusChangedListener(mPlayStatusChangedListener)
        }
    }

    inner class PlayStatusChangedListener : OnPlayStatusChangedListener() {
        override fun playStart(which: Chapter?, index: Int, status: Int) {
            which?.let { mPlayServiceCallback.startPlay(it, index, status) }
        }

        override fun playStop(which: Chapter?, index: Int, status: Int) {
            which?.let { mPlayServiceCallback.stopPlay(it, index, status) }
        }

    }

    inner class SongChangedListener : OnSongChangedListener() {
        override fun onSongChange(which: Chapter?, index: Int) {
            which?.let { mPlayServiceCallback.songChanged(it, index) }
        }

    }

    inner class PlayListChangedListener : OnPlayListChangedListener() {
        override fun onPlayListChange(current: Chapter?, index: Int) {
            current?.let { mPlayServiceCallback.onPlayListChange(it, index) }
        }

    }

    inner class DataIsReadyListener : OnDataIsReadyListener() {
        override fun dataIsReady() {
            mControl?.let { mPlayServiceCallback.dataIsReady(it) }

        }

    }

}