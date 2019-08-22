// IOnSongChangedListener.aidl
package com.uting.aidl;

import com.uting.aidl.Chapter;
// Declare any non-default types here with import statements

interface IOnSongChangedListener {
    //该方法运行在线程池中（非 UI 线程）
        void onSongChange(in Chapter which, int index);
}
