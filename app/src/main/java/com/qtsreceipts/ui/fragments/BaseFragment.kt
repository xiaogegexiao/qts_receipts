package com.qtsreceipts.ui.fragments

import android.content.Context
import android.support.annotation.AnimRes
import com.trello.rxlifecycle2.components.support.RxFragment
import dagger.android.support.AndroidSupportInjection

open class BaseFragment : RxFragment() {

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @AnimRes
    open fun getFragmentEnterAnim(): Int = 0

    @AnimRes
    open fun getFragmentExitAnim(): Int = 0

    @AnimRes
    open fun getFragmentPopEnterAnim(): Int = 0

    @AnimRes
    open fun getFragmentPopExitAnim(): Int = 0
}