package com.qtsreceipts

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import receipts.qts.com.qtsreceipts.R

class MainActivity : RxAppCompatActivity() {
    companion object {
        const val FRAGMENT_ROOT = "mainRootFragment"
    }

    var mRootFragment: BaseFragment? = null
        get() = fragmentManager.findFragmentByTag(FRAGMENT_ROOT) as BaseFragment
        set(value) {
            field = value
            if (field != null) {
                popAllFragments()
                fragmentManager.beginTransaction()
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        actionBarToolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mRootFragment = ReceiptsListFragment.getInstance()
    }

    fun popAllFragments() {
        if (isFinishing) return
        if (fragmentManager.backStackEntryCount > 0) {
            val firstItemId = fragmentManager.getBackStackEntryAt(0).id
            fragmentManager.popBackStack(firstItemId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun pushFragment(fragment: BaseFragment, tag: String) {
        if (isFinishing) return
        fragmentManager
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
        val count = fragmentManager.backStackEntryCount
        if (count > 0) {
            fragmentManager.popBackStack()
        } else {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // Check if the current fragment wants to handle back presses
        var handled = false

        if (fragmentManager.backStackEntryCount > 0 && !isFinishing) {
            popFragment()
            handled = true
        }

        if (!handled) {
            super.onBackPressed()
        }
    }
}
