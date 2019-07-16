package com.uting.api

import java.io.Serializable


/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */

class BaseResponse<T> (

    val status: Boolean,

    val msg: String,

    var data: T

) : Serializable