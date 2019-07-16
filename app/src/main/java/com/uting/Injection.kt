package com.uting

import com.uting.api.ApiService
import com.uting.api.RetrofitClient

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
object Injection {
    fun provideApiService(): ApiService {
        return RetrofitClient.INSTANCE
    }
}