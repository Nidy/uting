package com.uting.ui.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.uting.R
import com.uting.service.PlayController
import com.uting.ui.home.entity.ChapterInfo
import com.uting.util.DisplayUtils




/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
class RecordView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null,  defStyleAttr: Int = 0)
    : RelativeLayout(context, attributes, defStyleAttr) {

    private var mScreenWidth: Int = 0
    private var mScreenHeight: Int = 0
    private lateinit var mIvNeedle: ImageView
    private lateinit var mIvDisc: ImageView
    private lateinit var mNeedleAnimator: ObjectAnimator
    /*标记唱针复位后，是否需要重新偏移到唱片处*/
    private var mIsNeed2StartPlayAnimator = false
    var mMusicStatus = MusicStatus.STOP
    private var mNeedleAnimatorStatus = NeedleAnimatorStatus.IN_FAR_END
    private lateinit var mDiscAnimators: ObjectAnimator
    private val mIPlayInfo: IPlayInfo? = null
    private lateinit var mChapterInfo: ChapterInfo


    init {
        mScreenWidth = DisplayUtils.getScreenWidth(context);
        mScreenHeight = DisplayUtils.getScreenHeight(context);
    }

    enum class NeedleAnimatorStatus {
        /*移动时：从唱盘往远处移动*/
        TO_FAR_END,
        /*移动时：从远处往唱盘移动*/
        TO_NEAR_END,
        /*静止时：离开唱盘*/
        IN_FAR_END,
        /*静止时：贴近唱盘*/
        IN_NEAR_END
    }

    enum class MusicStatus {
        PLAY, PAUSE, STOP
    }

    enum class MusicChangedStatus {
        PLAY, PAUSE, NEXT, LAST, STOP
    }

    interface  IPlayInfo {
        fun onMusicInfoChanged(chapterName: String, author: String)

        fun onMusicChanged(musicChangedStatus: MusicChangedStatus)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initDiscBackground()
        initNeedle()
        initObjectAnimator()
        initDiscPicLayout()
    }

    private fun initDiscBackground() {
        val mDiscBackground = findViewById<ImageView>(com.uting.R.id.ivDiscBlackgound);
        mDiscBackground.setImageDrawable(getDiscBackgroundDrawable());

        val marginTop = DisplayUtils.SCALE_DISC_MARGIN_TOP * mScreenHeight
        val layoutParams = mDiscBackground.getLayoutParams() as LayoutParams
        layoutParams.setMargins(0, marginTop.toInt(), 0, 0)

        mDiscBackground.layoutParams = layoutParams
    }

    private fun initNeedle() {
        mIvNeedle = findViewById(com.uting.R.id.iv_needle)
        val needleWidth = DisplayUtils.SCALE_NEEDLE_WIDTH * mScreenWidth
        val needleHeight = DisplayUtils.SCALE_NEEDLE_HEIGHT * mScreenHeight

        val marginTop = DisplayUtils.SCALE_NEEDLE_MARGIN_TOP * mScreenHeight * -1
        val marginLeft = DisplayUtils.SCALE_NEEDLE_MARGIN_LEFT * mScreenWidth

        val originBitmap = BitmapFactory.decodeResource(resources, com.uting.R.mipmap.ic_needle)
        val bitmap = Bitmap.createScaledBitmap(originBitmap, needleWidth.toInt(), needleHeight.toInt(), false)

        val lp = mIvNeedle.layoutParams as LayoutParams
        lp.setMargins(marginLeft.toInt(), marginTop.toInt(), 0, 0)
        mIvNeedle.pivotX = (DisplayUtils.SCALE_NEEDLE_PIVOT_X * mScreenWidth).toFloat()
        mIvNeedle.pivotY = ((DisplayUtils.SCALE_NEEDLE_PIVOT_Y * mScreenHeight).toFloat())
        mIvNeedle.rotation = DisplayUtils.ROTATION_INIT_NEEDLE.toFloat()
        mIvNeedle.setImageBitmap(bitmap)
        mIvNeedle.layoutParams = lp
    }

    private fun initObjectAnimator() {
        mNeedleAnimator = ObjectAnimator.ofFloat(mIvNeedle, View.ROTATION, DisplayUtils
            .ROTATION_INIT_NEEDLE.toFloat(), 0f)
        mNeedleAnimator.duration = 500
        mNeedleAnimator.interpolator = AccelerateInterpolator()
        mNeedleAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
                //do nothing
            }

            override fun onAnimationEnd(p0: Animator?) {
                if (mNeedleAnimatorStatus === NeedleAnimatorStatus.TO_NEAR_END) {
                    mNeedleAnimatorStatus = NeedleAnimatorStatus.IN_NEAR_END
                    playDiscAnimator()
                    mMusicStatus = MusicStatus.PLAY
                } else if (mNeedleAnimatorStatus === NeedleAnimatorStatus.TO_FAR_END) {
                    mNeedleAnimatorStatus = NeedleAnimatorStatus.IN_FAR_END
                    if (mMusicStatus === MusicStatus.STOP) {
                        mIsNeed2StartPlayAnimator = true
                    }
                }

                if (mIsNeed2StartPlayAnimator) {
                    mIsNeed2StartPlayAnimator = false
                    playAnimator()
                }
            }

            override fun onAnimationCancel(p0: Animator?) {
                //do nothing
            }

            override fun onAnimationStart(p0: Animator?) {
                /**
                 * 根据动画开始前NeedleAnimatorStatus的状态，
                 * 即可得出动画进行时NeedleAnimatorStatus的状态
                 * */
                if (mNeedleAnimatorStatus == NeedleAnimatorStatus.IN_FAR_END) {
                    mNeedleAnimatorStatus = NeedleAnimatorStatus.TO_NEAR_END;
                } else if (mNeedleAnimatorStatus == NeedleAnimatorStatus.IN_NEAR_END) {
                    mNeedleAnimatorStatus = NeedleAnimatorStatus.TO_FAR_END;
                }
            }

        })
    }

    private fun getDiscBackgroundDrawable(): Drawable {
        val discSize = mScreenWidth * DisplayUtils.SCALE_DISC_SIZE
        val bitmapDisc = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, com.uting.R.mipmap.ic_disc_blackground),
            discSize.toInt(), discSize.toInt(), false)
        return RoundedBitmapDrawableFactory.create(resources, bitmapDisc)
    }

    private fun getDiscObjectAnimator(): ObjectAnimator {
        val objectAnimator = ObjectAnimator.ofFloat<View>(mIvDisc, View.ROTATION, 0f, 360f)
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE)
        objectAnimator.setDuration((20 * 1000).toLong())
        objectAnimator.setInterpolator(LinearInterpolator())

        return objectAnimator
    }

    private fun initDiscPicLayout() {
        val drawable = getDiscDrawable(R.drawable.ic_music2)
        mIvDisc = findViewById(R.id.iv_disc)
        mIvDisc.setImageDrawable(drawable)
        val layoutParams = mIvDisc.getLayoutParams() as LayoutParams
        val marginTop = (DisplayUtils.SCALE_DISC_MARGIN_TOP * mScreenHeight).toInt()
        layoutParams.setMargins(0, marginTop, 0, 0)
        mIvDisc.setLayoutParams(layoutParams)
    }

    /**
     * 得到唱盘图片
     * 唱盘图片由空心圆盘及音乐专辑图片“合成”得到
     */
    private fun getDiscDrawable(musicPicRes: Int): Drawable {
        val discSize = (mScreenWidth * DisplayUtils.SCALE_DISC_SIZE).toInt()
        val musicPicSize = (mScreenWidth * DisplayUtils.SCALE_MUSIC_PIC_SIZE).toInt()

        val bitmapDisc = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(
                resources, com.uting.R.mipmap.ic_disc
            ), discSize, discSize, false
        )
        val bitmapMusicPic = getMusicPicBitmap(musicPicSize, musicPicRes)
        val discDrawable = BitmapDrawable(bitmapDisc)
        val roundMusicDrawable = RoundedBitmapDrawableFactory.create(resources, bitmapMusicPic)

        //抗锯齿
        discDrawable.setAntiAlias(true)
        roundMusicDrawable.setAntiAlias(true)

        val drawables = arrayOfNulls<Drawable>(2)
        drawables[0] = roundMusicDrawable
        drawables[1] = discDrawable

        val layerDrawable = LayerDrawable(drawables)
        val musicPicMargin = ((DisplayUtils.SCALE_DISC_SIZE - DisplayUtils
            .SCALE_MUSIC_PIC_SIZE) * mScreenWidth / 2).toInt()
        //调整专辑图片的四周边距，让其显示在正中
        layerDrawable.setLayerInset(
            0, musicPicMargin, musicPicMargin, musicPicMargin,
            musicPicMargin
        )

        return layerDrawable
    }

    private fun getMusicPicBitmap(musicPicSize: Int, musicPicRes: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeResource(resources, musicPicRes, options)
        val imageWidth = options.outWidth

        val sample = imageWidth / musicPicSize
        var dstSample = 1
        if (sample > dstSample) {
            dstSample = sample
        }
        options.inJustDecodeBounds = false
        //设置图片采样率
        options.inSampleSize = dstSample
        //设置图片解码格式
        options.inPreferredConfig = Bitmap.Config.RGB_565

        return Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(
                resources,
                musicPicRes, options
            ), musicPicSize, musicPicSize, true
        )
    }

    /*播放动画*/
    private fun playAnimator() {
        /*唱针处于远端时，直接播放动画*/
        if (mNeedleAnimatorStatus === NeedleAnimatorStatus.IN_FAR_END) {
            mNeedleAnimator.start()
        } else if (mNeedleAnimatorStatus === NeedleAnimatorStatus.TO_FAR_END) {
            mIsNeed2StartPlayAnimator = true
        }/*唱针处于往远端移动时，设置标记，等动画结束后再播放动画*/
    }

    /*暂停动画*/
    private fun pauseAnimator() {
        /*播放时暂停动画*/
        if (mNeedleAnimatorStatus === NeedleAnimatorStatus.IN_NEAR_END) {

            pauseDiscAnimatior()
        } else if (mNeedleAnimatorStatus === NeedleAnimatorStatus.TO_NEAR_END) {
            mNeedleAnimator.reverse()
            /**
             * 若动画在没结束时执行reverse方法，则不会执行监听器的onStart方法，此时需要手动设置
             */
            mNeedleAnimatorStatus = NeedleAnimatorStatus.TO_FAR_END
        }/*唱针往唱盘移动时暂停动画*/
        /**
         * 动画可能执行多次，只有音乐处于停止 / 暂停状态时，才执行暂停命令
         */
        if (mMusicStatus === MusicStatus.STOP) {
            notifyMusicStatusChanged(MusicChangedStatus.STOP)
        } else if (mMusicStatus === MusicStatus.PAUSE) {
            notifyMusicStatusChanged(MusicChangedStatus.PAUSE)
        }
    }


    /*播放唱盘动画*/
    private fun playDiscAnimator() {
        if (mDiscAnimators.isPaused()) {
            mDiscAnimators.resume()
        } else {
            mDiscAnimators.start()
        }
        /**
         * 唱盘动画可能执行多次，只有不是音乐不在播放状态，在回调执行播放
         */
        if (mMusicStatus !== MusicStatus.PLAY) {
            notifyMusicStatusChanged(MusicChangedStatus.PLAY)
        }
    }

    /*暂停唱盘动画*/
    private fun pauseDiscAnimatior() {
        mDiscAnimators.pause()
        mNeedleAnimator.reverse()
    }

    fun setChapter(chapterInfo: ChapterInfo) {
        this.mChapterInfo = chapterInfo
        PlayController.get().setChapter(chapterInfo = mChapterInfo)
        mDiscAnimators = getDiscObjectAnimator()
    }

    fun notifyMusicInfoChanged(position: Int) {
        if (mIPlayInfo != null) {
            mIPlayInfo.onMusicInfoChanged(mChapterInfo.title, mChapterInfo.nikename)
        }
    }

    fun notifyMusicStatusChanged(musicChangedStatus: MusicChangedStatus) {
        if (mIPlayInfo != null) {
            mIPlayInfo.onMusicChanged(musicChangedStatus)
        }
    }

    private fun play() {
        mMusicStatus = MusicStatus.PLAY
        playAnimator()
        PlayController.get().play()
    }

    private fun pause() {
        mMusicStatus = MusicStatus.PAUSE
        pauseAnimator()
        PlayController.get().pause()
    }

    fun stop() {
        mMusicStatus = MusicStatus.STOP
        pauseAnimator()
    }

    fun playOrPause() {
        if (mMusicStatus === MusicStatus.PLAY) {
            pause()
        } else {
            play()
        }
    }

}