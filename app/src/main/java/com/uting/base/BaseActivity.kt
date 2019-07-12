package com.uting.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        Toast.makeText(this, "MainActivity init", Toast.LENGTH_SHORT).show()
        setUpView()

        registerListeners()
    }

    @LayoutRes
    abstract fun getContentView() : Int

    protected open fun setUpView() {

    }

    protected open fun registerListeners() {

    }
}