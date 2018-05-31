package com.qtsreceipts.di.components

import android.content.Context
import com.qtsreceipts.QtsReceiptApplication
import com.qtsreceipts.di.modules.AndroidBuilder
import com.qtsreceipts.di.modules.ApiModule
import com.qtsreceipts.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AppModule::class),
    (ApiModule::class),
    (AndroidBuilder::class)])
interface AppComponent {
    fun inject(application: QtsReceiptApplication)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(applicationContex: Context): Builder
    }
}