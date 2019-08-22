package com.uting.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.uting.aidl.PlayControlImpl

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayService : Service() {

    private lateinit var mPlayControlBinder: PlayControlImpl

    override fun onCreate() {
        super.onCreate()
        mPlayControlBinder = PlayControlImpl(this)

        mPlayControlBinder.onDataIsReady()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mPlayControlBinder
    }
}