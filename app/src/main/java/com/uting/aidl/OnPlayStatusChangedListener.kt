package com.uting.aidl

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class OnPlayStatusChangedListener : IOnPlayStatusChangedListener.Stub() {

    abstract override fun playStart(which: Chapter?, index: Int, status: Int)

    abstract override fun playStop(which: Chapter?, index: Int, status: Int)
}