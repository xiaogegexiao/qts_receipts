package com.qtsreceipts.di.modules

import com.qtsreceipts.di.scopes.ActivityScope
import com.qtsreceipts.di.scopes.FragmentScope
import com.qtsreceipts.ui.activity.MainActivity
import com.qtsreceipts.ui.fragments.ReceiptDetailFragment
import com.qtsreceipts.ui.fragments.ReceiptsListFragment
import com.qtsreceipts.ui.fragments.WebPageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AndroidBuilder {
    @ActivityScope
    @ContributesAndroidInjector()
    internal abstract fun bindMainActivity(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector(modules = [(ReceiptListModule::class)])
    internal abstract fun provideReceiptListFragment(): ReceiptsListFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun provideReceiptDetailFragment(): ReceiptDetailFragment

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun provideWebPageFragment(): WebPageFragment
}