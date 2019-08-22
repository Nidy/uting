package com.uting.ui.home

import android.content.ComponentName
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import com.uting.R
import com.uting.aidl.Chapter
import com.uting.aidl.IPlayControl
import com.uting.base.BaseActivity
import com.uting.manager.MediaManager
import com.uting.manager.PlayServiceManager
import com.uting.service.OnServiceConnect
import com.uting.service.PlayController
import com.uting.service.PlayServiceCallback
import com.uting.service.PlayServiceConnection
import com.uting.ui.home.contract.PlayContract
import com.uting.ui.home.entity.ChapterInfo
import com.uting.ui.home.presenter.PlayPresenter
import com.uting.ui.widget.PlayListBottomSheetDialog
import com.uting.ui.widget.RecordView

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class PlayActivity : BaseActivity(), PlayContract.View, OnServiceConnect, PlayServiceCallback {

    companion object {
        val TAG: String = "PlayActivity"
    }

    override lateinit var mPresenter: PlayContract.Presenter
    private lateinit var mChapterInfo: ChapterInfo
    private var mIndex: Int = 0

    private lateinit var mIvPlay: ImageView
    private lateinit var mRecordView: RecordView

    private var mPlayControl: IPlayControl? = null
    private lateinit var mPlayServiceConnection: PlayServiceConnection
    private var mPlayServiceManager: PlayServiceManager? = null
    private var mPlayListDialog: PlayListBottomSheetDialog? = null

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mIvPlay = findViewById(R.id.iv_play)
        mRecordView = findViewById(R.id.record_view)

        mChapterInfo = intent.getSerializableExtra("chapterInfo") as ChapterInfo
        mIndex = intent.getSerializableExtra("index") as Int
        mRecordView.setChapter(mChapterInfo)

        bindService()

    }

    override fun setToolbarTrans(): Boolean {
        return true
    }

    override fun registerListeners() {
        super.registerListeners()
        mIvPlay.setOnClickListener {

            mPlayControl?.run {
                if (PlayController.get().STATUS_PLAYING == status()) {
                    pause()
                } else {
                    resume()
                }
            }
        }

        findViewById<ImageView>(R.id.iv_play_list).setOnClickListener {
            mPlayControl?.let {
                mPlayListDialog?.show()
            }
        }

        findViewById<ImageView>(R.id.iv_pre).setOnClickListener {
            mPlayControl?.pre()
        }

        findViewById<ImageView>(R.id.iv_next).setOnClickListener {
            mPlayControl?.next()
        }
    }

    private fun bindService() {
        mPlayServiceManager = PlayServiceManager(this)
        mPlayServiceConnection = PlayServiceConnection(this, this, this)
        mPlayServiceManager?.bindPlayService(mPlayServiceConnection)
    }

    override fun onConnected(name: ComponentName, service: IBinder) {
        mPlayControl = IPlayControl.Stub.asInterface(service)
        mPlayControl?.let {
            mRecordView.initData(it)
            songChanged(MediaManager.convertChapter(mChapterInfo), mIndex)
            mPlayListDialog = PlayListBottomSheetDialog(this, it)
        }

        Log.i(TAG, "Service onConnected!")
    }

    override fun disConnected(name: ComponentName) {
        mPlayServiceConnection = PlayServiceConnection(this, this, this)
        mPlayServiceManager?.bindPlayService(mPlayServiceConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayServiceConnection.unRegisterListeners()
        unbindService(mPlayServiceConnection)
        mPlayServiceManager = null
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun songChanged(song: Chapter, index: Int) {
        mPlayControl?.setCurrentSong(MediaManager.convertChapter(mChapterInfo))
        mPlayListDialog?.setSelectPos(mIndex)
    }

    override fun startPlay(song: Chapter, index: Int, status: Int) {
        mRecordView.play()
        when (mRecordView.mMusicStatus) {
            RecordView.MusicStatus.PLAY -> mIvPlay.setImageResource(R.drawable.ic_pause)
            RecordView.MusicStatus.STOP -> mIvPlay.setImageResource(R.drawable.ic_pause)
            RecordView.MusicStatus.PAUSE -> mIvPlay.setImageResource(R.drawable.ic_play)
        }
    }

    override fun stopPlay(song: Chapter, index: Int, status: Int) {
        mRecordView.pause()
        when (mRecordView.mMusicStatus) {
            RecordView.MusicStatus.PLAY -> mIvPlay.setImageResource(R.drawable.ic_pause)
            RecordView.MusicStatus.STOP -> mIvPlay.setImageResource(R.drawable.ic_pause)
            RecordView.MusicStatus.PAUSE -> mIvPlay.setImageResource(R.drawable.ic_play)
        }
    }

    override fun onPlayListChange(current: Chapter, index: Int) {

    }

    override fun dataIsReady(mControl: IPlayControl) {

    }
}