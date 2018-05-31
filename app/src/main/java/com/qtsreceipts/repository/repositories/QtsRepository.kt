package com.qtsreceipts.repository.repositories

import com.qtsreceipts.repository.apis.QtsApi
import com.qtsreceipts.repository.models.ReceiptsResponse
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QtsRepository @Inject constructor(
        private val qtsApi: QtsApi) {
    fun getReceipts(): Observable<ReceiptsResponse> {
        return qtsApi.listReceipts()
    }
}