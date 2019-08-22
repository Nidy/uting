package com.uting.aidl

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class OnPlayListChangedListener : IOnPlayListChangedListener.Stub() {
    abstract override fun onPlayListChange(current: Chapter?, index: Int)
}