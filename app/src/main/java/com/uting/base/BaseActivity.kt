package com.uting.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class BaseActivity : AppCompatActivity() {

    protected open var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        Toast.makeText(this, "MainActivity init", Toast.LENGTH_SHORT).show()
        setUpView()

        if (setToolbarTrans() && mToolbar != null) {
            transparentToolbar()
        }
        registerListeners()
    }

    @LayoutRes
    abstract fun getContentView() : Int

    protected open fun createPresenter() {

    }

    protected open fun setUpView() {

    }

    protected open fun registerListeners() {

    }

    protected open fun setToolbarTrans(): Boolean {
        return false
    }

    private fun transparentToolbar() {
        val window = getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(Color.TRANSPARENT)
    }
}