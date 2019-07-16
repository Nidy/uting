package com.uting.ui.home.entity

import java.io.Serializable

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */

data class ChapterInfo(
    var id: Int,
    var orderNo: Int,
    var duration: Int,
    var title: String,
    var playUrl32: String,
    var playUrl64: String,
    var playPathAacv164: String,
    var playPathAacv224: String,
    var coverSmall: String,
    var coverMiddle: String,
    var coverLarge: String,
    var nikename: String,
    var smallLogo: String,
    var createdAt: String
    ) :
        Serializable
