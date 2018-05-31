package com.qtsreceipts.ui.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.qtsreceipts.repository.models.Receipt
import com.qtsreceipts.ui.activity.MainActivity
import com.squareup.picasso.Picasso
import receipts.qts.com.qtsreceipts.R
import javax.inject.Inject

class ReceiptDetailFragment : BaseFragment() {

    companion object {
        private const val ARG_RECEIPT = "arg_receipt"
        fun getInstance(receipt: Receipt): ReceiptDetailFragment {
            val fragment = ReceiptDetailFragment()
            fragment.arguments = Bundle().apply {
                this.putParcelable(ARG_RECEIPT, receipt)
            }
            return fragment
        }
    }

    @BindView(R.id.image)
    lateinit var mImageView: ImageView

    @BindView(R.id.title)
    lateinit var mTitleTextView: TextView

    @BindView(R.id.ingredients)
    lateinit var mIngredientsTextView: TextView

    @BindView(R.id.link)
    lateinit var mLinkTextView: TextView

    @OnClick(R.id.link)
    fun onLinkClicked() {
        if (activity is MainActivity) {
            mReceipt?.let {
                (activity as MainActivity).pushFragment(WebPageFragment.newInstance(it), "view href")
            }
        }
    }

    @Inject
    lateinit var mPicasso: Picasso

    var mReceipt: Receipt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mReceipt = it.getParcelable(ARG_RECEIPT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_receipt_detail, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (mReceipt == null) {
            (activity as MainActivity).popFragment()
            return
        }
        setHasOptionsMenu(false)
        activity?.title = mReceipt!!.title
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        mReceipt?.let {
            if (!it.thumbnail.isEmpty()) {
                mPicasso.load(it.thumbnail).into(mImageView)
            }
            mTitleTextView.text = it.title
            mIngredientsTextView.text = it.ingredients
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLinkTextView.text = Html.fromHtml(
                        "<a href=\"" + it.href + "\">Link</a>", Html.FROM_HTML_MODE_LEGACY)
            } else {
                mLinkTextView.text = Html.fromHtml(
                        "<a href=\"" + it.href + "\">Link</a>")
            }
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