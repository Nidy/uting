package com.uting.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 *  @author YangJijun <jijun.yang@56qq.com>
 */
abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        createPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getContentViewLayout(), container, false)
        setUpView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerListeners()
    }

    @LayoutRes
    abstract fun getContentViewLayout() : Int

    open fun createPresenter() {

    }

    open fun setUpView(view: View) {

    }

    open fun registerListeners() {

    }
}