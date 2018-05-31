package com.qtsreceipts.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.BindView
import butterknife.ButterKnife
import com.qtsreceipts.repository.models.Receipt
import receipts.qts.com.qtsreceipts.R


/**
 * A simple [BaseFragment] subclass.
 * Use the [WebPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebPageFragment : BaseFragment() {

    private var mReceipt: Receipt? = null

    @BindView(R.id.webview)
    lateinit var mWebView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            mReceipt = it.getParcelable(ARG_RECEIPT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_page, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mReceipt == null) {
            return
        }
        activity?.title = mReceipt!!.title

        mWebView.settings.blockNetworkImage = false
        mWebView.settings.domStorageEnabled = true
        mWebView.webChromeClient = WebChromeClient()
        mWebView.webViewClient = WebViewClient()
        mWebView.loadUrl(mReceipt!!.href)
    }

    override fun onDetach() {
        mWebView.stopLoading()
        super.onDetach()
    }

    companion object {
        const val ARG_RECEIPT = "arg_receipt"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WebPageFragment.
         */
        fun newInstance(receipt: Receipt): WebPageFragment {
            val fragment = WebPageFragment()
            fragment.arguments = Bundle().apply {
                this.putParcelable(ARG_RECEIPT, receipt)
            }
            return fragment
        }
    }

    override fun getFragmentEnterAnim(): Int {
        return R.anim.slide_up_in
    }

    override fun getFragmentExitAnim(): Int {
        return R.anim.slide_up_out
    }

    override fun getFragmentPopEnterAnim(): Int {
        return R.anim.slide_down_in
    }

    override fun getFragmentPopExitAnim(): Int {
        return R.anim.slide_down_out
    }
}
