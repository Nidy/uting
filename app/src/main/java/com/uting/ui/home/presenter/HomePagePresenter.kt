package com.uting.ui.home.presenter

import com.uting.Injection
import com.uting.api.BaseResponse
import com.uting.ui.home.contract.HomePageContract
import com.uting.ui.home.entity.ChapterInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class HomePagePresenter(
    var mView: HomePageContract.View?
) : HomePageContract.Presenter {

    private var mPageNum = 1
    private val mPageSize = 20

    override fun refresh() {
        mPageNum = 1
        load(true)
    }

    init {
        mView?.mPresenter = this
    }

    override fun loadMore() {
        load(false)
    }

    override fun start() {
        refresh()
    }

    private fun load(forceRefresh: Boolean) {
        Injection.provideApiService().getChapters(mPageNum, mPageSize)
            .enqueue(object : Callback<BaseResponse<List<ChapterInfo>>> {
                override fun onFailure(call: Call<BaseResponse<List<ChapterInfo>>>, t: Throwable) {
                    mView?.onLoadError(t.message)
                }

                override fun onResponse(
                    call: Call<BaseResponse<List<ChapterInfo>>>,
                    response: Response<BaseResponse<List<ChapterInfo>>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null && result.status) {
                            mPageNum++
                            val isEnd = result.data.size < 20
                            if (forceRefresh) {

                            }
                            if (forceRefresh) {
                                mView?.onRefresh(result.data, isEnd)
                            } else {
                                mView?.onChaptersLoaded(result.data, isEnd)
                            }
                        } else {
                            mView?.onLoadError(result?.msg)
                        }

                    } else{
                        mView?.onLoadError(response.message())
                    }
                }

            })
    }
}