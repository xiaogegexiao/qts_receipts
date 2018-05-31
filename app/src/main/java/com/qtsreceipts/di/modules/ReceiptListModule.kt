package com.qtsreceipts.di.modules

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.qtsreceipts.ui.adapter.ReceiptListAdapter
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class ReceiptListModule {
    @Provides
    open fun provideReceiptListAdapter(picasso: Picasso): ReceiptListAdapter {
        return ReceiptListAdapter(picasso, null)
    }

    @Provides
    fun provideLayoutManager(context: Context): GridLayoutManager {
        return GridLayoutManager(context, 1)
    }
}