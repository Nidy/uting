package com.uting.service

import android.content.ComponentName
import android.os.IBinder

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
interface OnServiceConnect {
    fun onConnected(name: ComponentName, service: IBinder)

    fun disConnected(name: ComponentName)
}