package com.uting.util

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
object DateUtils {
    fun formatSecToStr(duration: Int): String {
        var time = if (duration < 60) {
            String.format("00:%02d", duration % 60)
        } else if (duration < 60) {
            String.format("%02d:%02d", duration/60, duration%60)
        } else{
            String.format("%02d:%02d:%02d",duration/3600, duration%3600/60, duration%60)
        }
        return time
    }
}