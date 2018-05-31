package com.qtsreceipts.ui.activity

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.qtsreceipts.ui.fragments.BaseFragment
import com.qtsreceipts.ui.fragments.ReceiptsListFragment
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import receipts.qts.com.qtsreceipts.R
import javax.inject.Inject

class MainActivity : RxAppCompatActivity(), HasSupportFragmentInjector {
    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingFragmentInjector
    }

    companion object {
        const val FRAGMENT_ROOT = "mainRootFragment"
    }

    var mRootFragment: BaseFragment? = null
        get() = supportFragmentManager.findFragmentByTag(FRAGMENT_ROOT) as BaseFragment
        set(value) {
            field = value
            if (field != null) {
                popAllFragments()
                supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, field, FRAGMENT_ROOT)
                        .commit()
            }
        }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        mActionBarTitleTextView.text = title?.toString()?.toUpperCase()
    }

    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    @BindView(R.id.toolbar_actionbar)
    lateinit var mActionBarToolbar: Toolbar

    @BindView(R.id.toolbar_title)
    lateinit var mActionBarTitleTextView: TextView

    private val actionBarToolbar: Toolbar?
        get() {
            setSupportActionBar(mActionBarToolbar)
            return mActionBarToolbar
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        actionBarToolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mRootFragment = ReceiptsListFragment.getInstance()
    }

    fun popAllFragments() {
        if (isFinishing) return
        if (supportFragmentManager.backStackEntryCount > 0) {
            val firstItemId = supportFragmentManager.getBackStackEntryAt(0).id
            supportFragmentManager.popBackStack(firstItemId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun pushFragment(fragment: BaseFragment, tag: String) {
        if (isFinishing) return
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                        fragment.getFragmentEnterAnim(),
                        fragment.getFragmentExitAnim(),
                        fragment.getFragmentPopEnterAnim(),
                        fragment.getFragmentPopExitAnim()
                )
                .replace(R.id.content_frame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(tag)
                .commit()
    }

    fun popFragment() {
        if (isFinishing) return
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStack()
        } else {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // Check if the current fragment wants to handle back presses
        var handled = false

        if (supportFragmentManager.backStackEntryCount > 0 && !isFinishing) {
            popFragment()
            handled = true
        }

        if (!handled) {
            super.onBackPressed()
        }
    }
}
