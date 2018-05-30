package com.qtsreceipts

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindString
import butterknife.BindView
import butterknife.ButterKnife
import receipts.qts.com.qtsreceipts.R

class ReceiptsListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun getInstance(): ReceiptsListFragment {
            return ReceiptsListFragment()
        }
    }

    @BindView(android.R.id.list)
    lateinit var mReceiptListView: RecyclerView

    @BindView(R.id.swipe_refresh_layout)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindString(R.string.title_receipt_list)
    lateinit var mTitleReceiptList: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_receipt_list, container, false)
        ButterKnife.bind(this, view)
        mSwipeRefreshLayout.setOnRefreshListener(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)
        activity.title = mTitleReceiptList
    }

    override fun onRefresh() {

    }
}