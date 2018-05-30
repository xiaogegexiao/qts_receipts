package com.qtsreceipts

import android.support.annotation.AnimRes
import com.trello.rxlifecycle2.components.RxFragment

open class BaseFragment: RxFragment() {
    @AnimRes
    open fun getFragmentEnterAnim(): Int = 0

    @AnimRes
    open fun getFragmentExitAnim(): Int = 0

    @AnimRes
    open fun getFragmentPopEnterAnim(): Int = 0

    @AnimRes
    open fun getFragmentPopExitAnim(): Int = 0
}