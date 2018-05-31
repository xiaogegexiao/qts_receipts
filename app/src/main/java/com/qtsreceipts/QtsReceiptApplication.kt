package com.qtsreceipts

import android.app.Activity
import android.app.Application
import com.qtsreceipts.di.components.AppComponent
import com.qtsreceipts.di.components.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class QtsReceiptApplication : Application(), HasActivityInjector {
    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var mAppComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        // Dependency injection setup
        mAppComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
        mAppComponent.inject(this)
    }
}