package com.uting.aidl

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class OnSongChangedListener : IOnSongChangedListener.Stub() {

    abstract override fun onSongChange(which: Chapter?, index: Int)
}