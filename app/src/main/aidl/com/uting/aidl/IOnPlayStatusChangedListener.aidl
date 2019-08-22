// IOnPlayStatusChangedListener.aidl
package com.uting.aidl;

import com.uting.aidl.Chapter;

// Declare any non-default types here with import statements

interface IOnPlayStatusChangedListener {
    //自动播放时歌曲播放完成时回调
    void playStop(in Chapter which, int index, int status);

    //自动播放时歌曲开始播放时回调
    void playStart(in Chapter which, int index, int status);
}
