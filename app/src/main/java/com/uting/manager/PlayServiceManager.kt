package com.uting.manager

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import com.uting.service.PlayService

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayServiceManager constructor(context: Context) {

    private var mContext: Context = context


    companion object {
        fun startPlayService(context: Context) {
            val intent = Intent(context, PlayService::class.java)
            context.startService(intent)
        }
    }

    fun bindPlayService(connection : ServiceConnection) {
        val intent = Intent(mContext, PlayService::class.java)
        mContext.bindService(intent, connection, Service.BIND_AUTO_CREATE)
    }
}