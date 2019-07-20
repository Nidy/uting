package com.uting.ui.home

import android.widget.ImageView
import com.uting.R
import com.uting.base.BaseActivity
import com.uting.ui.home.contract.PlayContract
import com.uting.ui.home.entity.ChapterInfo
import com.uting.ui.home.presenter.PlayPresenter
import com.uting.ui.widget.RecordView

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayActivity : BaseActivity(), PlayContract.View {

    override lateinit var mPresenter: PlayContract.Presenter
    lateinit var mChapterInfo: ChapterInfo

    private lateinit var mIvPlay: ImageView
    private lateinit var mRecordView: RecordView

    override fun getContentView(): Int {
        return R.layout.activity_play
    }

    override fun createPresenter() {
        mPresenter = PlayPresenter(this)
    }

    override fun setUpView() {
        super.setUpView()

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        mIvPlay = findViewById(R.id.iv_play)
        mRecordView = findViewById(R.id.record_view)

        mChapterInfo = intent.getSerializableExtra("chapterInfo") as ChapterInfo
        mRecordView.setChapter(mChapterInfo)

    }

    override fun setToolbarTrans(): Boolean {
        return true
    }

    override fun registerListeners() {
        super.registerListeners()
        mIvPlay.setOnClickListener {
            mRecordView.playOrPause()
            when (mRecordView.mMusicStatus) {
                RecordView.MusicStatus.PLAY -> mIvPlay.setImageResource(R.drawable.ic_pause)
                RecordView.MusicStatus.STOP -> mIvPlay.setImageResource(R.drawable.ic_pause)
                RecordView.MusicStatus.PAUSE -> mIvPlay.setImageResource(R.drawable.ic_play)
            }
        }
    }

    override fun onRefresh(chapters: List<ChapterInfo>, isEnd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChaptersLoaded(chapters: List<ChapterInfo>, isEnd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadError(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}