// IPlayControl.aidl
package com.uting.aidl;


import com.uting.aidl.Chapter;
import com.uting.aidl.IOnPlayListChangedListener;
import com.uting.aidl.IOnSongChangedListener;
import com.uting.aidl.IOnPlayStatusChangedListener;
import com.uting.aidl.IOnDataIsReadyListener;
// Declare any non-default types here with import statements

interface IPlayControl {
    //播放指定章节
    int play(in Chapter which);

    int playByIndex(int index);

    //设置当前歌曲
    int setCurrentSong(in Chapter song);

    //上一首
    Chapter pre();

    //下一首
    Chapter next();

    //暂停
    int pause();

    //继续
    int resume();

    //当前播放歌曲
    Chapter currentSong();

    int currentSongIndex();

    //播放状态
    int status();

    //设置播放列表，返回下一首播放歌曲
    // 为该播放列表设置一个 id ，用于区分不同歌单
    Chapter setPlayList(in List<Chapter> songs, int current);

    Chapter setPlaySheet(int sheetID, int current);

    //获得播放列表
    List<Chapter> getPlayList();

    //注册播放曲目改变时回调
    void registerOnSongChangedListener(IOnSongChangedListener li);
    void registerOnPlayStatusChangedListener(IOnPlayStatusChangedListener li);
    void registerOnPlayListChangedListener(IOnPlayListChangedListener li);
    void registerOnDataIsReadyListener(IOnDataIsReadyListener li);

    //取消注册播放曲目改变时回调
    void unregisterOnSongChangedListener(IOnSongChangedListener li);
    void unregisterOnPlayStatusChangedListener(IOnPlayStatusChangedListener li);
    void unregisterOnPlayListChangedListener(IOnPlayListChangedListener li);
    void unregisterOnDataIsReadyListener(IOnDataIsReadyListener li);

    //设置播放模式 0 列表播放（默认，播放到列表最后时停止播放），1 单曲循环，2列表循环，3 随机播放
    void setPlayMode(int mode);

    //获得当前播放曲目进度
    int getProgress();

    //定位到指定位置
    int seekTo(int pos);

    //移除播放列表中的歌曲（如果播放列表中有该歌曲的话）
    void remove(in Chapter song);

    //获得循环模式
    int getPlayMode();
}
