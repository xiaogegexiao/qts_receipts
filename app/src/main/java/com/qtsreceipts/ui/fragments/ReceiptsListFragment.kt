package com.qtsreceipts.ui.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import com.qtsreceipts.repository.models.Receipt
import com.qtsreceipts.repository.repositories.QtsRepository
import com.qtsreceipts.ui.activity.MainActivity
import com.qtsreceipts.ui.adapter.ReceiptListAdapter
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import receipts.qts.com.qtsreceipts.R
import javax.inject.Inject

class ReceiptsListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, ReceiptListAdapter.OnItemSelectedListener {
    override fun onItemSelected(receipt: Receipt) {
        if (activity is MainActivity) {
            (activity as MainActivity).pushFragment(ReceiptDetailFragment.getInstance(receipt), "receipt detail")
        }
    }

    companion object {
        fun getInstance(): ReceiptsListFragment {
            return ReceiptsListFragment()
        }
    }

    @Inject
    lateinit var mQtsRepository: QtsRepository

    @Inject
    lateinit var mReceiptListAdapter: ReceiptListAdapter

    lateinit var mLayoutManager: GridLayoutManager

    @BindView(android.R.id.list)
    lateinit var mReceiptListView: RecyclerView

    @BindView(R.id.swipe_refresh_layout)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindString(R.string.title_receipt_list)
    lateinit var mTitleReceiptList: String

    var mProcessing = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_receipt_list, container, false)
        ButterKnife.bind(this, view)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)
        mLayoutManager = GridLayoutManager(activity, 1)
        activity?.title = mTitleReceiptList
        mReceiptListView.layoutManager = mLayoutManager
        mReceiptListView.adapter = mReceiptListAdapter
        mReceiptListAdapter.mListener = this
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mReceiptListAdapter.getItemViewType(position)) {
                    ReceiptListAdapter.VIEW_TYPE_SECTION -> mLayoutManager.spanCount
                    ReceiptListAdapter.VIEW_TYPE_TITLE_RECEIPT -> mLayoutManager.spanCount
                    else -> 1
                }
            }
        }
        calculateNumColumns(resources.configuration.orientation)
    }

    override fun onResume() {
        super.onResume()
        fetchLatest()
    }

    override fun onRefresh() {
        fetchLatest()
    }

    private fun setProcessing(processing: Boolean) {
        if (mProcessing != processing) {
            mProcessing = processing
            mSwipeRefreshLayout.isRefreshing = processing
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchLatest() {
        Observable.fromCallable { setProcessing(true)}
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap { mQtsRepository.getReceipts() }
                .map { t ->
                    t.results
                }
                .doFinally { setProcessing(false) }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.PAUSE))
                .subscribe({
                    mReceiptListAdapter.receiptList = it
                }, {
                    Toast.makeText(activity, "Oops, something went wrong!", Toast.LENGTH_SHORT).show()
                })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        calculateNumColumns(newConfig.orientation)
    }

    private fun calculateNumColumns(orientation: Int) {
        mLayoutManager.spanCount = when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }
    }

    override fun getFragmentEnterAnim(): Int {
        return R.anim.slide_left_in
    }

    override fun getFragmentExitAnim(): Int {
        return R.anim.slide_left_out
    }

    override fun getFragmentPopEnterAnim(): Int {
        return R.anim.slide_right_in
    }

    override fun getFragmentPopExitAnim(): Int {
        return R.anim.slide_right_out
    }
}