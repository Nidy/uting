// IOnPlayListChangedListener.aidl
package com.uting.aidl;

import com.uting.aidl.Chapter;
// Declare any non-default types here with import statements

interface IOnPlayListChangedListener {
    void onPlayListChange(in Chapter current, int index);
}
