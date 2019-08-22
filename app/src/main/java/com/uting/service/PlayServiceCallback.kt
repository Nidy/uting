package com.uting.service

import com.uting.aidl.Chapter
import com.uting.aidl.IPlayControl

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
interface PlayServiceCallback {
    /**
     * 当前歌曲改变时回调
     * @see com.duan.musicoco.aidl.OnSongChangedListener.onSongChange
     */
    abstract fun songChanged(song: Chapter, index: Int)

    /**
     * 此方法由服务端控制调用
     * @see com.duan.musicoco.aidl.OnPlayStatusChangedListener.playStart
     */
    abstract fun startPlay(song: Chapter, index: Int, status: Int)

    /**
     * 此方法由服务端控制调用
     * @see com.duan.musicoco.aidl.OnPlayStatusChangedListener.playStop
     */
    abstract fun stopPlay(song: Chapter, index: Int, status: Int)


    /**
     * 此方法由服务端控制调用
     *
     * @see com.duan.musicoco.aidl.OnPlayListChangedListener.onPlayListChange
     */
    abstract fun onPlayListChange(current: Chapter, index: Int)

    /**
     * 服务端数据初始化完成时回调，客户端与服务器的交互应在此调用到达时才开始
     *
     * @param mControl
     */
    abstract fun dataIsReady(mControl: IPlayControl)
}