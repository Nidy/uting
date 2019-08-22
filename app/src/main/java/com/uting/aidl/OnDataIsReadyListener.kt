package com.uting.aidl

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class OnDataIsReadyListener : IOnDataIsReadyListener.Stub() {

    abstract override fun dataIsReady()

}