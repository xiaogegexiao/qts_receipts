package com.qtsreceipts.ui.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.qtsreceipts.repository.models.Receipt
import com.squareup.picasso.Picasso
import receipts.qts.com.qtsreceipts.R

class ReceiptListAdapter(var mPicasso: Picasso, var mListener: OnItemSelectedListener?) : RecyclerView.Adapter<ReceiptListAdapter.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SECTION = 0
        const val VIEW_TYPE_IMAGE_RECEIPT = 1
        const val VIEW_TYPE_TITLE_RECEIPT = 2
    }

    private var imageReceiptList = arrayListOf<Receipt>()
    private var titleReceiptList = arrayListOf<Receipt>()
    var receiptList = arrayListOf<Receipt>()
        set(value) {
            field.clear()
            field.addAll(value)
            imageReceiptList.clear()
            titleReceiptList.clear()
            value.forEach {
                if (it.thumbnail.isEmpty()) {
                    titleReceiptList.add(it)
                } else {
                    imageReceiptList.add(it)
                }
            }
            notifyDataSetChanged()
        }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            ButterKnife.bind(this, itemView)
        }
    }

    internal inner class SectionViewHolder(itemView: View) : ViewHolder(itemView) {
        @BindView(R.id.section_title)
        lateinit var mSectionTitleTextView: TextView
    }

    internal inner class ImageReceiptViewHolder(itemView: View) : ViewHolder(itemView) {
        @BindView(R.id.title)
        lateinit var mTitle: TextView

        @BindView(R.id.image)
        lateinit var mImage: ImageView

        init {
            itemView.setOnClickListener {
                mListener?.onItemSelected(itemView.tag as Receipt)
            }
        }
    }

    internal inner class TitleReceiptViewHolder(itemView: View) : ViewHolder(itemView) {
        @BindView(R.id.title)
        lateinit var mTitle: TextView

        init {
            itemView.setOnClickListener {
                mListener?.onItemSelected(itemView.tag as Receipt)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SECTION -> SectionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_section, parent, false))
            VIEW_TYPE_IMAGE_RECEIPT -> ImageReceiptViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_image_receipt, parent, false))
            else -> TitleReceiptViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_title_receipt, parent, false))
        }
    }

    override fun getItemCount(): Int {
        var res = 0
        res += 1 + imageReceiptList.size
        res += 1 + titleReceiptList.size
        return res
    }

    override fun getItemViewType(position: Int): Int {
        var tempPosition = position
        if (tempPosition == 0) {
            return VIEW_TYPE_SECTION
        }
        tempPosition--
        if (!imageReceiptList.isEmpty() && tempPosition < imageReceiptList.size) {
            return VIEW_TYPE_IMAGE_RECEIPT
        }
        tempPosition -= imageReceiptList.size
        if (tempPosition == 0) {
            return VIEW_TYPE_SECTION
        }
        return VIEW_TYPE_TITLE_RECEIPT
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            VIEW_TYPE_SECTION -> renderSection(holder as SectionViewHolder, getSectionStringForPosition(position))
            VIEW_TYPE_IMAGE_RECEIPT -> renderImageReceipt(holder as ImageReceiptViewHolder, getReceiptForPosition(position))
            VIEW_TYPE_TITLE_RECEIPT -> renderTitleReceipt(holder as TitleReceiptViewHolder, getReceiptForPosition(position))
        }
    }

    private fun renderSection(sectionViewHolder: SectionViewHolder, sectionTitleRes: Int) {
        sectionViewHolder.mSectionTitleTextView.setText(sectionTitleRes)
    }

    private fun renderImageReceipt(imageReceiptViewHolder: ImageReceiptViewHolder, imageReceipt: Receipt) {
        mPicasso.cancelRequest(imageReceiptViewHolder.mImage)
        mPicasso.load(Uri.parse(imageReceipt.thumbnail)).into(imageReceiptViewHolder.mImage)
        imageReceiptViewHolder.mTitle.text = imageReceipt.title.trim()
        imageReceiptViewHolder.itemView.tag = imageReceipt
    }

    private fun renderTitleReceipt(titleReceiptViewHolder: TitleReceiptViewHolder, titleReceipt: Receipt) {
        titleReceiptViewHolder.mTitle.text = titleReceipt.title.trim()
        titleReceiptViewHolder.itemView.tag = titleReceipt
    }

    private fun getSectionStringForPosition(position: Int): Int {
        return if (position == 0) {
            R.string.label_popular
        } else {
            R.string.label_other
        }
    }

    private fun getReceiptForPosition(position: Int): Receipt {
        return if (position <= imageReceiptList.size) {
            imageReceiptList[position - 1]
        } else {
            titleReceiptList[position - 2 - imageReceiptList.size]
        }
    }

    interface OnItemSelectedListener{
        fun onItemSelected(receipt: Receipt)
    }
}