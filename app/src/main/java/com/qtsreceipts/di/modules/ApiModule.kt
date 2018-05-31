package com.qtsreceipts.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.qtsreceipts.EnvVariants
import com.qtsreceipts.repository.apis.QtsApi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import receipts.qts.com.qtsreceipts.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class ApiModule {
    companion object {
        private const val OK_HTTP_CONNECT_TIMEOUT = 20000L
        private const val OK_HTTP_READ_TIMEOUT = 20000L
    }

    @Provides
    @Singleton
    open fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }

    /**
     * Dagger provides okhttpclient for http calls
     * @param httpLoggingInterceptor
     * @return
     */
    @Provides
    @Singleton
    internal fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(OK_HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(OK_HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideQtsApiClient(
            okHttpClient: OkHttpClient,
            gson: Gson): QtsApi {
        return Retrofit
                .Builder()
                .baseUrl(EnvVariants.HOST_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(QtsApi::class.java)
    }
}